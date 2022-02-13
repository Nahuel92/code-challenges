package com.propify.challenge.dto;

import java.util.Objects;

public class PropertyType {
    public static final PropertyType SINGLE_FAMILY = new PropertyType("Single Family");
    public static final PropertyType MULTI_FAMILY = new PropertyType("Multi-family");
    public static final PropertyType CONDOMINIUM = new PropertyType("Condominium");
    public static final PropertyType TOWNHOUSE = new PropertyType("Townhouse");

    private final String type;

    public PropertyType(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyType that = (PropertyType) o;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
