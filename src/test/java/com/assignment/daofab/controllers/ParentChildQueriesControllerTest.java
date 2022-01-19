package com.assignment.daofab.controllers;

import com.assignment.daofab.exceptions.ParentNotFound;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ParentChildQueriesControllerTest {

  @Autowired private MockMvc mvc;

  @Test
  void testGetAllParents() throws Exception {
    ResultActions result = mvc.perform(get("/parents"));
    String response = result.andReturn().getResponse().getContentAsString();
    assertThat(response).contains("\"recordCount\":7");
  }

  @Test
  void testFindChildrenForParentId() throws Exception {
    ResultActions result = mvc.perform(get("/parent/1/children"));
    String response = result.andReturn().getResponse().getContentAsString();
    assertThat(response).contains("\"recordCount\":3");
  }

  @Test
  void testFindChildrenForParentIdDoesNotExists() {
    assertThatExceptionOfType(ParentNotFound.class)
        .isThrownBy(() -> mvc.perform(get("/parent/100/children")));
  }

  @Test
  void testParentWithNoChildren() throws Exception {
    ResultActions result = mvc.perform(get("/parent/7/children"));
    String response = result.andReturn().getResponse().getContentAsString();
    assertThat(response).contains("\"recordCount\":0");
  }
}
