import controllers.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import util.Managers;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        System.out.println("Поехали!");

        // Создание 2х задач
        Task throwGarbage = new Task(
                "Выкинуть мусор",
                "Нужно выйти из дома, дойти до мусорного контейнера и выкинуть мусор",
                Status.NEW
        );
        Task buyMilk = new Task(
                "Купить молока",
                "Нужно взять деньги, выйти из дома в магазин, купить молока с жирностью 3%",
                Status.NEW
        );
        taskManager.create(throwGarbage);
        Long buyMilkId = taskManager.create(buyMilk);

        // Создание эпика с 2мя подзадачами
        Epic makeTea = new Epic(
                "Сделать чай",
                "Сложная задача в 2 шага - сделать чай"
        );
        taskManager.create(makeTea);
        Subtask makeTeaFirst = new Subtask(
                "Заварить чай",
                "Найти чайник, налить в него воды, довести до кипения",
                Status.NEW,
                makeTea.getId()
        );
        Subtask makeTeaSecond = new Subtask(
                "Налить чай в стакан",
                "Найти кружку, добавить заварки, влить в неё кипяток",
                Status.NEW,
                makeTea.getId()
        );
        Long makeTeaFirstId = taskManager.create(makeTeaFirst);
        taskManager.create(makeTeaSecond);

        //Создание эпика с 1й подзадачей
        Epic preheatCheburek = new Epic(
                "Разогреть чебурек",
                "Эпик по приготовлению чебуреков"
        );
        Long preheatCheburekId = taskManager.create(preheatCheburek);

        Subtask preheatCheburekFirst = new Subtask(
                "Разогревание чебурека, акт 1, действие 1",
                "Поставить чебурек в микроволновку на мощность 800W на 3-5 минут",
                Status.NEW,
                preheatCheburek.getId()
        );
        taskManager.create(preheatCheburekFirst);

        //Вывод
        printAllTasks(taskManager);

        //Изменение статуса отдельно стоящей задачи
        Task buyMilkUpdateStatus = new Task(
                buyMilkId,
                buyMilk.getName(),
                buyMilk.getDescription(),
                Status.IN_PROGRESS
        );
        taskManager.update(buyMilkUpdateStatus);

        // Изменение статуса подзадачи эпика
        Subtask makeTeaFirstUpdateStatus = new Subtask(
                makeTeaFirstId,
                makeTeaFirst.getName(),
                makeTeaFirst.getDescription(),
                Status.IN_PROGRESS,
                makeTea.getId()
        );
        taskManager.update(makeTeaFirstUpdateStatus);

        //Вывод изменений
        System.out.println("**изменение статусов**");
        System.out.println("Обычная задача:");
        System.out.println(taskManager.getTask(buyMilkId));

        System.out.println("Подзадача:");
        System.out.println(taskManager.getSubtask(makeTeaFirstId));

        System.out.println("Эпик подзадачи:");
        System.out.println(taskManager.getEpic(makeTea.getId()));

        // Удаление задачи
        taskManager.removeTask(buyMilkId);

        // Удаление подзадачи эпика
        taskManager.removeSubtask(makeTeaFirstId);

        // Удаление эпика
        taskManager.removeEpic(preheatCheburekId);

        //Вывод
        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);
            for (Task task : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
