package com.hackerrank.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.api.model.Scan;
import com.hackerrank.api.repository.ScanRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
class ScanControllerTest {
  ObjectMapper om = new ObjectMapper();
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ScanRepository repository;

  @Test
  public void testCreation() throws Exception {
    Scan expectedRecord = Scan.builder().domainName("java.com").build();
    Scan actualRecord = om.readValue(mockMvc.perform(post("/scan")
            .contentType("application/json")
            .content(om.writeValueAsString(expectedRecord)))
            .andDo(print())
            .andExpect(jsonPath("$.id", greaterThan(0)))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), Scan.class);

    Assertions.assertEquals(expectedRecord.getDomainName(), actualRecord.getDomainName());
  }

  @Test
  public void testGetById() throws Exception {
      mockMvc.perform(get("/scan/{id}", 1))
              .andDo(print())
              .andExpect(status().isOk())
              .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
  }

  @Test
  public void testGetByIdNotFound() throws Exception {
    mockMvc.perform(get("/scan/{id}", 10))
            .andExpect(status().isNotFound());
  }

  @Test
  public void testDelete() throws Exception {
    mockMvc.perform(delete("/scan/{id}", 1))
            .andExpect(status().isOk());
  }

  @Test
  public void testDeleteNotFound() throws Exception {
    mockMvc.perform(delete("/scan/{id}", 10))
            .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteTwice() throws Exception {
    mockMvc.perform(delete("/scan/{id}", 1))
            .andExpect(status().isOk());
    mockMvc.perform(delete("/scan/{id}", 1))
            .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteAndFind() throws Exception {
    mockMvc.perform(get("/scan/{id}", 1))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    mockMvc.perform(delete("/scan/{id}", 1))
            .andExpect(status().isOk());
    mockMvc.perform(get("/scan/{id}", 10))
            .andExpect(status().isNotFound());
  }

  @Test
  public void testSearch() throws Exception {
    mockMvc.perform(get("/scan/search/{domain}", "domain1.com").param("orderBy", "numMissingImages"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].numMissingImages").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].numMissingImages").value(2));
  }

  @Test
  public void testSearchBadRequest() throws Exception {
    mockMvc.perform(get("/scan/search/{domain}", "domain1.com").param("orderBy", "kitten"))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }
}
