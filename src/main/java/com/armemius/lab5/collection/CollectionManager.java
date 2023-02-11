package com.armemius.lab5.collection;

import com.armemius.lab5.collection.data.Person;
import com.armemius.lab5.collection.data.StudyGroup;
import com.armemius.lab5.collection.exceptions.CollectionFileException;
import com.armemius.lab5.collection.exceptions.CollectionRuntimeException;
import com.armemius.lab5.commands.exceptions.CommandArgumentException;
import com.armemius.lab5.commands.exceptions.CommandRuntimeException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * <b>CollectionManager</b> is singleton class that handles
 * operations with collection and its data
 *
 * On initialization, it sets up serializer
 * and gets environment variable '<i>LAB_5_PATH</i>' that points to
 * file with data
 */
public class CollectionManager {
    private CollectionManager() {}

    private static TreeMap<Integer, StudyGroup> storage;
    private static String path;
    private static final ZonedDateTime creationTime;
    private static final ObjectMapper mapper;

    static {
        storage = new TreeMap<>();
        creationTime = ZonedDateTime.now();
        path = System.getenv("LAB_5_PATH");
        if (path == null) {
            System.out.println("""

                    ###############! WARNING !###############
                    Environment variable 'LAB_5_PATH' is not set
                    Collection manager will use default path './data.yaml'
                    #########################################
                    
                    """);
            path = "./data.yaml";
        }
        mapper = new ObjectMapper(new YAMLFactory());
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.findAndRegisterModules();
    }

    /**
     * Method that checks if collection contains specified id
     * @param id Id to check
     * @return Returns True if element is in the collection, otherwise returns False
     */
    public static boolean checkId(int id) {
        return storage.containsKey(id);
    }

    /**
     * Method that returns collection's type
     * @return Collection's class type
     */
    public static String getCollectionType() {
        return storage.getClass().toString();
    }

    /**
     * Method that returns collection initialization time
     * @return Collection initialization time
     */
    public static ZonedDateTime getCreationTime() {
        return creationTime;
    }

    /**
     * Method that returns number of elements inside collection
     * @return Number of elements inside collection
     */
    public static long getElementsCount() {
        return storage.size();
    }

    /**
     * Loads collection from file
     */
    public static void load() {
        try {
            storage = mapper.readValue(new File(path), new TypeReference<>() {});
            for (var it : storage.entrySet()) {
                if (it.getKey() != it.getValue().getId())
                    throw new CollectionRuntimeException("Broken data");
            }
        }
        catch (CollectionRuntimeException | DatabindException e) {
            System.out.println("'" + path + "' contains broken data");
            storage.clear();
            StudyGroup.getUsedIds().clear();
        }
        catch (IOException e) {
            System.out.println("Unable to load '" + path + "' file with data\n" + e.getMessage());
        }
    }

    /**
     * Saves collection to file
     * @throws CollectionFileException Throws exception if there were troubles with writing the collection to file
     */
    public static void save() throws CollectionFileException {
        try {
            mapper.writeValue(new File(path), storage);
        } catch (IOException e) {
            throw new CollectionFileException(e.getMessage());
        }
    }

    /**
     * Method that outputs storage as list of <b>StudyGroup</b>
     * @return List of <b>StudyGroup</b>
     */
    public static List<StudyGroup> getAll() {
        List<StudyGroup> groups = new ArrayList<>();
        for (var it : storage.entrySet()) {
            groups.add(it.getValue());
        }
        return groups;
    }

    /**
     * Method that adds new <b>StudyGroup</b> to collection
     * @param group <b>StudyGroup</b> to add
     */
    public static void add(StudyGroup group) {
        storage.put(genId(), group);
    }

    /**
     * Method that updates <b>StudyGroup</b> in collection
     * @param id Id of the <b>StudyGroup</b>
     * @param group New <b>StudyGroup</b>
     */
    public static void update(int id, StudyGroup group) {
        if (!storage.containsKey(id))
            throw new CommandRuntimeException("Can't find the element with id " + id);
        storage.replace(id, group);
    }

