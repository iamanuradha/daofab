package com.assignment.daofab.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Child {
  private Long id;
  private String sender;
  private String receiver;
  private Long totalAmount;
  private Long paidAmount;
  private Long parentId;
}
