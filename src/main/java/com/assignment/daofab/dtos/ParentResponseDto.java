package com.assignment.daofab.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParentResponseDto {
  private Long id;
  private String sender;
  private String receiver;
  private Long totalAmount;
  private Long totalPaidAmount;
}
