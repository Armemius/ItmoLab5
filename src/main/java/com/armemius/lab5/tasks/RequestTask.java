package com.armemius.lab5.tasks;

import com.armemius.lab5.collection.CollectionManager;
import com.armemius.lab5.collection.data.*;
import com.armemius.lab5.commands.CommandContext;
import com.armemius.lab5.commands.exceptions.CommandRuntimeException;
import com.armemius.lab5.io.InputHandler;
import com.armemius.lab5.io.OutputHandler;

/**
 * <b>Request</b> class represents tasks for commands with data input (e.g. {@link StudyGroup})
 */
public abstract class RequestTask extends InputTask {
    /**
     * Auxiliary function, contains logic
     * for inputting and creating new {@link StudyGroup}
     */
    protected StudyGroup requestGroup(InputHandler inputHandler, OutputHandler outputHandler) {
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
    protected Person requestAdmin(InputHandler inputHandler, OutputHandler outputHandler) {
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
}
