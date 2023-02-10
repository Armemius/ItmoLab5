package com.armemius.lab5.collection.data;

import com.armemius.lab5.collection.exceptions.CollectionRuntimeException;

@SuppressWarnings("unused")
public class Coordinates {
    public Coordinates() {}

    public Coordinates(int x, Long y) {
        this.x = x;
        if (y <= -266)
            throw new CollectionRuntimeException("Incorrect parameters for Coordinates");
        else
            this.y = y;
    }
    private int x;
    private Long y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        if (y <= -266)
            throw new CollectionRuntimeException("Incorrect parameter for setter");
        else
            this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates(x: " + x
                + ", y: " + y + ")";
    }
}
