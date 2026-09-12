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
    private static final String FILE_PATH = Paths.get("data", "jude.txt").toString();

    private static void saveTasks(List<Task> tasks) {
        try {
            File file = new File(FILE_PATH);
            // Ensure the parent directory exists
            file.getParentFile().mkdirs();

            FileWriter fw = new FileWriter(file);
            for (Task task : tasks) {
                // You will need to implement toFileFormat() in your Task classes
                fw.write(task.toFileFormat() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Something went wrong while saving: " + e.getMessage());
        }
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
                    String[] parts = line.split(" \\| ");
                    String type = parts[0];
                    boolean isDone = parts[1].equals("1");
                    String description = parts[2];

                    Task task = null;

                    if (type.equals("T")) {
                        task = new Todo(description);
                    } else if (type.equals("D")) {
                        task = new Deadline(description, parts[3]);
                    } else if (type.equals("E")) {
                        // Assuming your event stores start and end times separately
                        String timeString = parts[3]; 
                        String[] times = timeString.split(" - ");
                        String start = times[0];
                        String end = times[1];
                        task = new Event(description, start, end);
                    }

                    if (task != null) {
                        if (isDone) {
                            task.markAsDone();
                        }
                        tasks.add(task);
                    }
                } catch (Exception e) {
                    // Skips corrupted lines (the Stretch Goal requirement)
                    System.out.println("Skipping corrupted data: " + line);
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    public static void main(String[] args) {
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
                if (action.equals("bye")) {
                    break;
                }

                // print tasks
                if (action.equals("list")) {
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
                }

                // unmark tasks
                else if (action.equals("unmark")) {
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
                }

                // mark tasks as done
                else if (action.equals("mark")) {
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
                }

                // delete task
                else if (action.equals("delete")) {
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
                }

                // todo, event, deadline tasks
                else if (action.equals("todo") || action.equals("deadline") || action.equals("event")) {

                    // check for empty task description
                    if (line.trim().equals(action)) {
                        throw new JudeException("OOPS!!! The description of a " + action + " cannot be empty.");
                    }

                    Task newTask = null;

                    // add todo items to tasks
                    if (action.equals("todo")) {
                        String description = line.substring("todo ".length());
                        newTask = new Todo(description);
                    }

                    // add deadline items to tasks
                    else if (action.equals("deadline")) {
                        String description = line.substring("deadline ".length(), line.indexOf(" /by"));
                        String by = line.substring(line.indexOf("/by ") + "/by ".length());
                        newTask = new Deadline(description, by);
                    }

                    else if (action.equals("event")) {
                        String description = line.substring("event ".length(), line.indexOf(" /from"));
                        String start = line.substring(line.indexOf("/from ") + "/from ".length(), line.indexOf(" /to"));
                        String end = line.substring(line.indexOf("/to ") + "/to ".length());
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
