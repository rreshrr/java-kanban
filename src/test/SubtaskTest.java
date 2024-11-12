package test;

import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubtaskTest {

    @Test
    void testSubtasksEqualityById() {
        Subtask subtask1 = new Subtask(1L, "Subtask 1", "Description 1", Status.NEW, 2L);
        Subtask subtask2 = new Subtask(1L, "Subtask 2", "Description 2", Status.DONE, 3L);

        assertEquals(subtask1, subtask2, "Subtasks with the same id should be equal");
    }

    @Test
    void testSubtasksInequalityById() {
        Subtask subtask1 = new Subtask(1L, "Subtask 1", "Description 1", Status.NEW, 2L);
        Subtask subtask2 = new Subtask(2L, "Subtask 2", "Description 2", Status.DONE, 3L);

        assertNotEquals(subtask1, subtask2, "Subtasks with different ids should not be equal");
    }

    @Test
    void testSubtaskCannotHaveItselfAsEpic() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Subtask(1L, "Subtask", "Description", Status.NEW, 1L),
                "Expected throw exception while trying create subtask with equal id and epicId");
        exception.getMessage().equals("Subtask cannot have same id as epic");

    }
}