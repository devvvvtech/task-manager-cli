package com.devvvvtech.taskmanager;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class TaskManager {
    private static ArrayList<Task> taskList = new ArrayList<>();
    private static int taskIdCounter = 1;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        loadTasksFromFile();

        while(running) {
            System.out.println("\n==== Task Manager ====");
            System.out.println("1. Add Task");
            System.out.println("2. View Task");
            System.out.println("3. Delete Task by ID");
            System.out.println("4. Mark Task as Completed");
            System.out.println("5. Update Task by ID");
            System.out.println("6. Sort Task");
            System.out.println("0. Exit");
            System.out.println("Choose an option");

            int choice = sc.nextInt();
            sc.nextLine(); //take leftover newline

            switch (choice) {
                case(1):
                    addTask(sc);
                    break;
                case(2):
                    viewTask();
                    break;
                case(3):
                    deleteTaskByID(sc);
                    break;
                case(4):
                    markTaskCompleted(sc);
                    break;
                case(5):
                    updateTaskByID(sc);
                    break;
                case(6):
                    sortTasks(sc);
                    break;
                case(0):
                    running = false;
                    saveTaskToFile();
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
        sc.close();
    }

    //adding task
    private static void addTask(Scanner sc) {
        System.out.println("Enter title: ");
        String title = sc.nextLine();

        System.out.println("Enter description: ");
        String desc = sc.nextLine();

        System.out.println("Enter due date (yyyy-mm-dd): ");
        String dateInput = sc.nextLine();
        LocalDate dueDate = LocalDate.parse(dateInput);

        Task newTask = new Task(taskIdCounter++, title, desc, dueDate, TaskStatus.PENDING);
        taskList.add(newTask);
        System.out.println("Task added successfully! ✅");
    }

    //viewing task
    private static void viewTask() {
        if(taskList.isEmpty()) {
            System.out.println("No tasks available");
            return;
        }
        System.out.println("\nAll Tasks: ");
        for(Task task : taskList) {
            System.out.println(task);
        }
    }

    //deleting task
    private static void deleteTaskByID(Scanner sc){
        System.out.println("Enter the ID of task to delete: ");
        int id = sc.nextInt();

        Iterator<Task> iterator = taskList.iterator();
        while(iterator.hasNext()) {
            if(iterator.next().getId() == id) {
                iterator.remove();
                System.out.println("Task Deleted");
                return;
            }
        }
        System.out.println("Task not found");
    }

    //marking task as complete
    public static void markTaskCompleted(Scanner sc){
        System.out.println("Enter ID of task to mark as completed: ");
        int id = sc.nextInt();

        for(Task task : taskList) {
            if(task.getId() == id){
                if(task.getStatus().equals(TaskStatus.COMPLETED)){
                    System.out.println("Task is already completed ✅");
                    return;
                }else {
                    task.setStatus(TaskStatus.COMPLETED);
                    System.out.println("Status changed successfully ✅");
                    return;
                }
            }
        }
        System.out.println("Task not found");
    }

    //updating task
    public static void updateTaskByID(Scanner sc) {
        System.out.println("Enter ID of task to update: ");
        int id = sc.nextInt();

        for(Task task : taskList) {
            if(task.getId() == id) {
                boolean isRunning = true;
                while(isRunning) {
                    System.out.println("\nChoose what to update");
                    System.out.println("1. Title");
                    System.out.println("2. Description");
                    System.out.println("3. Due date");
                    System.out.println("0. Exit");
                    int option = sc.nextInt();
                    sc.nextLine();
                    switch (option) {
                        case 1:
                            System.out.println("Enter the title: ");
                            String title = sc.nextLine();
                            task.setTitle(title);
                            System.out.println("Task updated successfully ✅");
                            break;
                        case 2:
                            System.out.println("Enter the description: ");
                            String desc = sc.nextLine();
                            task.setDescription(desc);
                            System.out.println("Task updated successfully ✅");
                            break;
                        case 3:
                            System.out.println("Enter the due date (yyyy-mm-dd): ");
                            String dateInput = sc.nextLine();
                            try{
                                LocalDate date = LocalDate.parse(dateInput);
                                task.setDueDate(date);
                            } catch (DateTimeParseException e) {
                                System.out.println("Invalid date Format. Use yyyy-mm-dd.");
                                break;
                            }
                            System.out.println("Task updated successfully ✅");
                            break;
                        case 0:
                            isRunning = false;
                            break;
                        default:
                            System.out.println("Enter valid option!!");
                    }
                }
                System.out.println("\uD83D\uDD01Updated task: " + task);
                return;
            }
        }
        System.out.println("Task not found!!");
    }

    //sorting task
    public static void sortTasks(Scanner sc) {
        System.out.println("1. Sort by Due Date (Earliest First)");
        System.out.println("2. Sort by Status (PENDING first, then COMPLETED");
        System.out.println("Choose one option");
        try{
            int choice = sc.nextInt();
            if(choice == 1) {
                sortByDueDate();
            } else if(choice == 2) {
                sortByStatus();
            }
        }catch (InputMismatchException e) {
            System.out.println("Invalid option");
        }
    }
    //sorting task by due date
    public static void sortByDueDate() {
        taskList.sort(new Comparator<Task>() {
            @Override
            public int compare(Task a, Task b) {
                return a.getDueDate().compareTo(b.getDueDate());
            }
        });
        System.out.println("Task sorted successfully.✅");
        System.out.println("Here are your tasks");
        viewTask();
    }
    //sorting task by status
    public static void sortByStatus() {
        taskList.sort(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.getStatus().compareTo(o2.getStatus());
            }
        });
        System.out.println("Task sorted successfully.✅");
        System.out.println("Here are your tasks");
        viewTask();
    }
    //saving task to the file
    public static void saveTaskToFile() {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("task.txt"))) {
            for(Task task : taskList) {
                String line = task.getId() + "," +
                        task.getTitle() + "," +
                        task.getDescription() + "," +
                        task.getDueDate() + "," +
                        task.getStatus();

                writer.write(line);
                writer.newLine();
            }
            System.out.println("📁 Tasks saved to file successfully!");
        } catch(IOException e) {
            System.out.println("❌ Error saving tasks: " + e.getMessage());
        }
    }

    //load task from the file
    public static void loadTasksFromFile() {
        File file = new File("tasks.txt");
        if(!file.exists()) {
            return; //No file yet -> skip loading
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 5);
                int id = Integer.parseInt(parts[0]);
                String title = parts[1];
                String description = parts[2];
                LocalDate dueDate = LocalDate.parse(parts[3]);
                TaskStatus status = TaskStatus.valueOf(parts[4]);

                Task task = new Task(id, title, description, dueDate, status);
                taskList.add(task);
            }

        }catch (Exception e) {
            System.out.println("❌ Error loading tasks: " + e.getMessage());
        }

    }
}
