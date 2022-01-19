package com.assignment.daofab.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Child {
  private Long id;
  private Long parentId;
  private Long paidAmount;
  private Parent parent;
}
