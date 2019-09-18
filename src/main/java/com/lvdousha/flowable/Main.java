package com.lvdousha.flowable;

import java.util.HashMap;
import java.util.List;
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
import com.alibaba.fastjson.JSONObject;

public class Main {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    Main main = new Main();
    main.avoidStaffProcess();
  }

  public void avoidStaffProcess() {
    ProcessEngine processEngine = init();
    // deploy(processEngine, "processes/avoidStaffProcess.bpmn20.xml");
    apply(processEngine, "仲裁员助理2");
    // assistantAudit(processEngine, "22505");
    // leaderSendBack(processEngine, "22505");
    getTaskUser(processEngine, "仲裁员助理2");
    getTaskGroup(processEngine, "leader");
  }

  public void leaderSendBack(ProcessEngine processEngine, String taskId) {
    TaskService taskService = processEngine.getTaskService();
    JSONObject param = new JSONObject();
    param.put("sendBackReason", "退回理由");
    param.put("operator", "操作人员");
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("leaderId", "部门负责人ID");
    variables.put("operate", "sendBack");
    variables.put("extraParams", param);
    taskService.complete(taskId, variables);
  }

  public void assistantAudit(ProcessEngine processEngine, String taskId) {
    TaskService taskService = processEngine.getTaskService();
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("infomation", "助理编辑审批表2");
    taskService.complete(taskId, variables);
  }

  public void getTaskUser(ProcessEngine processEngine, String userId) {
    TaskService taskService = processEngine.getTaskService();
    List<Task> tasks = taskService.createTaskQuery().taskAssignee(userId).list();
    System.out.println("getTaskUser) You have " + tasks.size() + " tasks:");
    for (int i = 0; i < tasks.size(); i++) {
      Task task = tasks.get(i);
      System.out.println(
          (i + 1) + ") " + task.getName() + "==" + task.getAssignee() + "==" + task.getOwner());
    }
  }

  public void getTaskGroup(ProcessEngine processEngine, String group) {
    TaskService taskService = processEngine.getTaskService();
    List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(group).list();
    System.out.println("getTaskGroup) You have " + tasks.size() + " tasks:");
    for (int i = 0; i < tasks.size(); i++) {
      Task task = tasks.get(i);
      System.out.println(
          (i + 1) + ") " + task.getName() + "==" + task.getAssignee() + "==" + task.getOwner());
    }
  }

  public ProcessInstance apply(ProcessEngine processEngine, String assistantId) {
    RuntimeService runtimeService = processEngine.getRuntimeService();

    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("assistantId", assistantId);
    ProcessInstance processInstance =
        runtimeService.startProcessInstanceByKey("avoidStaffProcess", variables);

    return processInstance;
  }

  public Deployment deploy(ProcessEngine processEngine, String bpmnFile) {
    RepositoryService repositoryService = processEngine.getRepositoryService();
    Deployment deployment =
        repositoryService.createDeployment().addClasspathResource(bpmnFile).deploy();
    return deployment;
  }


  public ProcessEngine init() {
    ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration().setJdbcUrl(
        "jdbc:mysql://localhost:3306/flowable?useUnicode=yes&characterEncoding=UTF-8&useSSL=true")
        .setJdbcUsername("root").setJdbcPassword("root").setJdbcDriver("com.mysql.jdbc.Driver")
        .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

    ProcessEngine processEngine = cfg.buildProcessEngine();
    return processEngine;
  }
}
