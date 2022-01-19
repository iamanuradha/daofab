package com.assignment.daofab.respositories;

import com.assignment.daofab.exceptions.InitializationException;
import com.assignment.daofab.exceptions.ParentNotFound;
import com.assignment.daofab.models.Child;
import com.assignment.daofab.models.Parent;

import java.util.List;

public interface FileDataRepository {
  void init(String parentDataJson, String childrenDataJson) throws InitializationException;

  List<Parent> getParents();

  List<Child> findChildrenForParentId(Long parentId) throws ParentNotFound;
}
