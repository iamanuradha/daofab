package com.assignment.daofab.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public abstract class BaseDto {
  private final Long recordCount;
  private final Date timestamp;
}
