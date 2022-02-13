package com.propify.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.propify.challenge.controller.PropertyController;
import com.propify.challenge.dto.Address;
import com.propify.challenge.dto.Property;
import com.propify.challenge.service.PropertyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// More tests can be added, due to a lack of time and the simplicity of this project
// only a few tests were added.
@WebMvcTest(PropertyController.class)
class PropertyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropertyService propertyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSearch() throws Exception {
        when(propertyService.search(any(), any())).thenReturn(Set.of());

        mockMvc.perform(get("/properties"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testFindById() throws Exception {
        final var property = new Property();
        property.setId(1);
        property.setCode("1234");
        property.setEmailAddress("marty@mcfly.com");

        when(propertyService.findById(1)).thenReturn(property);

        mockMvc.perform(get("/property/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(new ObjectMapper().writeValueAsString(property))
                );
    }

    @Test
    public void testInsert() throws Exception {
        final var property = getValidPropertyWithoutID();

        mockMvc.perform(post("/property")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(property))
                )
                .andExpect(status().isOk());

        verify(propertyService, times(1)).insert(property);
    }

    @Test
    public void testInsertWithPropertyId() throws Exception {
        final var property = getValidPropertyWithoutID();
        property.setId(1);

        mockMvc.perform(post("/property")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(property))
                )
                .andExpect(status().isBadRequest());

        verify(propertyService, times(0)).update(any());
    }

    @Test
    public void testInsertWithoutRequestBody() throws Exception {
        mockMvc.perform(post("/property")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());

        verify(propertyService, times(0)).insert(any());
    }

    @Test
    public void testUpdate() throws Exception {
        final var property = getValidPropertyWithoutID();
        property.setId(1);

        mockMvc.perform(put("/property")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(property))
                )
                .andExpect(status().isOk());

        verify(propertyService, times(1)).update(property);
    }

    @Test
    public void testUpdateWithoutPropertyId() throws Exception {
        mockMvc.perform(put("/property")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getValidPropertyWithoutID()))
                )
                .andExpect(status().isBadRequest());

        verify(propertyService, times(0)).update(any());
    }

    @Test
    public void testUpdateWithoutRequestBody() throws Exception {
        mockMvc.perform(put("/property")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());

        verify(propertyService, times(0)).update(any());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/property/{id}", 1))
                .andExpect(status().isOk());
        verify(propertyService, times(1)).delete(1);
    }

    @Test
    public void testPropertyReport() throws Exception {
        mockMvc.perform(get("/properties/report"))
                .andExpect(status().isOk());
        verify(propertyService, times(1)).propertyReport();
    }

    private Property getValidPropertyWithoutID() {
        final var property = new Property();
        property.setCode("1234567890");
        property.setEmailAddress("marty@mcfly.com");

        final var address = new Address();
        address.setCity("Springfield");
        address.setState("SP");
        address.setStreet("742 evergreen");
        address.setZip("12345");
        address.setTimezone("UTC");
        property.setAddress(address);
        return property;
    }
}
