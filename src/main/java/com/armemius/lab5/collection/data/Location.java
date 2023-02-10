package com.armemius.lab5.collection.data;

import com.armemius.lab5.collection.exceptions.CollectionRuntimeException;

@SuppressWarnings("unused")
public class Location {
    public Location() {}

    public Location(long x, Double y, Long z) {
        this.x = x;
        if (y == null)
            throw new CollectionRuntimeException("Incorrect parameters for Location");
        else
            this.y = y;
        if (z == null)
            throw new CollectionRuntimeException("Incorrect parameters for Location");
        else
            this.z = z;
    }

    private long x;
    private Double y;
    private Long z;

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        if (y == null)
            throw new CollectionRuntimeException("Incorrect parameter for setter");
        else
            this.y = y;
    }

    public Long getZ() {
        return z;
    }

    public void setZ(Long z) {
        if (z == null)
            throw new CollectionRuntimeException("Incorrect parameter for setter");
        else
            this.z = z;
    }

    @Override
    public String toString() {
        return "Location(x: " + x
                + ", y: " + y
                + ", z: " + z + ")";
    }
}
