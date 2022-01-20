package com.assignment.daofab.controllers;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

//TODO: move to object based assertions rather than string based

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ParentChildQueriesControllerTest {

  @Autowired private MockMvc mvc;

  @Test
  void testGetAllParents() throws Exception {
    ResultActions result = mvc.perform(get("/parents"));
    String response = result.andReturn().getResponse().getContentAsString();
    assertThat(response).contains("\"nextPage\"");
  }

  @Test
  void testFindChildrenForParentId() throws Exception {
    ResultActions result = mvc.perform(get("/parent/1/children"));
    String response = result.andReturn().getResponse().getContentAsString();
    assertThat(response).contains("backlink");
  }

  @Test
  void testFindChildrenForParentIdDoesNotExists() throws Exception {
    ResultActions result = mvc.perform(get("/parent/100/children"));
    String response = result.andReturn().getResponse().getContentAsString();
    assertThat(response).contains("Parent not found");
  }

  @Test
  void testParentWithNoChildren() throws Exception {
    ResultActions result = mvc.perform(get("/parent/7/children"));
    String response = result.andReturn().getResponse().getContentAsString();
    assertThat(response).contains("\"records\":0");
  }

  @Test
  void testParentWithInvalidPageRequest() throws Exception {
    ResultActions result = mvc.perform(get("/parents?page=-100"));
    String response = result.andReturn().getResponse().getContentAsString();
    assertThat(response).contains("Invalid page number");
  }
}
