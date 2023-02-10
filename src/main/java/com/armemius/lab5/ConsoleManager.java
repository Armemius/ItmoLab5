package com.armemius.lab5;

import com.armemius.lab5.collection.CollectionManager;
import com.armemius.lab5.collection.data.*;
import com.armemius.lab5.collection.exceptions.CollectionFileException;
import com.armemius.lab5.commands.CommandContext;
import com.armemius.lab5.commands.CommandParser;
import com.armemius.lab5.commands.exceptions.CommandArgumentException;
import com.armemius.lab5.commands.exceptions.CommandRuntimeException;
import com.armemius.lab5.commands.nodes.Node;
import com.armemius.lab5.io.InputHandler;
import com.armemius.lab5.io.OutputHandler;
import org.javatuples.Pair;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Singleton class responsible for interactions between user and computer
 * Generates commands tree and then starts console manager that
 * handles all the interactions between user and machine
 * @author Stepanov Arseniy P3109<br>
 */
public class ConsoleManager {
    private static boolean isRunning = false;
    private static CommandParser parser = null;


    private ConsoleManager() {}

    /**
     * Starts the console manager, to start it you
     * need to specify following parameters:
     * @param inputHandler Class that handles logic for input, read more {@link InputHandler}
     * @param outputHandler Class that handles logic for output, read more {@link OutputHandler}
     * @param parser Class that describes how the parsing will be done, read more {@link CommandParser}
     *
     * Console manager has only one instance, that means you need to stop
     * the manager to run it with new parameters
     */
    public static void start(InputHandler inputHandler, OutputHandler outputHandler, CommandParser parser) {
        if (isRunning)
            return;
        ConsoleManager.parser = parser;
        isRunning = true;
        while (isRunning) {
            outputHandler.hold("$ ");
            ConsoleManager.parser.parse(inputHandler.get());
        }
    }

    /**
     * Stops running console manager instance
     */
    public static void stop() {
        if (!isRunning)
            return;
        isRunning = false;
    }

    /**
     * Class that contains action logic for commands
     * and auxiliary functions for them
     * For better understanding about how commands
     * work you might want to read about:
     * @see Node#executes(Node.Task)
     * @see Node#run(CommandContext)
     * @see com.armemius.lab5.commands.nodes.Node.Task
     * @see CommandContext
     */
    public static class Actions {
        /**
         * Action for <b>help</b> command
         * Doesn't receive arguments
         * @param context
         */
        public static void showHelp(CommandContext context) {
            OutputHandler output = context.outputHandler();
            if (context.params().contains("h")) {
                output.put("""
                        Syntax:
                        > help
                        This command shows the list of all commands
                        PARAMS:
                        -h / --help\t\tShow this menu
                        
                        Cute cat 4 U
                         /\\_/\\\s
                        ( o.o )
                         > ^ <\s
                        """);
                return;
            }
            // Commands for task
            output.put("""
                    List of all commands:
                    help -- Outputs the list of all the commands with some info (you can use -h or --help parameter to gain more info about specific command)
                    info -- Show information about collection
                    show -- Output all the collection's elements into console
                    insert -- Inserts new element with specified key
                    update <id> -- Updates an element with specified key
                    remove <value> -- Removes an element with specified value (-h or --help for more information)
                    clear -- Clears the collection
                    save -- Save collection to file
                    execute <filename> -- Executes script in specified filename
                    exit -- Stops execution of the program
                    replace <id> -- Replaces an element with specified id with a new value if condition met
                    count <value> [delta] -- Counts the number of elements where condition met
                    filter <value> -- Outputs the elements where specified value is substring of the 'name' field in collection's elements""");
            // Auxiliary commands
            output.put("getenv -- Outputs the value of 'LAB_5_PATH'");
        }

        /**
         * Action for <b>info</b> command
         * Doesn't receive arguments
         * @param context
         */
        public static void showInfo(CommandContext context) {
            OutputHandler output = context.outputHandler();
            if (context.params().contains("h")) {
                output.put("""
                        Syntax:
                        > info
                        This command outputs info about collection
                        PARAMS:
                        -h / --help\t\tShow this menu
                        """);
                return;
            }
            output.put("Collection statistics:");
            output.put("Init time:\t" + CollectionManager.getCreationTime().format(DateTimeFormatter.ISO_LOCAL_TIME));
            output.put("Elements:\t" + CollectionManager.getElementsCount());
            output.put("Type:\t\t" + CollectionManager.getCollectionType());
        }

