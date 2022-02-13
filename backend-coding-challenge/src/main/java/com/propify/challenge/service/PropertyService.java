package com.propify.challenge.service;

import com.propify.challenge.dto.Property;
import com.propify.challenge.dto.PropertyReport;
import com.propify.challenge.mapper.PropertyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PropertyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyService.class);
    private final PropertyMapper propertyMapper;
    private final AlertService alertService;

    public PropertyService(final PropertyMapper propertyMapper,
                           final AlertService alertService) {
        this.propertyMapper = propertyMapper;
        this.alertService = alertService;
    }

    public Collection<Property> search(final String minRentPrice, final String maxRentPrice) {
        return propertyMapper.search(minRentPrice, maxRentPrice);
    }

    public Property findById(int id) {
        return propertyMapper.findById(id);
    }

    public void insert(final Property property) {
        propertyMapper.insert(property);
        LOGGER.info("CREATED: '{}'", property.getId());
    }

    public void update(final Property property) {
        propertyMapper.update(property);
        LOGGER.info("UPDATED: '{}'", property.getId());
    }

    @Transactional
    public void delete(int id) {
        propertyMapper.delete(id);
        LOGGER.info("DELETED: '{}'", id);
        final var executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> alertService.sendPropertyDeletedAlert(id));
    }

    public PropertyReport propertyReport() {
        var allProperties = propertyMapper.search(null, null);
        var propertyReport = new PropertyReport();

        // Calculate total quantity
        propertyReport.setTotalQuantity(allProperties.size());

        // Calculate the quantity of each type, 0 if there is no properties.
        propertyReport.setQuantityPerType(
                allProperties
                        .stream()
                        .map(Property::getType)
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        );

        // Calculate the average rent price (exclude the properties without rent price or with rent price = 0)
        propertyReport.setAverageRentPrice(
                allProperties
                        .stream()
                        .filter(e -> e.getRentPrice() != null || e.getRentPrice() > 0)
                        .collect(Collectors.averagingDouble(Property::getRentPrice))
        );

        // Calculate the quantity of properties in the state of Illinois (IL)
        propertyReport.setIllinoisQuantity(
                allProperties
                        .stream()
                        .map(Property::getAddress)
                        .filter(e -> "IL".equalsIgnoreCase(e.getState()))
                        .count()
        );

        return propertyReport;
    }
}
