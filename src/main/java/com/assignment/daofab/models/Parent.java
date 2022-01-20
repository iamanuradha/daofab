package com.assignment.daofab.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Parent {
  private Long id;
  private String sender;
  private String receiver;
  private Long totalAmount;
  private Long totalPaidAmount = 0L;
  private List<Child> children = new ArrayList<>();
}
