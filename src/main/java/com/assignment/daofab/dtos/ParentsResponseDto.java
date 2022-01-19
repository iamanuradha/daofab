package com.assignment.daofab.dtos;

import lombok.Getter;
import org.springframework.hateoas.EntityModel;

import java.util.Date;
import java.util.List;

@Getter
public class ParentsResponseDto extends BaseDto {
  private final List<EntityModel<ParentResponseDto>> parents;

  public ParentsResponseDto(
      List<EntityModel<ParentResponseDto>> parents, Long recordCount, Date timestamp) {
    super(recordCount, timestamp);
    this.parents = parents;
  }
}
