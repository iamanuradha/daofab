package com.assignment.daofab.models;

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
  private List<Child> children = new ArrayList<>();
}
