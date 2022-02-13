package com.propify.challenge.dto;

import javax.validation.constraints.*;
import java.util.Objects;

public class Property {
    // A trade-off was chosen for this validation.
    // I would rather have two DTOs, one for insert operations and the other for update operations.
    // In this case, I opted for reusing the same DTO since this is the only difference between both operations
    // and the validation are made in each controller handler method.

    private Integer id; // must be null for INSERT and not null for UPDATE
    private String createTime;
    private PropertyType type;

    @Positive
    @Digits(integer = Integer.MAX_VALUE, fraction = 2)
    private Double rentPrice; // must be greater than 0, 2 decimal places

    @NotNull
    private Address address; // must not be null

    @Email
    private String emailAddress; // must be a valid email address

    @NotNull
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^[A-Z0-9]*$")
    private String code; // not null, only uppercase letters or numbers, 10 characters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public PropertyType getType() {
        return type;
    }

    public void setType(PropertyType type) {
        this.type = type;
    }

    public Double getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(Double rentPrice) {
        this.rentPrice = rentPrice;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property property = (Property) o;
        return Objects.equals(id, property.id) && Objects.equals(createTime, property.createTime) && Objects.equals(type, property.type) && Objects.equals(rentPrice, property.rentPrice) && Objects.equals(address, property.address) && Objects.equals(emailAddress, property.emailAddress) && Objects.equals(code, property.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createTime, type, rentPrice, address, emailAddress, code);
    }
}
