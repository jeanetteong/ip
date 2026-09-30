package jude;

import jude.exception.JudeException;

public class Parser {
    private static final String BY_PREFIX_WITH_SPACE = " /by";
    private static final String BY_PREFIX = "/by ";
    private static final String FROM_PREFIX_WITH_SPACE = " /from";
    private static final String FROM_PREFIX = "/from ";
    private static final String TO_PREFIX_WITH_SPACE = " /to";
    private static final String TO_PREFIX = "/to ";

    public static String getAction(String command) {
        return command.split(" ")[0];
    }

    public static int getTaskNumber(String command) {
        return Integer.parseInt(command.split(" ")[1]);
    }

    public static void checkEmptyDescription(String command, String action) throws JudeException {
        if (command.trim().equals(action)) {
            throw new JudeException("OOPS!!! The description of a " + action + " cannot be empty.");
        }
    }

    public static String parseTodoDescription(String command) {
        return command.substring("todo ".length());
    }

    public static String[] parseDeadlineDetails(String command) {
        String description = command.substring("deadline ".length(), command.indexOf(BY_PREFIX_WITH_SPACE));
        String by = command.substring(command.indexOf(BY_PREFIX) + BY_PREFIX.length());
        return new String[]{description, by};
    }

    public static String[] parseEventDetails(String command) {
        String description = command.substring("event ".length(), command.indexOf(FROM_PREFIX_WITH_SPACE));
        String start = command.substring(command.indexOf(FROM_PREFIX) + FROM_PREFIX.length(), command.indexOf(TO_PREFIX_WITH_SPACE));
        String end = command.substring(command.indexOf(TO_PREFIX) + TO_PREFIX.length());
        return new String[]{description, start, end};
    }

    public static String parseFindKeyword(String command) throws JudeException {
        if (command.trim().equals("find")) {
            throw new JudeException("OOPS!!! The search keyword cannot be empty.");
        }
        return command.substring("find ".length());
    }
}