package com.armemius.lab5.tasks;

import com.armemius.lab5.collection.CollectionManager;
import com.armemius.lab5.collection.data.*;
import com.armemius.lab5.commands.CommandContext;
import com.armemius.lab5.io.InputHandler;
import com.armemius.lab5.io.OutputHandler;
import com.armemius.lab5.tasks.RequestTask;

public class InsertTask extends RequestTask {
    /**
     * Action for <b>insert</b> command
     * Doesn't receive arguments
     * @param context
     */
    @Override
    public void execute(CommandContext context) {
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
            CollectionManager.add(genRandomGroup());
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

    protected StudyGroup genRandomGroup() {
        return new StudyGroup(CollectionManager.genId(),
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
        );
    }
}
