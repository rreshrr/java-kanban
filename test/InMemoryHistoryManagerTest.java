package test;

import controllers.HistoryManager;
import controllers.TaskManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Managers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
        taskManager = Managers.getDefault();
    }

    @Test
    void testAddTaskToHistory() {
        Task task = new Task("History Task", "History Description", Status.NEW);
        Long taskId = taskManager.create(task);
        Task storedTask = taskManager.getTask(taskId);

        historyManager.add(storedTask);

        assertEquals(1, historyManager.getHistory().size(), "History should contain one task");
        assertTrue(historyManager.getHistory().contains(storedTask), "History should contain the added task");
    }

    @Test
    void testTaskImmutabilityInHistory() {
        Task task = new Task("Immutable History Task", "Description", Status.NEW);
        Long taskId = taskManager.create(task);
        Task storedTask = taskManager.getTask(taskId);

        historyManager.add(storedTask);

        Task historyTask = historyManager.getHistory().getFirst();
        assertEquals(storedTask.getId(), historyTask.getId(), "Task ID should remain unchanged in history");
        assertEquals(storedTask.getName(), historyTask.getName(), "Task name should remain unchanged in history");
        assertEquals(storedTask.getDescription(), historyTask.getDescription(), "Task description should remain unchanged in history");
        assertEquals(storedTask.getStatus(), historyTask.getStatus(), "Task status should remain unchanged in history");
    }

    @Test
    void testTaskImmutabilityInHistoryAfterModification() {
        Task task = new Task("Initial Task", "Initial Description", Status.NEW);
        Long taskId = taskManager.create(task);

        Task taskFromManager = taskManager.getTask(taskId);
        historyManager.add(taskFromManager);

        Task historyTaskSnapshot = historyManager.getHistory().getFirst();

        taskFromManager.setName("Modified Task");
        taskFromManager.setDescription("Modified Description");
        taskFromManager.setStatus(Status.DONE);
        taskManager.update(taskFromManager);

        assertEquals("Initial Task", historyTaskSnapshot.getName(), "Task name in history should remain unchanged");
        assertEquals("Initial Description", historyTaskSnapshot.getDescription(), "Task description in history should remain unchanged");
        assertEquals(Status.NEW, historyTaskSnapshot.getStatus(), "Task status in history should remain unchanged");
    }

    @Test
    void testHistoryDepthLimit() {
        int historyDepth = 10;

        for (int i = 1; i <= historyDepth + 2; i++) {
            Task task = new Task((long) i, "Task " + i, "Description " + i, Status.NEW);
            historyManager.add(task);
        }

        assertEquals(historyDepth, historyManager.getHistory().size());

        assertEquals("Task 3", historyManager.getHistory().getFirst().getName(), "Oldest task should be removed");
        assertEquals("Task 12", historyManager.getHistory().getLast().getName(), "Latest task should be last in history");
    }

    @Test
    void testTaskDeletionDoesNotAffectHistory() {
        Task task = new Task("Task", "Description", Status.NEW);
        Long taskId = taskManager.create(task);

        historyManager.add(taskManager.getTask(taskId));

        taskManager.removeTask(taskId);

        assertEquals(1, historyManager.getHistory().size());
        assertEquals(taskId, historyManager.getHistory().getFirst().getId(), "Task should remain in history after removal from TaskManager");
    }

}