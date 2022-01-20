package com.assignment.daofab.dtos;

import lombok.Getter;
import org.springframework.hateoas.EntityModel;

import java.util.Date;
import java.util.List;

@Getter
public class ChildrenResponseDto extends BaseDto {
  private final List<EntityModel<ChildResponseDto>> children;

  public ChildrenResponseDto(
      List<EntityModel<ChildResponseDto>> children, Date timestamp) {
    super(children.size(), timestamp);
    this.children = children;
  }
}
