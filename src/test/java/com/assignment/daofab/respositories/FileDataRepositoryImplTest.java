package com.assignment.daofab.respositories;

import com.assignment.daofab.exceptions.InitializationException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
class FileDataRepositoryImplTest {

  @Test
  public void testInit() throws InitializationException {
    FileDataRepository repository = new FileDataRepositoryImpl();
    repository.init("classpath:Parent.json", "classpath:Child.json");
    assertFalse(repository.getParents().isEmpty());
  }

  @Test
  public void testInitFailForParentMissing() throws InitializationException {
    FileDataRepository repository = new FileDataRepositoryImpl();
    try {
      repository.init("classpath:P.json", "classpath:Child.json");
      throw new AssertionError("Init should fail");
    } catch (InitializationException e) {
      assertTrue(e.getMessage().contains("Error while reading data"));
      return;
    }
  }

  @Test
  public void testInitFailForChildMissing() throws InitializationException {
    FileDataRepository repository = new FileDataRepositoryImpl();
    try {
      repository.init("classpath:Parent.json", "classpath:C.json");
      throw new AssertionError("Init should fail");
    } catch (InitializationException e) {
      assertTrue(e.getMessage().contains("Error while reading data"));
      return;
    }
  }
}
