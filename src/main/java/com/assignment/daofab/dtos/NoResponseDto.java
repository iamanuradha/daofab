package com.assignment.daofab.dtos;

import lombok.Getter;

import java.util.Date;

@Getter
public class NoResponseDto extends BaseDto {

  public NoResponseDto() {
    super(0L, new Date());
  }
}