        /**
         * Action for <b>show</b> command
         * Doesn't receive arguments
         * @param context
         */
        public static void showElements(CommandContext context) {
            OutputHandler output = context.outputHandler();
            if (context.params().contains("h") ) {
                output.put("""
                        Syntax:
                        > show
                        Shows elements in collection
                        PARAMS:
                        -h / --help\t\tShow this menu
                        """);
                return;
            }
            var groups = CollectionManager.getAll();
            if (groups.isEmpty()) {
                output.put("Collection is empty");
                return;
            }
            output.put("Collection elements:");
            for (var it : groups) {
                output.put(it.toString());
            }
        }

        /**
         * Action for <b>insert</b> command
         * Doesn't receive arguments
         * @param context
         */
        public static void insertElement(CommandContext context) {
            OutputHandler outputHandler = context.outputHandler();
            InputHandler inputHandler = context.inputHandler();
            if (context.params().contains("h")) {
                outputHandler.put("""
                        Syntax:
                        > insert
                        Insert command stands for adding new elements inside collection
                        After the command is received process of building new StudyGroup begins, where you should input valid field values for the new group
                        PARAMS:
                        -h / --help\t\tShow this menu
                        -r / --random\tSkip the process of creating group and instead generate it randomly
                        """);
                return;
            }
            if (context.params().contains("r")) {
                outputHandler.put("Inserting random element");
                CollectionManager.add(new StudyGroup(CollectionManager.genId(),
                        "GROUP_NAME" + (int)(Math.random() * 1000),
                        new Coordinates((int)(Math.random() * 200000 - 100000), (long)(Math.random() * 10215 - 215)),
                        (long)(Math.random() * 20 + 10),
                        (int)(Math.random() * 5 + 1),
                        Math.random() * 3 + 2,
                        switch ((int)(Math.random() * 5)) {
                            case 0 -> Semester.SECOND;
                            case 1 -> Semester.THIRD;
                            case 2 -> Semester.SEVENTH;
                            case 3 -> Semester.EIGHTH;
                            default -> null;
                        },
                        new Person(
                                "ADMIN_NAME" + (int)(Math.random() * 1000),
                                (float)(Math.random() * 50 + 150),
                                switch ((int)(Math.random() * 3)) {
                                    case 0 -> EyeColor.GREEN;
                                    case 1 -> EyeColor.WHITE;
                                    default -> EyeColor.YELLOW;
                                },
                                switch ((int)(Math.random() * 5)) {
                                    case 0 -> HairColor.BLACK;
                                    case 1 -> HairColor.GREEN;
                                    case 2 -> HairColor.ORANGE;
                                    case 3 -> HairColor.RED;
                                    default -> HairColor.WHITE;
                                },
                                switch ((int)(Math.random() * 4)) {
                                    case 0 -> Country.CHINA;
                                    case 1 -> Country.SOUTH_KOREA;
                                    case 2 -> Country.UNITED_KINGDOM;
                                    default -> Country.VATICAN;
                                },
                                new Location(
                                        (long)(Math.random() * 200000 - 10000),
                                        Math.random() * 200000 - 10000,
                                        (long)(Math.random() * 200000 - 10000)
                                )
                        )
                ));
                return;
            }
            outputHandler.put("Inserting new element");
            StudyGroup group = null;
            while (group == null) {
                group = requestGroup(inputHandler, outputHandler);
                outputHandler.put("You want to add group: " + group);
                outputHandler.hold("Proceed? (Input empty line if yes) ");
                String response = inputHandler.get();
                if (!response.isBlank())
                    group = null;
            }
            CollectionManager.add(group);
        }

