package com.propify.challenge.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.ZoneId;
import java.util.Objects;

public class Address {
    @NotBlank
    private String street; // must not be null or blank

    @NotBlank
    private String city; // must not be null or blank

    @NotBlank
    @Size(min = 2, max = 2)
    private String state; // 2-letter code, must not be null or blank

    @NotBlank
    @Size(min = 5, max = 5)
    private String zip; // 5-digit code, must not be null or blank

    @NotBlank
    // Since I am running out of time, I implemented the extra validation in the setter method.
    // Ideally, that should be converted into an annotation like `@ValidTimeZone`
    private String timezone; // Must be a valid timezone

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        try {
            this.timezone = ZoneId.of(timezone).toString();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Invalid Time Zone string!");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) && Objects.equals(city, address.city) && Objects.equals(state, address.state) && Objects.equals(zip, address.zip) && Objects.equals(timezone, address.timezone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, state, zip, timezone);
    }
}
