package com.propify.challenge.dto;

import java.util.Map;
import java.util.Objects;

public class PropertyReport {
    private Integer totalQuantity;
    private Map<PropertyType, Long> quantityPerType;
    private Double averageRentPrice;
    private Long illinoisQuantity;

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Map<PropertyType, Long> getQuantityPerType() {
        return quantityPerType;
    }

    public void setQuantityPerType(Map<PropertyType, Long> quantityPerType) {
        this.quantityPerType = quantityPerType;
    }

    public Double getAverageRentPrice() {
        return averageRentPrice;
    }

    public void setAverageRentPrice(Double averageRentPrice) {
        this.averageRentPrice = averageRentPrice;
    }

    public Long getIllinoisQuantity() {
        return illinoisQuantity;
    }

    public void setIllinoisQuantity(Long illinoisQuantity) {
        this.illinoisQuantity = illinoisQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyReport that = (PropertyReport) o;
        return Objects.equals(totalQuantity, that.totalQuantity) && Objects.equals(quantityPerType, that.quantityPerType) && Objects.equals(averageRentPrice, that.averageRentPrice) && Objects.equals(illinoisQuantity, that.illinoisQuantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalQuantity, quantityPerType, averageRentPrice, illinoisQuantity);
    }
}
