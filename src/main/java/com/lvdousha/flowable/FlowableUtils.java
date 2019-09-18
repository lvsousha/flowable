package com.lvdousha.flowable;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;

public class FlowableUtils {

  
  public static ProcessEngine init() {
    ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration().setJdbcUrl(
        "jdbc:mysql://localhost:3306/flowable?useUnicode=yes&characterEncoding=UTF-8&useSSL=true")
        .setJdbcUsername("root").setJdbcPassword("root").setJdbcDriver("com.mysql.jdbc.Driver")
        .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

    ProcessEngine processEngine = cfg.buildProcessEngine();
    return processEngine;
  }

  public static Deployment deploy(ProcessEngine processEngine, String bpmnFile) {
    RepositoryService repositoryService = processEngine.getRepositoryService();
    Deployment deployment =
        repositoryService.createDeployment().addClasspathResource(bpmnFile).deploy();
    return deployment;
  }
}
