package test;

import controllers.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void testAddAndGetTaskById() {
        Task task = new Task("Task 1", "Description 1", Status.NEW);
        Long taskId = taskManager.create(task);

        assertNotNull(taskManager.getTask(taskId), "Task should be retrievable by ID");
    }

    @Test
    void testAddAndGetEpicAndSubtaskById() {
        Epic epic = new Epic("Epic 1", "Epic Description");
        Long epicId = taskManager.create(epic);

        Subtask subtask = new Subtask("Subtask 1", "Subtask Description", Status.NEW, epicId);
        Long subtaskId = taskManager.create(subtask);

        assertNotNull(taskManager.getEpic(epicId), "Epic should be retrievable by ID");
        assertNotNull(taskManager.getSubtask(subtaskId), "Subtask should be retrievable by ID");
        assertTrue(taskManager.getEpicSubtasks(epicId).contains(taskManager.getSubtask(subtaskId)),
                "Subtask should be associated with the correct Epic");
    }

    @Test
    void testNoIdConflictsBetweenManualAndGeneratedIds() {
        Task manualTask = new Task(1L, "Manual Task", "Description", Status.NEW);
        Long generatedId = taskManager.create(new Task("Generated Task", "Description", Status.NEW));

        taskManager.create(manualTask);

        assertNotEquals(generatedId, manualTask.getId(), "Generated and manual IDs should not conflict");
    }

    @Test
    void testTaskImmutabilityAfterAddition() {
        Task originalTask = new Task("Immutable Task", "Immutable Description", Status.NEW);
        Long taskId = taskManager.create(originalTask);

        Task storedTask = taskManager.getTask(taskId);

        assertEquals(originalTask.getName(), storedTask.getName(), "Task name should remain the same");
        assertEquals(originalTask.getDescription(), storedTask.getDescription(), "Task description should remain the same");
        assertEquals(originalTask.getStatus(), storedTask.getStatus(), "Task status should remain the same");
    }

    @Test
    void testEpicSubtasksManagement() {
        Epic epic = new Epic("Epic Task", "Epic Description");
        Long epicId = taskManager.create(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", Status.NEW, epicId);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", Status.IN_PROGRESS, epicId);
        Long subtaskId1 = taskManager.create(subtask1);
        Long subtaskId2 = taskManager.create(subtask2);

        List<Subtask> epicSubtasks = taskManager.getEpicSubtasks(epicId);
        assertEquals(2, epicSubtasks.size(), "Epic should contain two subtasks");
        assertTrue(epicSubtasks.stream().anyMatch(subtask -> subtask.getId().equals(subtaskId1)));
        assertTrue(epicSubtasks.stream().anyMatch(subtask -> subtask.getId().equals(subtaskId2)));

        taskManager.removeSubtask(subtaskId1);
        epicSubtasks = taskManager.getEpicSubtasks(epicId);
        assertEquals(1, epicSubtasks.size(), "Epic should contain one subtask after deletion");

        taskManager.clearSubtasks();
        epicSubtasks = taskManager.getEpicSubtasks(epicId);
        assertTrue(epicSubtasks.isEmpty(), "Epic should have no subtasks after clearing");
    }

    @Test
    void testGetAllTypesTasks() {
        Task task = new Task("Task", "Description", Status.NEW);
        Epic epic = new Epic("Epic", "Epic Description");

        taskManager.create(task);
        long epicId = taskManager.create(epic);

        Subtask subtask = new Subtask("Subtask", "Subtask Description", Status.DONE, epicId);
        taskManager.create(subtask);

        List<? extends Task> allTasks = taskManager.getAllTypesTasks();
        assertEquals(3, allTasks.size(), "All types of tasks should be returned");
        assertTrue(allTasks.contains(task));
        assertTrue(allTasks.contains(epic));
        assertTrue(allTasks.contains(subtask));
    }

}
