package com.propify.challenge.mapper;

import com.propify.challenge.dto.Address;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

// I would replace MyBatis with Spring Data
// I am strongly against XML configuration files
// because they slow down devs and they are cumbersome to work with.
@Mapper
public interface AddressMapper {

    void insert(Address address);

    Set<Address> search();

    Address findById(Integer id);

}
