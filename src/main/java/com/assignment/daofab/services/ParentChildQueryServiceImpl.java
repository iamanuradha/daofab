package com.assignment.daofab.services;

import com.assignment.daofab.exceptions.ParentNotFound;
import com.assignment.daofab.models.Child;
import com.assignment.daofab.models.Parent;
import com.assignment.daofab.respositories.FileDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParentChildQueryServiceImpl implements ParentChildQueryService {
  private final FileDataRepository repository;

  public ParentChildQueryServiceImpl(FileDataRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Parent> getParents() {
    return repository.getParents();
  }

  @Override
  public List<Child> findChildrenByParentId(Long parentId) throws ParentNotFound {
    return repository.findChildrenForParentId(parentId);
  }
}
