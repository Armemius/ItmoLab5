package com.armemius.lab5.tasks;

import com.armemius.lab5.collection.CollectionManager;
import com.armemius.lab5.collection.data.StudyGroup;
import com.armemius.lab5.commands.CommandContext;
import com.armemius.lab5.commands.exceptions.CommandArgumentException;
import com.armemius.lab5.commands.exceptions.CommandRuntimeException;
import com.armemius.lab5.commands.params.Conflict;
import com.armemius.lab5.commands.params.Param;
import com.armemius.lab5.commands.params.Parametrized;
import com.armemius.lab5.io.InputHandler;
import com.armemius.lab5.io.OutputHandler;
import com.armemius.lab5.tasks.RequestTask;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Parametrized(
        params = {
                @Param(letter = "h", name = "help"),
                @Param(letter = "l", name = "lower"),
                @Param(letter = "g", name = "greater"),
                @Param(letter = "s", name = "students"),
                @Param(letter = "e", name = "expelled"),
                @Param(letter = "a", name = "avg-mark"),
                @Param(letter = "d", name = "admin")
        },
        incompatible = {
                @Conflict({"l", "g"}),
                @Conflict({"s", "e", "a", "d"})
        }
)
public class ReplaceTask extends RequestTask {
    /**
     * Action for <b>replace</b> command
     * Receives one argument
     * @param context
     */
    @Override
    public void execute(CommandContext context) {
        OutputHandler outputHandler = context.outputHandler();
        InputHandler inputHandler = context.inputHandler();
        if (context.params().contains("h")) {
            outputHandler.put("""
                        Syntax:
                        > replace <id>
                        Command responsive for replacement of element from collection
                        PARAMS:
                        -h / --help\tShow this menu
                        -l / --lower\tReplaces element if value lower than specified (default - id)
                        -g / --greater\tReplaces element if value lower than specified (default - id)
                        -s / --students\tMakes --lower and --greater parameters work with studentsCount field
                        -e / --expelled\tMakes --lower and --greater parameters work with expelledStudents field
                        -a / --avg-mark\tMakes --lower and --greater parameters work with averageMark field
                        """);
            return;
        }
        if (context.args().size() < 1)
            throw new CommandArgumentException("Argument wasn't provided");
        var params = context.params();
        if (!params.contains("s") && !params.contains("e") && !params.contains("a")
            && (params.contains("g") || params.contains("l")))
            throw new CommandArgumentException("Can't apply comparator modifiers to id");
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
            throw new CommandRuntimeException("Incorrect value for argument");
        }

    }

    /**
     * Auxiliary function, contains logic
     * for replacing groups
     */
    private <T extends Number> boolean replaceGroup(Class<T> clazz, String methodName, String arg, int mode, int id, StudyGroup newGroup) {
        T value;
        try {
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
            Method method = StudyGroup.class.getMethod(methodName);
            return CollectionManager.replace((group) -> {
                try {
                    return Math.signum(clazz.cast(method.invoke(group)).doubleValue() - value.doubleValue()) == Math.signum(mode);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new CommandRuntimeException(e.getMessage());
                }
            }, id, newGroup);
        } catch (NoSuchMethodException | NumberFormatException ex) {
            throw new CommandRuntimeException(ex.getMessage());
        }
    }
}
