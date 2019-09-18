package com.lvdousha.flowable;

import java.util.HashMap;
import java.util.Map;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ObjectParamTest {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration().setJdbcUrl(
        "jdbc:mysql://localhost:3306/flowable?useUnicode=yes&characterEncoding=UTF-8&useSSL=true")
        .setJdbcUsername("root").setJdbcPassword("root").setJdbcDriver("com.mysql.jdbc.Driver")
        .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

    ProcessEngine processEngine = cfg.buildProcessEngine();
    
    RepositoryService repositoryService = processEngine.getRepositoryService();
    Deployment deployment = repositoryService.createDeployment().addClasspathResource("processes/objectParamTest.bpmn20.xml").deploy();
    System.out.println("文件定义id：" + deployment.getId());
    
    Map<String, Object> variables = new HashMap<String, Object>();
    JSONObject param = new JSONObject();
    param.put("operate", "1");
    Model model = new Model();
    model.operate = "1";
    variables.put("param", JSON.toJSON(model));
    
    RuntimeService runtimeService = processEngine.getRuntimeService();
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("objectParamTest", variables);
    System.out.println("流程ID：" + processInstance.getActivityId() + "==" + processInstance.getId() + "==" + processInstance.getCallbackId() + "==" + processInstance.getProcessInstanceId());
    
    String taskId = "17503";
    TaskService taskService = processEngine.getTaskService();
    Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
    taskService.setVariableLocal(taskId, taskId + "localVariable", taskId + "的私有变量");
    System.out.println("任务ID:" + task.getParentTaskId());
    taskService.complete(taskId);
  }

}
