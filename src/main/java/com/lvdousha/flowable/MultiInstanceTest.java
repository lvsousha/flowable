package com.lvdousha.flowable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;

public class MultiInstanceTest {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    MultiInstanceTest test = new MultiInstanceTest();
    test.test();
  }
  
  public void test() {
    ProcessEngine processEngine = FlowableUtils.init();
    
    FlowableUtils.deploy(processEngine, "processes/multiInstanceTest.bpmn20.xml");
    
    Map<String, Object> variables = new HashMap<String, Object>();
    String[] personList = {"user1", "user2", "user3"};
    variables.put("personList", Arrays.asList(personList));
    RuntimeService runtimeService = processEngine.getRuntimeService();
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("multiInstanceTest", variables);
    System.out.println("流程ID：" + processInstance.getId());
    
    String taskId = "27503";
    TaskService taskService = processEngine.getTaskService();
    taskService.setVariable(taskId, "operate", "agree");
    taskService.complete(taskId);
  }

}
