package org.itstep;

import java.io.*;
import java.util.*;



public class App {
    static Map<User, List<Task>> toDoList = new TreeMap<>();
    static User tmpUser = null;
    static boolean isPass = false;
    static boolean exit = false;

    static class RegisterUser implements Action {

        @Override
        public void doIt() {
            while (true) {
                System.out.println("Введите логин");
                Scanner scanner = new Scanner(System.in);
                String login = scanner.nextLine();
                if (toDoList.get(new User(login, "")) != null) {
                    System.out.println("Пользователь с таким именем уже зарегистрирован");
                    continue;
                }
                System.out.println("Введите пароль");
                String password = scanner.nextLine();
                toDoList.put(new User(login, password), new ArrayList<>());
                System.out.println("Пользователь успешно добавлен");
                break;

            }
        }
    }
    static class AuthUser implements Action {

        @Override
        public void doIt() {
            System.out.println("Введите логин");
            Scanner scanner = new Scanner(System.in);
            String login = scanner.nextLine();
            System.out.println("Введите пароль");
            String password = scanner.nextLine();
            for (User user : toDoList.keySet()) {
                if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                    tmpUser = user;
                    isPass = true;
                    System.out.println("Авторизация пройдена успешно");
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {

        String filename = "todolist.txt";
        File file = new File(filename);
        if (file.exists()) {
            try (ObjectInput input = new ObjectInputStream(new FileInputStream(filename))) {
                toDoList = (Map<User, List<Task>>) input.readObject();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        } else {
            toDoList = new TreeMap<>();
        }

        Menu mainMenu = new Menu("Main Menu");
        mainMenu.add(new Menu("Регистрация нового пользователя", new RegisterUser()));
        mainMenu.add(new Menu("Авторизация пользователя", new AuthUser()));
        mainMenu.add(new Menu("Выход", () -> exit = true));

        Menu listMenu = new Menu("ToDoList Menu");
        listMenu.add(new Menu("Отображение списка всех не выполненых задач (отсортированные по важности и дате)", () -> {

            toDoList.get(tmpUser).sort(Task::compareTo);
            System.out.println("Задачи: ");
            for (Task task : toDoList.get(tmpUser)) {
                if (!task.isDone()) {
                    System.out.println(task);
                }
            }
            System.out.println();

        }));
        listMenu.add(new Menu("Отображение списка всех выполненных задач (отсортированные по дате в обратном порядке)", () -> {
            System.out.println("Задачи: ");
                            toDoList.get(tmpUser).sort(Task::compareTo);
                            for (Task task : toDoList.get(tmpUser)) {
                                if (task.isDone()) {
                                    System.out.println(task);
                                }
                            }
                            System.out.println();
        }));

        listMenu.add(new Menu("Добавление новой задачи (класс Task)", () -> {
           Scanner scanner = new Scanner(System.in);
                            System.out.println("Введите описание задачи");
                            String description = scanner.nextLine();
                            System.out.println("Введите дату");
                            String date = scanner.nextLine();
                            System.out.println("Введите важность");
                            int priority = scanner.nextInt();
                            toDoList.get(tmpUser).add(new Task(description, priority, date, false));
        }));
        listMenu.add(new Menu("Отметка про выполнение задания", () -> {

            toDoList.get(tmpUser).sort(Task::compareTo);
            int count = 0;
            for (Task task : toDoList.get(tmpUser)) {
                if (!task.isDone()) {
                    count++;
                    System.out.println(toDoList.get(tmpUser).indexOf(task) + " " + task);
                }
            }
            if (count > 0) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Введите номер задачи, чтобы отметить ее как выполненную");
                toDoList.get(tmpUser).get(scanner.nextInt()).setDone(true);
            } else {
                System.out.println("Все задача выполнены");
            }

        }));

        listMenu.add(new Menu("Удаление задачи", () -> {

            toDoList.get(tmpUser).sort(Task::compareTo);
            for (Task task : toDoList.get(tmpUser)) {
                System.out.println(toDoList.get(tmpUser).indexOf(task) + " " + task);
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите номер задачи, которую нужно удалить");
            toDoList.get(tmpUser).remove(scanner.nextInt());

        }));

        listMenu.add(new Menu("Архивирование выполненных задач (после архивирования исчезают из списка)", () -> {

            FileWriter fw = null;
            StringBuilder line = new StringBuilder();
            for (Task task : toDoList.get(tmpUser)) {
                if (task.isDone()) {
                    line.append(task.getDescription()).append("///").append(task.getPriority()).append("///").append(task.getDate()).append("\n");
                }
            }
            try {
                fw = new FileWriter("archive.txt", true);
                fw.write(line.toString());
                toDoList.get(tmpUser).removeIf(Task::isDone);
                System.out.println("Выполненные задачи перенесены в архив");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                assert fw != null;
                fw.close();
            }

        }));

        listMenu.add(new Menu("Выход", () -> exit = true));

        while (!isPass) {
            if (exit) {
                break;
            }
            mainMenu.show();
        }
        while (!exit) {
            listMenu.show();
        }

        try (ObjectOutput output = new ObjectOutputStream(new FileOutputStream(filename))) {
            output.writeObject(toDoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
