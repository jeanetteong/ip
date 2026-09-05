import java.util.Arrays;
import java.util.Scanner;

public class Jude {
    public static void main(String[] args) {
        String line;
        Task[] tasks = new Task[] {};
        System.out.println("""
                    ____________________________________________________________
                    Hello! I'm Jude
                    What can I do for you?
                    ____________________________________________________________
                """);

        while (true) {
            Scanner in = new Scanner(System.in);
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

                    if (tasks.length == 0) {
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
                    Task selectedTask = tasks[taskNo - 1];
                    selectedTask.unMark();
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
                    Task selectedTask = tasks[taskNo - 1];
                    selectedTask.markAsDone();
                    System.out.printf("""
                                ____________________________________________________________
                                Nice! I've marked this task as done:
                                    %s
                                ____________________________________________________________\n
                            """, selectedTask.toString());
                }

                // todo, event, deadline tasks
                else if (action.equals("todo") || action.equals("deadline") || action.equals("event")) {

                    // check for empty task description
                    if (line.trim().equals(action)) {
                        throw new JudeException("OOPS!!! The description of a " + action + " cannot be empty.");
                    }

                    tasks = Arrays.copyOf(tasks, tasks.length + 1);

                    // add todo items to tasks
                    if (action.equals("todo")) {
                        String description = line.substring("todo ".length());
                        tasks[tasks.length - 1] = new Todo(description);
                    }

                    // add deadline items to tasks
                    else if (action.equals("deadline")) {
                        String description = line.substring("deadline ".length(), line.indexOf(" /by"));
                        String by = line.substring(line.indexOf("/by ") + "/by ".length());
                        tasks[tasks.length - 1] = new Deadline(description, by);
                    }

                    else if (action.equals("event")) {
                        String description = line.substring("event ".length(), line.indexOf(" /from"));
                        String start = line.substring(line.indexOf("/from ") + "/from ".length(), line.indexOf(" /to"));
                        String end = line.substring(line.indexOf("/to ") + "/to ".length());
                        tasks[tasks.length - 1] = new Event(description, start, end);
                    }

                    System.out.printf("""
                                ____________________________________________________________
                                Got it. I've added this task:
                                    %s
                                Now you have %d tasks in the list.
                                ____________________________________________________________\n
                            """, tasks[tasks.length - 1].toString(), tasks.length);

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

        System.out.println("""
                    ____________________________________________________________
                    Bye. Hope to see you again soon!
                    ____________________________________________________________

                """);
    }
}
