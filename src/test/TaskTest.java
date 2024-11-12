package test;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskTest {

    @Test
    void testTasksEqualityById() {
        Task task1 = new Task(1L, "Task 1", "Description 1", Status.NEW);
        Task task2 = new Task(1L, "Task 2", "Description 2", Status.DONE);

        assertEquals(task1, task2, "Tasks with the same id should be equal");
    }

    @Test
    void testTasksInequalityById() {
        Task task1 = new Task(1L, "Task 1", "Description 1", Status.NEW);
        Task task2 = new Task(2L, "Task 2", "Description 2", Status.DONE);

        assertNotEquals(task1, task2, "Tasks with different ids should not be equal");
    }
}
