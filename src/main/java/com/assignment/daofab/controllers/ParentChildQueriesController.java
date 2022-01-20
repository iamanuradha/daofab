package com.assignment.daofab.controllers;

import com.assignment.daofab.dtos.*;
import com.assignment.daofab.exceptions.InvalidPageException;
import com.assignment.daofab.exceptions.ParentNotFound;
import com.assignment.daofab.models.Child;
import com.assignment.daofab.models.Parent;
import com.assignment.daofab.services.ParentChildQueryService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ParentChildQueriesController {

  private static final int PAGINATION_DEFAULT_START_PAGE = 1;
  private static final String KEY_LINKS_TO_CHILDREN = "children";
  private static final String KEY_LINKS_TO_PARENTS = "backlink";
  private static final String KEY_LINKS_TO_NEXT_PAGE = "nextPage";
  public static final String REQUEST_PARAM_PAGE = "page";
  private final ParentChildQueryService service;
  private final ModelMapper modelMapper;

  @Value("${app.daofab.pagination.limit}")
  private int pageSize;

  public ParentChildQueriesController(ParentChildQueryService service, ModelMapper modelMapper) {
    this.service = service;
    this.modelMapper = modelMapper;
  }

  @GetMapping(path = "/parents")
  public EntityModel<BaseDto> parents(@RequestParam(REQUEST_PARAM_PAGE) Optional<Integer> page)
      throws ParentNotFound, InvalidPageException {

    final List<Parent> parents = service.getParents();
    if (parents.isEmpty()) {
      return emptyResponse();
    }

    final int currentPage = page.orElse(PAGINATION_DEFAULT_START_PAGE);
    if (currentPage <= 0) {
      throw new InvalidPageException("Invalid page number " + currentPage);
    }
    return this.toParentsResponseDto(parents, currentPage);
  }

  @GetMapping(path = "/parent/{parentId}/children")
  public EntityModel<BaseDto> findChildrenForParentId(@PathVariable Long parentId)
      throws ParentNotFound, InvalidPageException {

    final List<Child> children = service.findChildrenByParentId(parentId);
    if (children.isEmpty()) {
      return emptyResponse();
    }

    final List<EntityModel<ChildResponseDto>> childrenResponseDto = toChildResponseDto(children);
    return EntityModel.of(new ChildrenResponseDto(childrenResponseDto, new Date()));
  }

  private EntityModel<BaseDto> toParentsResponseDto(List<Parent> parents, Integer currentPage)
      throws ParentNotFound, InvalidPageException {

    int fromIndex = (currentPage - 1) * pageSize;
    if (fromIndex > parents.size()) {
      currentPage = PAGINATION_DEFAULT_START_PAGE;
      fromIndex = 0;
    }
    List<Parent> parentsPage = nextPageParents(parents, fromIndex);
    Type listType = new TypeToken<List<ParentResponseDto>>() {}.getType();
    List<ParentResponseDto> mappedDto = modelMapper.map(parentsPage, listType);
    List<EntityModel<ParentResponseDto>> entityModel = addLinksToChildren(mappedDto);
    Page<EntityModel<ParentResponseDto>> page = addPagination(parents, currentPage, entityModel);
    return addLinkToNextPage(currentPage, parents.size(), page);
  }

  private EntityModel<BaseDto> addLinkToNextPage(
      Integer currentPage, int totalRecords, Page<EntityModel<ParentResponseDto>> page)
      throws ParentNotFound, InvalidPageException {
    int nextPage = currentPage > totalRecords/pageSize ? PAGINATION_DEFAULT_START_PAGE : currentPage + 1;
    EntityModel<BaseDto> response = EntityModel.of(new ParentsResponseDto(page, new Date()));
    WebMvcLinkBuilder linkToNextPage =
        linkTo(methodOn(this.getClass()).parents(Optional.of(nextPage)));
    response.add(linkToNextPage.withRel(KEY_LINKS_TO_NEXT_PAGE));
    return response;
  }

  private PageImpl<EntityModel<ParentResponseDto>> addPagination(
      List<Parent> parents, Integer currentPage, List<EntityModel<ParentResponseDto>> entityModel) {
    return new PageImpl<>(entityModel, PageRequest.of(currentPage, pageSize), parents.size());
  }

  private List<EntityModel<ParentResponseDto>> addLinksToChildren(List<ParentResponseDto> mappedDto)
      throws ParentNotFound, InvalidPageException {
    List<EntityModel<ParentResponseDto>> entityModel = new ArrayList<>(mappedDto.size());
    for (ParentResponseDto dto : mappedDto) {
      EntityModel<ParentResponseDto> model = EntityModel.of(dto);
      WebMvcLinkBuilder linkToChildren =
          linkTo(methodOn(this.getClass()).findChildrenForParentId(dto.getId()));
      model.add(linkToChildren.withRel(KEY_LINKS_TO_CHILDREN));
      entityModel.add(model);
    }
    return entityModel;
  }

  private List<Parent> nextPageParents(List<Parent> parents, int fromIndex) {
    int toIndex = Math.min(fromIndex + pageSize, parents.size());
    return parents.subList(fromIndex, toIndex);
  }

  private List<EntityModel<ChildResponseDto>> toChildResponseDto(List<Child> children)
      throws ParentNotFound, InvalidPageException {
    Type listType = new TypeToken<List<ChildResponseDto>>() {}.getType();
    List<ChildResponseDto> mappedDto = modelMapper.map(children, listType);
    return addLinkToParentsRequest(mappedDto);
  }

  private List<EntityModel<ChildResponseDto>> addLinkToParentsRequest(
      List<ChildResponseDto> mappedDto) throws ParentNotFound, InvalidPageException {
    List<EntityModel<ChildResponseDto>> entityModel = new ArrayList<>(mappedDto.size());
    for (ChildResponseDto dto : mappedDto) {
      EntityModel<ChildResponseDto> model = EntityModel.of(dto);
      WebMvcLinkBuilder backlink = linkTo(methodOn(this.getClass()).parents(Optional.of(1)));
      model.add(backlink.withRel(KEY_LINKS_TO_PARENTS));
      entityModel.add(model);
    }
    return entityModel;
  }

  private EntityModel<BaseDto> emptyResponse() {
    return EntityModel.of(new NoResponseDto());
  }
}
