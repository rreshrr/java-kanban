package test;

import model.Epic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EpicTest {

    @Test
    void testEpicsEqualityById() {
        Epic epic1 = new Epic(1L, "Epic 1", "Description 1");
        Epic epic2 = new Epic(1L, "Epic 2", "Description 2");

        assertEquals(epic1, epic2, "Epics with the same id should be equal");
    }

    @Test
    void testEpicsInequalityById() {
        Epic epic1 = new Epic(1L, "Epic 1", "Description 1");
        Epic epic2 = new Epic(2L, "Epic 2", "Description 2");

        assertNotEquals(epic1, epic2, "Epics with different ids should not be equal");
    }

    @Test
    void testEpicCannotAddItselfAsSubtask() {
        Epic epic = new Epic(1L, "Epic", "Description");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> epic.addSubtask(1L),
                "Expected throw exception while trying add epic to itself as a subtask");
        exception.getMessage().equals("Epic cannot be own subtask");
    }

    @Test
    void testAddAndRemoveSubtasks() {
        Epic epic = new Epic(1L, "Epic", "Description");
        epic.addSubtask(2L);
        epic.addSubtask(3L);

        assertTrue(epic.getSubtasksIds().contains(2L), "Subtask 2 should be added to the epic");
        assertTrue(epic.getSubtasksIds().contains(3L), "Subtask 3 should be added to the epic");

        epic.removeSubtask(2L);
        assertFalse(epic.getSubtasksIds().contains(2L), "Subtask 2 should be removed from the epic");
    }
}