package com.assignment.daofab.services;

import com.assignment.daofab.exceptions.ParentNotFound;
import com.assignment.daofab.models.Child;
import com.assignment.daofab.models.Parent;

import java.util.List;

public interface ParentChildQueryService {
  List<Parent> getParents();

  List<Child> findChildrenByParentId(Long parentId) throws ParentNotFound;
}
