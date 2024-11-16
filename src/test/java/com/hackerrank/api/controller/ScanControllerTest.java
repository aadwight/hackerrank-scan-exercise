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

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}
