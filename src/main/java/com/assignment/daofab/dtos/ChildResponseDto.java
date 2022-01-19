package com.assignment.daofab.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildResponseDto {
  private Long id;
  private Long parentId;
  private Long paidAmount;
}
