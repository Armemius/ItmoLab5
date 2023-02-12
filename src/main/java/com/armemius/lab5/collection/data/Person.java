package com.armemius.lab5.collection.data;


import com.armemius.lab5.collection.exceptions.CollectionRuntimeException;

import java.util.Objects;

@SuppressWarnings("unused")
public class Person {
    public Person() {}

    public Person(String name, float height, EyeColor eyeColor, HairColor hairColor, Country nationality, Location location) {
        if (name == null || name.isEmpty())
            throw new CollectionRuntimeException("Incorrect parameters for Person");
        this.name = name;
        if (height <= 0)
            throw new CollectionRuntimeException("Incorrect parameters for Person");
        this.height = height;
        if (eyeColor == null)
            throw new CollectionRuntimeException("Incorrect parameters for Person");
        this.eyeColor = eyeColor;
        if (hairColor == null)
            throw new CollectionRuntimeException("Incorrect parameters for Person");
        this.hairColor = hairColor;
        if (nationality == null)
            throw new CollectionRuntimeException("Incorrect parameters for Person");
        this.nationality = nationality;
        if (location == null)
            throw new CollectionRuntimeException("Incorrect parameters for Person");
        this.location = location;
    }

    private String name;
    private float height;
    private EyeColor eyeColor;
    private HairColor hairColor;
    private Country nationality;
    private Location location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new CollectionRuntimeException("Incorrect parameters for setter");
        }
        this.name = name;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        if (height <= 0) {
            throw new CollectionRuntimeException("Incorrect parameters for setter");
        }
        this.height = height;
    }

    public EyeColor getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(EyeColor eyeColor) {
        if (eyeColor == null) {
            throw new CollectionRuntimeException("Incorrect parameters for setter");
        }
        this.eyeColor = eyeColor;
    }

    public HairColor getHairColor() {
        return hairColor;
    }

    public void setHairColor(HairColor hairColor) {
        if (hairColor == null) {
            throw new CollectionRuntimeException("Incorrect parameters for setter");
        }
        this.hairColor = hairColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        if (nationality == null) {
            throw new CollectionRuntimeException("Incorrect parameters for setter");
        }
        this.nationality = nationality;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        if (location == null) {
            throw new CollectionRuntimeException("Incorrect parameters for setter");
        }
        this.location = location;
    }

    @Override
    public String toString() {
        return "Person(name: " + name
                + ", height: " + height
                + ", eyeColor:" + eyeColor
                + ", hairColor: " + hairColor
                + ", nationality:" + nationality
                + ", location:" + location + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (!(o instanceof Person))
            return false;
        Person person = (Person) o;
        return Float.compare(person.height, height) == 0 && name.equals(person.name) && eyeColor == person.eyeColor && hairColor == person.hairColor && nationality == person.nationality && location.equals(person.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, height, eyeColor, hairColor, nationality, location);
    }
}
