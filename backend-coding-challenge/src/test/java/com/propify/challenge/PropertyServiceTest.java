package com.propify.challenge;

import com.propify.challenge.dto.Address;
import com.propify.challenge.dto.Property;
import com.propify.challenge.dto.PropertyType;
import com.propify.challenge.mapper.AddressMapper;
import com.propify.challenge.mapper.PropertyMapper;
import com.propify.challenge.service.AlertService;
import com.propify.challenge.service.PropertyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PropertyServiceTest {
    @Autowired
    PropertyService propertyService;

    @MockBean
    PropertyMapper propertyMapper;

    @MockBean
    AddressMapper addressMapper;

    @MockBean
    AlertService alertService;

    // TODO: add at least 3 tests to the method propertyService.propertyReport()
    @Test
    public void testPropertyReport() {
        final var property = createProperty(10D, "12345",
                PropertyType.TOWNHOUSE, "marty@mcfly.com"
        );
        final var address = createAddress("Springfield", "SP",
                "742 evergreen", "12345", "UTC");
        property.setAddress(address);

        when(propertyMapper.search(null, null)).thenReturn(Set.of(property));

        final var propertyReport = propertyService.propertyReport();

        assertAll(() -> {
            assertEquals(10, propertyReport.getAverageRentPrice());
            assertEquals(0, propertyReport.getIllinoisQuantity());
            assertEquals(1, propertyReport.getTotalQuantity());

            assertEquals(1, propertyReport.getQuantityPerType().size());
            assertTrue(propertyReport.getQuantityPerType()
                    .entrySet()
                    .stream()
                    .allMatch(e -> e.getKey().equals(PropertyType.TOWNHOUSE) && e.getValue() == 1)
            );
        });
    }

    @Test
    public void testPropertyReport2() {
        when(propertyMapper.search(null, null)).thenReturn(Set.of());

        final var propertyReport = propertyService.propertyReport();

        assertAll(() -> {
            assertEquals(0, propertyReport.getAverageRentPrice());
            assertEquals(0, propertyReport.getIllinoisQuantity());
            assertEquals(0, propertyReport.getTotalQuantity());
            assertEquals(0, propertyReport.getQuantityPerType().size());
        });
    }

    @Test
    public void testPropertyReport3() {
        final Property property = createProperty(10D, "12345",
                PropertyType.TOWNHOUSE, "marty@mcfly.com");

        final var address = createAddress("Springfield", "SP",
                "742 evergreen", "12345", "UTC");
        property.setAddress(address);

        final Property property2 = createProperty(100D, "56789",
                PropertyType.MULTI_FAMILY, "bart@simpson.com");

        final var address2 = createAddress("illinois", "IL",
                "123 fake st", "45678", "UTC");
        property2.setAddress(address2);

        when(propertyMapper.search(null, null)).thenReturn(Set.of(property, property2));

        final var propertyReport = propertyService.propertyReport();

        assertAll(() -> {
            assertEquals(55, propertyReport.getAverageRentPrice());
            assertEquals(1, propertyReport.getIllinoisQuantity());
            assertEquals(2, propertyReport.getTotalQuantity());

            assertEquals(2, propertyReport.getQuantityPerType().size());

            final var entries = propertyReport.getQuantityPerType().entrySet();
            assertTrue(entries.stream().anyMatch(e -> e.getKey().equals(PropertyType.TOWNHOUSE) && e.getValue() == 1));
            assertTrue(entries.stream().anyMatch(e -> e.getKey().equals(PropertyType.MULTI_FAMILY) && e.getValue() == 1));
        });
    }

    private Address createAddress(String city, String state, String street, String zip, String timeZone) {
        final var address = new Address();
        address.setCity(city);
        address.setState(state);
        address.setStreet(street);
        address.setZip(zip);
        address.setTimezone(timeZone);
        return address;
    }

    private Property createProperty(double rentPrice, String code, PropertyType townhouse, String emailAddress) {
        final var property = new Property();
        property.setRentPrice(rentPrice);
        property.setCode(code);
        property.setType(townhouse);
        property.setEmailAddress(emailAddress);
        return property;
    }
}