    /**
     * Clears the collection
     */
    public static void clear() {
        storage.clear();
        StudyGroup.getUsedIds().clear();
    }

    /**
     * Method that removes elements from collection
     * @see com.armemius.lab5.ConsoleManager.Actions#removeGroups(Class, String, String, int)
     * @param comparator Predicate that returns true if StudyGroup should be removed
     * @return Number of removals
     */
    public static int remove(Predicate<StudyGroup> comparator) {
        int removals = 0;

        for (var it : getAll()) {
            if (comparator.test(it)) {
                storage.remove(it.getId());
                StudyGroup.getUsedIds().remove(it.getId());
                removals++;
            }
        }

        return removals;
    }


    /**
     * Method that replaces element from collection to another
     * @param comparator Predicate that returns true if StudyGroup should be replaced
     * @see com.armemius.lab5.ConsoleManager.Actions#replaceGroup(Class, String, String, int, int, StudyGroup)
     * @param id Id of the group to be replaced
     * @param group New group
     * @return True if replacement was successful, otherwise returns false
     */
    public static boolean replace(Predicate<StudyGroup> comparator, int id, StudyGroup group) {
        if (!storage.containsKey(id))
            throw new CommandRuntimeException("Can't find the element with id " + id);
        if (comparator.test(storage.get(id))) {
            storage.replace(id, group);
            return true;
        }
        return false;
    }

    /**
     * Removes first match with specified <i>groupAdmin</i>
     * @param admin Group to be removed with corresponding <i>groupAdmin</i>
     * @return True if there was removal, otherwise returns false
     */
    public static boolean removeAnyByGroupAdmin(Person admin) {
        for (var it : getAll()) {
            if (it.getGroupAdmin().equals(admin)) {
                storage.remove(it.getId());
                StudyGroup.getUsedIds().remove(it.getId());
                return true;
            }
        }
        return false;
    }

    /**
     * Counts how many element are equal to specified <i>averageMark</i>
     * @param avgMark Mark to compare with
     * @return Number of elements that are equal to specified <i>averageMark</i>
     */
    public static int countAvgMark(double avgMark) {
        int count = 0;

        for (var it : storage.entrySet()) {
            if (it.getValue().getAverageMark() == avgMark)
                count++;
        }
        return count;
    }

    /**
     * Counts how many element are equal to specified <i>averageMark</i> within specified delta
     * @param avgMark Mark to compare with
     * @param delta Maximal deviation for mark
     * @return Number of elements that are equal to specified <i>averageMark</i> within delta
     */
    public static int countAvgMarkDelta(double avgMark, double delta) {
        int count = 0;
        for (var it : storage.entrySet()) {
            if (Math.abs(avgMark - it.getValue().getAverageMark()) < delta)
                count++;
        }
        return count;
    }

    /**
     * Finds all the elements that has specified substring in their name
     * @param substring Substring to match with
     * @return List of all matched elements
     */
    public static List<StudyGroup> filterContent(String substring) {
        List<StudyGroup> result = new ArrayList<>();
        for (var it : storage.entrySet()) {
            if (it.getValue().getName().contains(substring))
                result.add(it.getValue());
        }
        return result;
    }

    /**
     * Finds all the elements that matches regex pattern
     * @param regex Regex to match with
     * @return List of all matched elements
     */
    public static List<StudyGroup> filterContentRegex(String regex) {
        try {
            List<StudyGroup> result = new ArrayList<>();
            for (var it : storage.entrySet()) {
                if (Pattern.matches(regex, it.getValue().getName()))
                    result.add(it.getValue());
            }
            return result;
        } catch (PatternSyntaxException ex) {
            throw new CommandArgumentException("Incorrect regex pattern");
        }

    }

    /**
     * Generates free id for group
     * @return Free id
     */
    public static int genId() {
        int id = 1;
        while (storage.containsKey(id)) {
            id++;
        }
        return id;
    }
}
