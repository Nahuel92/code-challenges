package com.propify.challenge;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.propify.challenge.dto.Property;
import com.propify.challenge.dto.PropertyType;
import com.propify.challenge.mapper.PropertyMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@SpringJUnitConfig
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("PropertyMapperTest.xml")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public class PropertyMapperTest {
    @Autowired
    PropertyMapper propertyMapper;

    @Test
    public void testInsert() {
        final var property = new Property();
        property.setRentPrice(3000.99);
        property.setType(PropertyType.MULTI_FAMILY);

        propertyMapper.insert(property);

        // The insert operation should return the ID from DB.
        // This is quick and dirt hack to get the property saved in the previous step.
        final var properties = propertyMapper.search("3000", "3001");

        assertAll(() -> {
            assertEquals(1, properties.size());
            assertTrue(properties.stream().allMatch(e -> e.getRentPrice() == 3000.99));
        });
    }

    @Test
    public void testFindById() {
        var property = propertyMapper.findById(1);

        assertAll(() -> {
            assertNotNull(property);
            assertEquals(1000.99, property.getRentPrice());
        });
    }

    @Test
    public void testSearch() {
        var property = propertyMapper.search("500", "801");

        assertAll(() -> {
            assertNotNull(property);
            assertEquals(2, property.size());
            assertTrue(property.stream().anyMatch(e -> e.getRentPrice() == 500.5));
            assertTrue(property.stream().anyMatch(e -> e.getRentPrice() == 800.9));
        });
    }

    @Test
    public void testUpdate() {
        final var originalProperty = propertyMapper.findById(3);
        originalProperty.setRentPrice(9999.9);

        propertyMapper.update(originalProperty);

        final var updatedProperty = propertyMapper.findById(3);
        assertAll(() -> {
            assertNotNull(updatedProperty);
            assertEquals(9999.9, updatedProperty.getRentPrice());
        });
    }

    @Test
    public void testDelete() {
        propertyMapper.delete(4);

        final var property = propertyMapper.findById(4);
        assertNull(property);
    }
}
