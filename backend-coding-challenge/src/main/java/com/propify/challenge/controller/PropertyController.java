package com.propify.challenge.controller;

import com.propify.challenge.dto.Property;
import com.propify.challenge.dto.PropertyReport;
import com.propify.challenge.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class PropertyController {
    private final PropertyService propertyService;

    PropertyController(final PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/properties")
    public Collection<Property> search(final String minRentPrice, final String maxRentPrice) {
        return propertyService.search(minRentPrice, maxRentPrice);
    }

    @GetMapping("/property/{id}")
    public Property findById(@PathVariable final Integer id) {
        return propertyService.findById(id);
    }

    @PostMapping("/property")
    // It should return a Property object with the ID coming from the DB.
    // I run out of time, so this method is not saving nested entities
    // like Address or PropertyType.
    // (Honestly, I would replace MyBatis with Spring Data and/or Hibernate
    // since I found MyBatis quite a legacy, error-prone and cumbersome
    // technology to work with).
    public void insert(@RequestBody @Validated final Property property) {
        if (property.getId() != null) {
            throw new IllegalArgumentException("Property ID must be absent for insert operations!");
        }
        propertyService.insert(property);
    }

    @PutMapping("/property")
    // It should return the updated Property object
    public void update(@RequestBody @Validated final Property property) {
        if (property.getId() == null) {
            throw new IllegalArgumentException("Property ID must be present for update operations!");
        }
        propertyService.update(property);
    }

    @DeleteMapping("/property/{id}")
    public void delete(@PathVariable final Integer id) {
        propertyService.delete(id);
    }

    @GetMapping("/properties/report")
    public PropertyReport report() {
        return propertyService.propertyReport();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private String handle(final IllegalArgumentException e) {
        return e.getMessage();
    }
}
