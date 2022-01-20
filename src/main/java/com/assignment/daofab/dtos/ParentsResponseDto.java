package com.assignment.daofab.dtos;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;

import java.util.Date;
import java.util.List;

@Getter
public class ParentsResponseDto extends BaseDto {
  private final Page<EntityModel<ParentResponseDto>> parents;

  public ParentsResponseDto(
      Page<EntityModel<ParentResponseDto>> parents, Date timestamp) {
    super(parents.getNumberOfElements(), timestamp);
    this.parents = parents;
  }
}
