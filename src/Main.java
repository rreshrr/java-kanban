public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
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
        taskManager.createTask(throwGarbage);
        taskManager.createTask(buyMilk);

        // Создание эпика с 2мя подзадачами
        Epic makeTea = new Epic(
                "Сделать чай",
                "Сложная задача в 2 шага - сделать чай"
                );
        Subtask makeTeaFirst = new Subtask(
                "Заварить чай",
                "Найти чайник, налить в него воды, довести до кипения",
                Status.NEW,
                makeTea
        );
        Subtask makeTeaSecond = new Subtask(
                "Налить чай в стакан",
                "Найти кружку, добавить заварки, влить в неё кипяток",
                Status.NEW,
                makeTea
        );
        taskManager.createTask(makeTeaFirst);
        taskManager.createTask(makeTeaSecond);
        taskManager.createTask(makeTea);

        //Создание эпика с 1й подзадачей
        Epic preheatCheburek = new Epic(
                "Разогреть чебурек",
                "Эпик по приготовлению чебуреков"
        );

        Subtask preheatCheburekFirst = new Subtask(
                "Разогревание чебурека, акт 1, действие 1",
                "Поставить чебурек в микроволновку на мощность 800W на 3-5 минут",
                Status.NEW,
                preheatCheburek
        );
        taskManager.createTask(preheatCheburek);
        taskManager.createTask(preheatCheburekFirst);

        //Вывод
        System.out.println("Задачи всех типов:");
        System.out.println(taskManager.getAllTypesTasks());

        System.out.println("Обычные задачи:");
        System.out.println(taskManager.getCommonTasks());

        System.out.println("Эпики:");
        System.out.println(taskManager.getEpics());

        System.out.println("Подзадачки:");
        System.out.println(taskManager.getSubtasks());

        //Изменение статуса отдельно стоящей задачи
        Task buyMilkUpdateStatus = new Task(
                buyMilk.getName(),
                buyMilk.getDescription(),
                Status.IN_PROGRESS
        );
        //taskManager.updateTask();
        Subtask makeTeaFirstUpdateStatus = new Subtask(
                makeTeaFirst.getName(),
                makeTeaFirst.getDescription(),
                Status.IN_PROGRESS,
                makeTea
        );
    }
}
