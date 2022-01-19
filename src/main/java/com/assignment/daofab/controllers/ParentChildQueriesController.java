package com.assignment.daofab.controllers;

import com.assignment.daofab.dtos.*;
import com.assignment.daofab.exceptions.ParentNotFound;
import com.assignment.daofab.models.Child;
import com.assignment.daofab.models.Parent;
import com.assignment.daofab.services.ParentChildQueryService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ParentChildQueriesController {

  private final ParentChildQueryService service;
  private final ModelMapper modelMapper;

  public ParentChildQueriesController(ParentChildQueryService service, ModelMapper modelMapper) {
    this.service = service;
    this.modelMapper = modelMapper;
  }

  @GetMapping(path = "/parents")
  public BaseDto parents() throws ParentNotFound {
    final List<Parent> parents = service.getParents();
    if (parents.isEmpty()) {
      return emptyResponse();
    }
    final List<EntityModel<ParentResponseDto>> parentResponseDto =
        this.toParentResponseDto(parents);
    return new ParentsResponseDto(parentResponseDto, (long) parentResponseDto.size(), new Date());
  }

  @GetMapping(path = "/parent/{parentId}/children")
  public BaseDto findChildrenForParentId(@PathVariable Long parentId) throws ParentNotFound {
    final List<Child> children = service.findChildrenByParentId(parentId);
    if (children.isEmpty()) {
      return emptyResponse();
    }
    final List<EntityModel<ChildResponseDto>> childrenResponseDto = toChildResponseDto(children);
    return new ChildrenResponseDto(
        childrenResponseDto, (long) childrenResponseDto.size(), new Date());
  }

  private List<EntityModel<ParentResponseDto>> toParentResponseDto(List<Parent> parents)
      throws ParentNotFound {
    Type listType = new TypeToken<List<ParentResponseDto>>() {}.getType();
    List<ParentResponseDto> mappedDto = modelMapper.map(parents, listType);
    List<EntityModel<ParentResponseDto>> entityModel = new ArrayList<>(mappedDto.size());
    for (ParentResponseDto dto : mappedDto) {
      EntityModel<ParentResponseDto> model = EntityModel.of(dto);
      WebMvcLinkBuilder linkToChildren =
          linkTo(methodOn(this.getClass()).findChildrenForParentId(dto.getId()));
      model.add(linkToChildren.withRel("children"));
      entityModel.add(model);
    }
    return entityModel;
  }

  private List<EntityModel<ChildResponseDto>> toChildResponseDto(List<Child> children)
      throws ParentNotFound {
    Type listType = new TypeToken<List<ChildResponseDto>>() {}.getType();
    List<ChildResponseDto> mappedDto = modelMapper.map(children, listType);
    List<EntityModel<ChildResponseDto>> entityModel = new ArrayList<>(mappedDto.size());
    for (ChildResponseDto dto : mappedDto) {
      EntityModel<ChildResponseDto> model = EntityModel.of(dto);
      WebMvcLinkBuilder backlink = linkTo(methodOn(this.getClass()).parents());
      model.add(backlink.withRel("backlink"));
      entityModel.add(model);
    }
    return entityModel;
  }

  private BaseDto emptyResponse() {
    return new NoResponseDto();
  }
}
