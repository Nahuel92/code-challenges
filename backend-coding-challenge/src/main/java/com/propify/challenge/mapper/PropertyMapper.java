package com.propify.challenge.mapper;

import com.propify.challenge.dto.Property;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface PropertyMapper {
    void insert(final Property property);

    Set<Property> search(final String minRentPrice, final String maxRentPrice);

    Property findById(final Integer id);

    void update(final Property property);

    void delete(final Integer id);
}
