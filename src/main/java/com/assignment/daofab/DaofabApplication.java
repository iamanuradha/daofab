package com.assignment.daofab;

import com.assignment.daofab.respositories.FileDataRepository;
import com.assignment.daofab.respositories.FileDataRepositoryImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DaofabApplication implements ApplicationRunner {

  @Value("${app.daofab.data.parents}")
  private String parentDataJson;

  @Value("${app.daofab.data.children}")
  private String childrenDataJson;

  public static void main(String[] args) {
    SpringApplication.run(DaofabApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    FileDataRepository fileDataRepository = new FileDataRepositoryImpl();
    fileDataRepository.init(parentDataJson, childrenDataJson);
  }

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    return modelMapper;
  }
}
