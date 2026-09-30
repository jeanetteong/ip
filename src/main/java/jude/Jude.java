package jude;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import jude.exception.JudeException;
import jude.task.Deadline;
import jude.task.Event;
import jude.task.Task;
import jude.task.Todo;

public class Jude {
    private static final String TODO_TYPE = "T";
    private static final String DEADLINE_TYPE = "D";
    private static final String EVENT_TYPE = "E";

    private static final String CMD_BYE = "bye";
    private static final String CMD_LIST = "list";
    private static final String CMD_UNMARK = "unmark";
    private static final String CMD_MARK = "mark";
    private static final String CMD_DELETE = "delete";
    private static final String CMD_TODO = "todo";
    private static final String CMD_DEADLINE = "deadline";
    private static final String CMD_EVENT = "event";

    private static final String BY_PREFIX_WITH_SPACE = " /by";
    private static final String BY_PREFIX = "/by ";
    private static final String FROM_PREFIX_WITH_SPACE = " /from";
    private static final String FROM_PREFIX = "/from ";
    private static final String TO_PREFIX_WITH_SPACE = " /to";
    private static final String TO_PREFIX = "/to ";

    private static final String FILE_DELIMITER = " \\| ";
    private static final String EVENT_TIME_DELIMITER = " - ";
    private static final String IS_DONE_FLAG = "1";

    private static final String FILE_PATH = Paths.get("data", "jude.txt").toString();

