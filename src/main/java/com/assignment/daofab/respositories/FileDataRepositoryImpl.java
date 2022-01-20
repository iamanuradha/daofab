package com.assignment.daofab.respositories;

import com.assignment.daofab.exceptions.InitializationException;
import com.assignment.daofab.exceptions.ParentNotFound;
import com.assignment.daofab.models.Child;
import com.assignment.daofab.models.Parent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FileDataRepositoryImpl implements FileDataRepository {

  private static Map<Long, Parent> associations;

  @Override
  public void init(String parentDataJson, String childrenDataJson) throws InitializationException {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      File resourceFile = ResourceUtils.getFile(parentDataJson);
      Map<String, List<Parent>> parsedParent =
          objectMapper.readValue(resourceFile, new TypeReference<Map<String, List<Parent>>>() {});
      List<Parent> parents = parsedParent.entrySet().iterator().next().getValue();
      parents.sort((a, b) -> (int) (a.getId() - b.getId()));

      resourceFile = ResourceUtils.getFile(childrenDataJson);
      Map<String, List<Child>> parsedChildren =
          objectMapper.readValue(resourceFile, new TypeReference<Map<String, List<Child>>>() {});
      List<Child> children = parsedChildren.entrySet().iterator().next().getValue();
      children.sort((a, b) -> (int) (a.getId() - b.getId()));

      associateParentChildRelation(parents, children);
    } catch (IOException e) {
      throw new InitializationException("Error while reading data ", e);
    }
  }

  @Override
  public List<Parent> getParents() {
    return new ArrayList(associations.values());
  }

  @Override
  public List<Child> findChildrenForParentId(Long parentId) throws ParentNotFound {
    if (!associations.containsKey(parentId)) {
      throw new ParentNotFound("Parent not found parentId:" + parentId);
    }
    return associations.get(parentId).getChildren();
  }

  private void associateParentChildRelation(List<Parent> parents, List<Child> children) {
    associations = new HashMap<>(parents.size());
    for (Parent parent : parents) {
      associations.put(parent.getId(), parent);
    }
    for (Child child : children) {
      Long parentId = child.getParentId();
      Parent parent = associations.get(parentId);
      parent.setTotalPaidAmount(parent.getTotalPaidAmount() + child.getPaidAmount());
      child.setReceiver(parent.getReceiver());
      child.setSender(parent.getSender());
      child.setTotalAmount(parent.getTotalAmount());
      List<Child> parentChildren = parent.getChildren();
      parentChildren.add(child);
    }
  }
}
