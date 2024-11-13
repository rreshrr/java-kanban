package test;

import controllers.HistoryManager;
import controllers.TaskManager;
import org.junit.jupiter.api.Test;
import util.Managers;

import static org.junit.jupiter.api.Assertions.assertNotNull;


class ManagersTest {

    @Test
    void testGetDefaultTaskManager() {
        TaskManager taskManager = Managers.getDefault();

        assertNotNull(taskManager, "TaskManager instance should be initialized");
    }

    @Test
    void testGetDefaultHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(historyManager, "HistoryManager instance should be initialized");
    }
}