    private static void saveTasks(List<Task> tasks) {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();

            FileWriter fw = new FileWriter(file);
            for (Task task : tasks) {
                fw.write(task.toFileFormat() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Something went wrong while saving: " + e.getMessage());
        }
    }

    private static Task parseTaskFromString(String line) throws Exception {
        String[] parts = line.split(FILE_DELIMITER);
        String type = parts[0];
        boolean isDone = parts[1].equals(IS_DONE_FLAG);
        String description = parts[2];

        Task task = null;

        if (type.equals(TODO_TYPE)) {
            task = new Todo(description);
        } else if (type.equals(DEADLINE_TYPE)) {
            task = new Deadline(description, parts[3]);
        } else if (type.equals(EVENT_TYPE)) {
            String timeString = parts[3];
            String[] times = timeString.split(EVENT_TIME_DELIMITER);
            String start = times[0];
            String end = times[1];
            task = new Event(description, start, end);
        }

        if (task != null && isDone) {
            task.markAsDone();
        }

        return task;
    }

    private static void loadTasks(List<Task> tasks) {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return;
            }
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNext()) {
                String line = fileScanner.nextLine();
                try {
                    Task task = parseTaskFromString(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                } catch (Exception e) {
                    System.out.println("Skipping corrupted data: " + line);
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    public static void main(String[] args) {
        String banner = """
                     _           _
                    | |_   _  __| | ___
                 _  | | | | |/ _` |/ _ \\
                | |_| | |_| | (_| |  __/
                 \\___/ \\__,_|\\__,_|\\___|
                """;
        System.out.println(banner);
        String line;
        List<Task> tasks = new ArrayList<>();
        loadTasks(tasks);

        System.out.println("""
                    ____________________________________________________________
                    Hello! I'm Jude
                    What can I do for you?
                    ____________________________________________________________
                """);

        Scanner in = new Scanner(System.in);
        while (true) {
            line = in.nextLine();
            String action = line.split(" ")[0];

            try {
                // exit
                if (action.equals(CMD_BYE)) {
                    break;
                }

                // print tasks
                if (action.equals(CMD_LIST)) {
                    System.out.println("""
                                ____________________________________________________________
                                Here are the tasks in your list:
                            """);

                    if (tasks.isEmpty()) {
                        System.out.println("    <None>");
                        System.out.println("    ____________________________________________________________");
                    } else {
                        int count = 1;
                        for (Task task : tasks) {
                            System.out.printf("    %d. %s\n", count, task.toString());
                            count++;
                        }
                        System.out.println("    ____________________________________________________________");
                        continue;
                    }
                } else if (action.equals(CMD_UNMARK)) { // unmark tasks
                    int taskNo = Integer.parseInt(line.split(" ")[1]);

                    // check if task number exists
                    if (taskNo <= 0 || taskNo > tasks.size()) {
                        throw new JudeException("OOPS!!! Task number " + taskNo + " does not exist.");
                    }

                    Task selectedTask = tasks.get(taskNo - 1);
                    selectedTask.unMark();
                    saveTasks(tasks);
                    System.out.printf("""
                                ____________________________________________________________
                                OK, I've marked this task as not done yet:
                                    %s
                                ____________________________________________________________\n
                            """, selectedTask.toString());
                    continue;
                } else if (action.equals(CMD_MARK)) { // mark tasks as done
                    int taskNo = Integer.parseInt(line.split(" ")[1]);

                    if (taskNo <= 0 || taskNo > tasks.size()) {
                        throw new JudeException("OOPS!!! Task number " + taskNo + " does not exist.");
                    }

                    Task selectedTask = tasks.get(taskNo - 1);
                    selectedTask.markAsDone();
                    saveTasks(tasks);
                    System.out.printf("""
                                ____________________________________________________________
                                Nice! I've marked this task as done:
                                    %s
                                ____________________________________________________________\n
                            """, selectedTask.toString());
                } else if (action.equals(CMD_DELETE)) { // delete task
                    int taskNo = Integer.parseInt(line.split(" ")[1]);

                    if (taskNo <= 0 || taskNo > tasks.size()) {
                        throw new JudeException("OOPS!!! Task number " + taskNo + " does not exist.");
                    }

                    Task selectedTask = tasks.get(taskNo - 1);
                    tasks.remove(selectedTask);
                    saveTasks(tasks);
                    System.out.printf("""
                                ____________________________________________________________
                                Noted. I've removed this task:
                                    %s
                                Now you have %d tasks in the list.
                                ____________________________________________________________\n
                            """, selectedTask.toString(), tasks.size());
                } else if (action.equals(CMD_TODO) || action.equals(CMD_DEADLINE) || action.equals(CMD_EVENT)) { // todo, event, deadline tasks
                    // check for empty task description
                    if (line.trim().equals(action)) {
                        throw new JudeException("OOPS!!! The description of a " + action + " cannot be empty.");
                    }

                    Task newTask = null;

                    // add todo items to tasks
                    if (action.equals(CMD_TODO)) {
                        String description = line.substring("todo ".length());
                        newTask = new Todo(description);
                    } else if (action.equals(CMD_DEADLINE)) { // add deadline items to tasks
                        String description = line.substring("deadline ".length(), line.indexOf(BY_PREFIX_WITH_SPACE));
                        String by = line.substring(line.indexOf(BY_PREFIX) + BY_PREFIX.length());
                        newTask = new Deadline(description, by);
                    } else if (action.equals(CMD_EVENT)) {
                        String description = line.substring("event ".length(), line.indexOf(FROM_PREFIX_WITH_SPACE));
                        String start = line.substring(line.indexOf(FROM_PREFIX) + FROM_PREFIX.length(),
                                line.indexOf(TO_PREFIX_WITH_SPACE));
                        String end = line.substring(line.indexOf(TO_PREFIX) + TO_PREFIX.length());
                        newTask = new Event(description, start, end);
                    }

                    if (newTask != null) {
                        tasks.add(newTask);
                        saveTasks(tasks);
                        System.out.printf("""
                                    ____________________________________________________________
                                    Got it. I've added this task:
                                        %s
                                    Now you have %d tasks in the list.
                                    ____________________________________________________________\n
                                """, newTask.toString(), tasks.size());
                    }
                } else {
                    // invalid task
                    throw new JudeException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (JudeException e) {
                System.out.printf("""
                            ____________________________________________________________
                            %s
                            ____________________________________________________________\n
                        """, e.getMessage());

            } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {
                // incomplete or unformatted command
                System.out.printf("""
                            ____________________________________________________________
                            %s
                            ____________________________________________________________\n
                        """, "OOPS!!! Your command is incomplete or formatted incorrectly.");

            } catch (NumberFormatException e) {
                // typing word instead of number for mark/undone task
                System.out.printf("""
                            ____________________________________________________________
                            %s
                            ____________________________________________________________\n
                        """, "OOPS!!! Please provide a valid task number.");
            }
        }

        in.close();
        System.out.println("""
                    ____________________________________________________________
                    Bye. Hope to see you again soon!
                    ____________________________________________________________

                """);
    }
}