        /**
         * Action for <b>update</b> command
         * Receives one argument
         * @param context
         */
        public static void updateElement(CommandContext context) {
            OutputHandler outputHandler = context.outputHandler();
            InputHandler inputHandler = context.inputHandler();
            if (context.params().contains("h")) {
                outputHandler.put("""
                        Syntax:
                        > update <id>
                        Command responsive for updating collection
                        PARAMS:
                        -h / --help\tShow this menu
                        """);
                return;
            }
            int id;
            try {
                id = Integer.parseInt(context.args().get(0));
            }
            catch (NumberFormatException ex) {
                throw new CommandRuntimeException("Group id is not an Integer");
            }
            if (!CollectionManager.checkId(id))
                throw new CommandRuntimeException("Collection manager doesn't have element with such id");
            outputHandler.put("Updating element with id " + id);
            StudyGroup group = null;
            while (group == null) {
                group = requestGroup(inputHandler, outputHandler);
                group.setId(id);
                outputHandler.put("You want to update group with id " + id + " with the following group: " + group);
                outputHandler.hold("Proceed? (Input empty line if yes) ");
                String response = inputHandler.get();
                if (!response.isBlank())
                    group = null;
            }
            CollectionManager.update(id, group);
        }

        /**
         * Action for <b>remove</b> command
         * Receives up to one argument
         * @param context
         */
        public static void removeElement(CommandContext context) {
            OutputHandler outputHandler = context.outputHandler();
            InputHandler inputHandler = context.inputHandler();
            if (context.params().contains("h")) {
                outputHandler.put("""
                        Syntax:
                        > remove <value>
                        Command responsive for removal of elements from collection
                        PARAMS:
                        -h / --help\tShow this menu
                        --lower\t\tRemoves elements with value lower than specified (default - id)
                        --greater\tRemoves elements with value lower than specified (default - id)
                        --students\tMakes --lower and --greater parameters work with studentsCount field
                        --expelled\tMakes --lower and --greater parameters work with expelledStudents field
                        --avg-mark\tMakes --lower and --greater parameters work with averageMark field
                        --admin\t\tRemoves one group from collection where groupAdmin matches specified one
                        """);
                return;
            }
            if (context.params().contains("d")) {
                if (context.args().size() > 0)
                    throw new CommandArgumentException("Too much arguments");
                Person admin = requestAdmin(inputHandler, outputHandler);
                if (CollectionManager.removeAnyByGroupAdmin(admin)) {
                    outputHandler.put("Successfully removed element");
                } else {
                    outputHandler.put("Unable to find element with matching group admin");
                }
                return;
            }
            if (context.args().size() < 1)
                throw new CommandArgumentException("Argument wasn't provided");
            try {
                String fieldName = "getId";
                Class type = Integer.class;;
                if ((context.params().contains("s"))) {
                    fieldName = "getStudentsCount";
                    type = Long.class;
                } else if ((context.params().contains("e"))) {
                    fieldName = "getExpelledStudents";
                } else if ((context.params().contains("a"))) {
                    fieldName = "getAverageMark";
                    type = Double.class;
                }
                int removals;
                if (context.params().contains("g")) {
                    removals = removeGroups(type, fieldName, context.args().get(0), 1);
                } else if (context.params().contains("l")) {
                    removals = removeGroups(type, fieldName, context.args().get(0), -1);
                } else {
                    removals = removeGroups(type, fieldName, context.args().get(0), 0);
                }
                outputHandler.put("Removed " + removals + " element(s)");
            }
            catch (NumberFormatException ex) {
                throw new CommandRuntimeException("Incorrect value type provided");
            }
        }

        /**
         * Action for <b>clear</b> command
         * Doesn't receive arguments
         * @param context
         */
        public static void clearCollection(CommandContext context) {
            OutputHandler outputHandler = context.outputHandler();
            InputHandler inputHandler = context.inputHandler();
            if (context.params().contains("h")) {
                outputHandler.put("""
                        Syntax:
                        > clear
                        Command responsive for clearing the collection
                        PARAMS:
                        -h / --help\tShow this menu
                        -f / --force\tSkip all confirmation steps
                        """);
                return;
            }
            if (!context.params().contains("f")) {
                outputHandler.hold("Are you sure, all unsaved data will be lost? (Empty input if yes) ");
                String response = inputHandler.get();
                if (!response.isBlank()) {
                    outputHandler.put("Operation aborted");
                    return;
                }
            }
            outputHandler.put("Clearing the collection");
            CollectionManager.clear();
        }

