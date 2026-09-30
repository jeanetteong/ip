package jude;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import jude.task.Deadline;
import jude.task.Event;
import jude.task.Task;
import jude.task.Todo;

public class Storage {
    private String filePath;

    private static final String TODO_TYPE = "T";
    private static final String DEADLINE_TYPE = "D";
    private static final String EVENT_TYPE = "E";
    private static final String FILE_DELIMITER = " \\| ";
    private static final String EVENT_TIME_DELIMITER = " - ";
    private static final String IS_DONE_FLAG = "1";

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return tasks;
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
        return tasks;
    }

    public void save(TaskList taskList) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();

            FileWriter fw = new FileWriter(file);
            for (int i = 0; i < taskList.size(); i++) {
                Task task = taskList.get(i);
                fw.write(task.toFileFormat() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Something went wrong while saving: " + e.getMessage());
        }
    }

    private Task parseTaskFromString(String line) throws Exception {
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
}