        /**
         * Action for <b>save</b> command
         * Doesn't receive arguments
         * @param context
         */
        public static void saveCollection(CommandContext context) {
            OutputHandler outputHandler = context.outputHandler();
            if (context.params().contains("h")) {
                outputHandler.put("""
                        Syntax:
                        > save
                        Command responsive for saving collection
                        PARAMS:
                        -h / --help\tShow this menu
                        """);
                return;
            }
            try {
                outputHandler.put("Saving the collection");
                CollectionManager.save();
                outputHandler.put("Done");
            } catch (CollectionFileException ex) {
                throw new CommandRuntimeException(ex.getMessage());
            }

        }

        /**
         * Action for <b>execute</b> command
         * Receives one argument
         * @param context
         */
        public static void executeScript(CommandContext context) {
            OutputHandler outputHandler = context.outputHandler();
            if (context.params().contains("h")) {
                outputHandler.put("""
                        Syntax:
                        > execute <filename>
                        Command responsive for executing scripts from files
                        PARAMS:
                        -h / --help\tShow this menu
                        """);
                return;
            }
            if (context.args().size() < 1)
                throw new CommandArgumentException("Argument wasn't provided");
            outputHandler.put("Executing script");
            String scriptPath = context.args().get(0);

            try (Scanner scanner = new Scanner(new File(scriptPath))) {
                while (scanner.hasNextLine()) {
                    parser.parse(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error while reading the file");
            }
        }

        /**
         * Action for <b>exit</b> command
         * Doesn't receive arguments
         * @param context
         */
        public static void exitProgram(CommandContext context) {
            OutputHandler outputHandler = context.outputHandler();
            InputHandler inputHandler = context.inputHandler();
            if (context.params().contains("h")) {
                outputHandler.put("""
                        Syntax:
                        > exit
                        Command responsive for stopping the program
                        PARAMS:
                        -h / --help\tShow this menu
                        -s / --save\tSaves the collection before exit (won't continue if save was unsuccessful and --force is not set)
                        -f / --force\tSkips all the confirmation steps
                        """);
                return;
            }
            if (context.params().contains("s")) {
                try {
                    saveCollection(context);
                }
                catch (CommandRuntimeException ex) {
                    if (!context.params().contains("f")) {
                        outputHandler.put("Error while saving the collection");
                        outputHandler.put("Operation aborted");
                        return;
                    }
                }
            }
            if (!context.params().contains("f") && !context.params().contains("s")) {
                outputHandler.hold("Are you sure, all unsaved data will be lost? (Empty input if yes) ");
                String response = inputHandler.get();
                if (!response.isBlank()) {
                    outputHandler.put("Operation aborted");
                    return;
                }
            }
            outputHandler.put("Stopping");
            ConsoleManager.stop();
        }

        /**
         * Action for <b>replace</b> command
         * Receives one argument
         * @param context
         */
        public static void replaceElement(CommandContext context) {
            OutputHandler outputHandler = context.outputHandler();
            InputHandler inputHandler = context.inputHandler();
            if (context.params().contains("h")) {
                outputHandler.put("""
                        Syntax:
                        > replace <id>
                        Command responsive for replacement of element from collection
                        PARAMS:
                        -h / --help\tShow this menu
                        --lower\t\tReplaces element if value lower than specified (default - id)
                        --greater\tReplaces element if value lower than specified (default - id)
                        --students\tMakes --lower and --greater parameters work with studentsCount field
                        --expelled\tMakes --lower and --greater parameters work with expelledStudents field
                        --avg-mark\tMakes --lower and --greater parameters work with averageMark field
                        """);
                return;
            }
            if (context.args().size() < 1)
                throw new CommandArgumentException("Argument wasn't provided");
            try {
                int id = Integer.parseInt(context.args().get(0));
                StudyGroup group = requestGroup(inputHandler, outputHandler);
                String fieldName = "getId";
                Class type = Integer.class;;
                if ((context.params().contains("s"))) {
                    fieldName = "getStudentsCount";
                    type = Long.class;
                } else if ((context.params().contains("e"))) {
                    fieldName = "getExpelledStudents";
                } else if ((context.params().contains("a"))) {
                    fieldName = "getAverageMark";
                    type = Double.class;
                }
                boolean update;
                if (context.params().contains("g")) {
                    update = replaceGroup(type, fieldName, context.args().get(0), 1, id, group);
                } else if (context.params().contains("l")) {
                    update = replaceGroup(type, fieldName, context.args().get(0), -1, id, group);
                } else {
                    update = replaceGroup(type, fieldName, context.args().get(0), 0, id, group);
                }
                outputHandler.put(update ? "Element updated" : "Element wasn't updated");
            }
            catch (NumberFormatException ex) {
                throw new CommandRuntimeException("Incorrect value type provided");
            }
        }

        /**
         * Action for <b>count</b> command
         * Receives one or two arguments
         * @param context
         */
        public static void countElements(CommandContext context) {
            OutputHandler outputHandler = context.outputHandler();
            InputHandler inputHandler = context.inputHandler();
            if (context.params().contains("h")) {
                outputHandler.put("""
                        Syntax:
                        > count <mark> [delta]
                        This command allows you to count the number of the groups with specified average mark (you can specify delta)
                        PARAMS:
                        -h / --help\tShow this menu
                        """);
                return;
            }
            if (context.args().size() < 1)
                throw new CommandArgumentException("Argument wasn't provided");
            double mark = Double.parseDouble(context.args().get(0));
            if (context.args().size() > 1) {
                double delta = Double.parseDouble(context.args().get(1));
                outputHandler.put("Count: " + CollectionManager.countAvgMarkDelta(mark, delta));
            } else {
                outputHandler.put("Count: " + CollectionManager.countAvgMark(mark));
            }
        }

        /**
         * Action for <b>filter</b> command
         * Receives one argument
         * @param context
         */
        public static void filterElement(CommandContext context) {
            OutputHandler outputHandler = context.outputHandler();
            InputHandler inputHandler = context.inputHandler();
            if (context.params().contains("h")) {
                outputHandler.put("""
                        Syntax:
                        > filter <pattern>
                        This command allows you to get all the groups where name contains specified substring
                        PARAMS:
                        -h / --help\tShow this menu
                        -r / --regex\tSubstring will be interpreted as regex pattern
                        """);
                return;
            }
            if (context.args().size() < 1)
                throw new CommandArgumentException("Argument wasn't provided");
            String substring = context.args().get(0);
            outputHandler.put("Filtering results" + (context.params().contains("r") ? "(regex)" : ""));
            if (context.params().contains("r")) {
                for (var it : CollectionManager.filterContentRegex(substring)) {
                    outputHandler.put(it.toString());
                }
            } else {
                for (var it : CollectionManager.filterContent(substring)) {
                    outputHandler.put(it.toString());
                }
            }
        }

        /**
         * Action for <b>getenv</b> command
         * Doesn't receive arguments
         * @param context
         */
        public static void getEnv(CommandContext context) {
            OutputHandler outputHandler = context.outputHandler();
            InputHandler inputHandler = context.inputHandler();
            if (context.params().contains("h")) {
                outputHandler.put("""
                        Syntax:
                        > getenv
                        This command shows the value of the environment variable 'LAB_5_PATH'
                        PARAMS:
                        -h / --help\tShow this menu
                        """);
                return;
            }
            String path = System.getenv("LAB_5_PATH");
            if (path == null)
                context.outputHandler().put("Environment variable 'LAB_5_PATH' is not set");
            else
                context.outputHandler().put("Environment variable 'LAB_5_PATH' value: '" + path + "'");
        }

        /**
         * Auxiliary function, contains logic
         * for inputting and creating new {@link StudyGroup}
         */
        private static StudyGroup requestGroup(InputHandler inputHandler, OutputHandler outputHandler) {
            Integer id = CollectionManager.genId();
            outputHandler.hold("Input group name (String): ");
            String groupName = getString(inputHandler, false);
            outputHandler.hold("Input group x coordinate (Integer): ");
            Integer groupX = getNumber(Integer.class, inputHandler, false);
            outputHandler.hold("Input group y coordinate (Long): ");
            Long groupY = getNumber(Long.class, inputHandler, false);
            Coordinates coordinates = new Coordinates(groupX, groupY);
            outputHandler.hold("Input students count (Long): ");
            Long students = getNumber(Long.class, inputHandler, false);
            outputHandler.hold("Input expelled students count (Int): ");
            Integer expelled = getNumber(Integer.class, inputHandler, false);
            outputHandler.hold("Input average mark value (Double): ");
            Double mark = getNumber(Double.class, inputHandler, false);
            outputHandler.hold("Input current semester (Variants: SECOND/THIRD/SEVENTH/EIGHT, value can be empty): ");
            Semester semester = getEnumField(Semester.class, inputHandler, true);
            Person admin = requestAdmin(inputHandler, outputHandler);
            return new StudyGroup(id, groupName, coordinates, students, expelled, mark, semester, admin);
        }

        /**
         * Auxiliary function, contains logic
         * for inputting and creating new {@link StudyGroup}
         */
        private static Person requestAdmin(InputHandler inputHandler, OutputHandler outputHandler) {
            outputHandler.hold("Input group admin name (String): ");
            String adminName = getString(inputHandler, false);
            outputHandler.hold("Input group admin height (Float): ");
            Float adminHeight = getNumber(Float.class, inputHandler, false);
            outputHandler.hold("Input admin eye color (Variants: GREEN/YELLOW/WHITE): ");
            EyeColor eyeColor = getEnumField(EyeColor.class, inputHandler, false);
            outputHandler.hold("Input admin hair color (Variants: GREEN/RED/BLACK/ORANGE/WHITE): ");
            HairColor hairColor = getEnumField(HairColor.class, inputHandler, false);
            outputHandler.hold("Input admin nationality (Variants: UNITED_KINGDOM/CHINA/VATICAN/SOUTH_KOREA): ");
            Country nation = getEnumField(Country.class, inputHandler, false);
            outputHandler.hold("Input admin x coordinate (Long): ");
            Long adminX = getNumber(Long.class, inputHandler, false);
            outputHandler.hold("Input admin y coordinate (Double): ");
            Double adminY = getNumber(Double.class, inputHandler, false);
            outputHandler.hold("Input admin z coordinate (Long): ");
            Long adminZ = getNumber(Long.class, inputHandler, false);
            return new Person(adminName, adminHeight, eyeColor, hairColor, nation, new Location(adminX, adminY, adminZ));
        }

        /**
         * Auxiliary function, contains logic
         * for inputting String
         */
        private static String getString(InputHandler inputHandler, boolean isNullable) {
            var value = inputHandler.get();
            value = (value.isBlank() ? null : value);
            return value;
        }

        /**
         * Auxiliary function, contains logic
         * for inputting numeric values
         */
        private static <T extends Number> T getNumber(Class<T> clazz, InputHandler inputHandler, boolean isNullable) {
            String value = getString(inputHandler, isNullable);
            if (value == null) {
                return null;
            }
            try {
                if (clazz == Double.class) {
                    return clazz.cast(Double.parseDouble(value));
                } else if (clazz == Integer.class) {
                    return clazz.cast(Integer.parseInt(value));
                } else if (clazz == Long.class) {
                    return clazz.cast(Long.parseLong(value));
                } else if (clazz == Float.class) {
                    return clazz.cast(Float.parseFloat(value));
                } else {
                    throw new IllegalArgumentException("Unsupported number class");
                }
            } catch (NumberFormatException e) {
                throw new CommandRuntimeException("Incorrect input format");
            }
        }

        /**
         * Auxiliary function, contains logic
         * for inputting Enum fields
         */
        private static <T extends Enum<T>> T getEnumField(Class<T> enumClass, InputHandler inputHandler, boolean isNullable) {
            String value = getString(inputHandler, isNullable);
            if (value == null) {
                return null;
            } else {
                try {
                    return Enum.valueOf(enumClass, value);
                } catch (IllegalArgumentException ex) {
                    throw new CommandRuntimeException("Incorrect value for " + enumClass.getSimpleName());
                }
            }
        }

        /**
         * Auxiliary function, contains logic
         * for removing groups
         */
        private static <T extends Number> int removeGroups(Class<T> clazz, String methodName, String arg, int mode) {
            T value;
            if (clazz == Double.class) {
                value = clazz.cast(Double.parseDouble(arg));
            } else if (clazz == Integer.class) {
                value = clazz.cast(Integer.parseInt(arg));
            } else if (clazz == Long.class) {
                value = clazz.cast(Long.parseLong(arg));
            } else if (clazz == Float.class) {
                value = clazz.cast(Float.parseFloat(arg));
            } else {
                throw new CommandRuntimeException("Unsupported number class");
            }
            try {
                Method method = StudyGroup.class.getMethod(methodName);
                return CollectionManager.remove((group) -> {
                    try {
                        return Math.signum(clazz.cast(method.invoke(group)).doubleValue() - value.doubleValue()) == Math.signum(mode);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new CommandRuntimeException(e.getMessage());
                    }
                });
            } catch (NoSuchMethodException e) {
                throw new CommandRuntimeException(e.getMessage());
            }
        }

        /**
         * Auxiliary function, contains logic
         * for replacing groups
         */
        private static <T extends Number> boolean replaceGroup(Class<T> clazz, String methodName, String arg, int mode, int id, StudyGroup newGroup) {
            T value;
            if (clazz == Double.class) {
                value = clazz.cast(Double.parseDouble(arg));
            } else if (clazz == Integer.class) {
                value = clazz.cast(Integer.parseInt(arg));
            } else if (clazz == Long.class) {
                value = clazz.cast(Long.parseLong(arg));
            } else if (clazz == Float.class) {
                value = clazz.cast(Float.parseFloat(arg));
            } else {
                throw new CommandRuntimeException("Unsupported number class");
            }
            try {
                Method method = StudyGroup.class.getMethod(methodName);
                return CollectionManager.replace((group) -> {
                    try {
                        return Math.signum(clazz.cast(method.invoke(group)).doubleValue() - value.doubleValue()) == Math.signum(mode);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new CommandRuntimeException(e.getMessage());
                    }
                }, id, newGroup);
            } catch (NoSuchMethodException e) {
                throw new CommandRuntimeException(e.getMessage());
            }
        }

        /**
         * This method is used to generate <i>Consumer</i> to
         * check parameters of specified command
         * @param params All possible parameters for the command
         * @param conflicts Parameters that conflict with each other
         * @return <i>Consumer</i> that checks parameter
         */
        @SafeVarargs
        public static Consumer<Set<String>> paramsChecker(ArrayList<Pair<String, String>> params, ArrayList<String>... conflicts) {
            return (raw) -> {
                Set<String> baked = new HashSet<>();
                boolean isPresent = false;
                for (var it : params) {
                    if (raw.contains(it.getValue0())) {
                        if (raw.contains(it.getValue1())) {
                            throw new CommandArgumentException("Duplicate parameters met");
                        } else {
                            if (baked.contains(it.getValue0())) {
                                throw new CommandArgumentException("Duplicate parameters met");
                            }
                            baked.add(it.getValue0());
                            raw.remove(it.getValue0());
                        }
                    } else if (raw.contains(it.getValue1())) {
                        if (baked.contains(it.getValue0())) {
                            throw new CommandArgumentException("Duplicate parameters met");
                        }
                        baked.add(it.getValue0());
                        raw.remove(it.getValue1());
                    }
                }
                if (!raw.isEmpty()) {
                    throw new CommandArgumentException("Unknown parameter options");
                }
                for (var it : conflicts) {
                    int counter = 0;
                    for (var jt : it) {
                        if (baked.contains(jt)) {
                            counter++;
                        }
                    }
                    if (counter > 1) {
                        throw new CommandArgumentException("Incompatible parameters met");
                    }
                }
                raw.addAll(baked);
            };
        }
    }
}