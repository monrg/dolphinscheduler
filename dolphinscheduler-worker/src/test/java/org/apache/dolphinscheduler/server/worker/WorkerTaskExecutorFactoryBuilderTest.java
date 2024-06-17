package org.apache.dolphinscheduler.server.worker;

import org.apache.dolphinscheduler.common.utils.JSONUtils;
import org.apache.dolphinscheduler.plugin.storage.api.StorageOperate;
import org.apache.dolphinscheduler.plugin.task.api.TaskExecutionContext;
import org.apache.dolphinscheduler.plugin.task.api.TaskPluginManager;
import org.apache.dolphinscheduler.plugin.task.api.enums.DataType;
import org.apache.dolphinscheduler.plugin.task.api.enums.Direct;
import org.apache.dolphinscheduler.plugin.task.api.enums.TaskExecutionStatus;
import org.apache.dolphinscheduler.plugin.task.api.model.Property;
import org.apache.dolphinscheduler.plugin.task.dbt.DBTParameters;
import org.apache.dolphinscheduler.server.worker.config.WorkerConfig;
import org.apache.dolphinscheduler.server.worker.registry.WorkerRegistryClient;
import org.apache.dolphinscheduler.server.worker.rpc.WorkerMessageSender;
import org.apache.dolphinscheduler.server.worker.runner.DefaultWorkerTaskExecutor;
import org.apache.dolphinscheduler.server.worker.runner.DefaultWorkerTaskExecutorFactory;
import org.apache.dolphinscheduler.server.worker.runner.WorkerTaskExecutorFactoryBuilder;;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
class WorkerTaskExecutorFactoryBuilderTest {

    @Autowired
    WorkerTaskExecutorFactoryBuilder workerTaskExecutorFactoryBuilder;

    @Autowired(required = false)
    private StorageOperate storageOperate;

    @Autowired
    private WorkerConfig workerConfig;

    @Autowired
    private WorkerMessageSender workerMessageSender;

    @Autowired
    private TaskPluginManager taskPluginManager;

    @Autowired
    private WorkerRegistryClient workerRegistryClient;
    @Test
    void createWorkerTaskExecutorFactory() {
        taskPluginManager.loadPlugin();
        TaskExecutionContext taskExecutionContext = TaskExecutionContext.builder()
                .taskInstanceId(26)
                .taskName("step01")
                .firstSubmitTime(1717384886606L)
                .startTime(0L)
                .taskType("SHELL")
                .workflowInstanceHost("10.37.129.2:5678")
                .host("10.37.129.2:1234")
                .logPath("/Users/monreid/workspace/dolphinscheduler/logs/20240603/13746077030144/1/12/26.log")
                .processId(0)
                .executePath("/Users/monreid/workspace/dolphinscheduler/data/20240603/13746077030144/1/12/26")
                .processDefineCode(13746077030144L)
                .processDefineVersion(1)
                .processInstanceId(12)
                .scheduleTime(0L)
                .executorId(1)
                .cmdTypeIfComplement(0)
                .tenantCode("default")
                .processDefineId(0)
                .projectId(0)
                .projectCode(13743727827712L)
//                .taskParams("{\"localParams\":[],\"rawScript\":\"print(\\\"11\\\")\",\"resourceList\":[]}")
                .taskParams("{\"localParams\":[],\"rawScript\":\"echo \\\"11\\\"\",\"resourceList\":[]}")
                .tenantCode("monreid")
                .prepareParamsMap(
                        new HashMap<>()
//                        Map.of(
//                        "system.task.definition.name", new Property("system.task.definition.name", "IN", "VARCHAR", "step01"),
//                        "system.project.name", new Property("system.project.name", "IN", "VARCHAR", null),
//                        "system.project.code", new Property("system.project.code", "IN", "VARCHAR", "13743727827712"),
//                        "system.workflow.instance.id", new Property("system.workflow.instance.id", "IN", "VARCHAR", "12"),
//                        "system.biz.curdate", new Property("system.biz.curdate", "IN", "VARCHAR", "20240603"),
//                        "system.biz.date", new Property("system.biz.date", "IN", "VARCHAR", "20240602"),
//                        "system.task.instance.id", new Property("system.task.instance.id", "IN", "VARCHAR", "26"),
//                        "system.workflow.definition.name", new Property("system.workflow.definition.name", "IN", "VARCHAR", "py_test"),
//                        "system.task.definition.code", new Property("system.task.definition.code", "IN", "VARCHAR", "13746073770240"),
//                        "system.workflow.definition.code", new Property("system.workflow.definition.code", "IN", "VARCHAR", "13746077030144"),
//                        "system.datetime", new Property("system.datetime", "IN", "VARCHAR", "20240603112126")
//                )

                )
                .taskTimeout(Integer.MAX_VALUE)
                .workerGroup("default")
                .delayTime(0)
                .currentExecutionStatus(TaskExecutionStatus.SUBMITTED_SUCCESS)
                .endTime(0L)
                .dryRun(0)
                .cpuQuota(-1)
                .memoryMax(-1)
                .testFlag(0)
                .logBufferEnable(false)
                .dispatchFailTimes(0)
                .build();

        System.out.println(taskExecutionContext);

        System.out.println(taskExecutionContext);

        DefaultWorkerTaskExecutorFactory defaultWorkerTaskExecutorFactory = new DefaultWorkerTaskExecutorFactory(taskExecutionContext,
                workerConfig,
                workerMessageSender,
                taskPluginManager,
                storageOperate,
                workerRegistryClient);
        DefaultWorkerTaskExecutor workerTaskExecutor = defaultWorkerTaskExecutorFactory.createWorkerTaskExecutor();
        workerTaskExecutor.run();
    }


    @Test
    void testFaker() {
        String TASK_PARAMETER =
                "{\"localParams\":[{\"prop\":\"config\",\"direct\":\"IN\",\"type\":\"INTEGER\",\"value\":\"default_locale: en-EN\\nfields:\\n  - name: lastname\\n    nullRate: 0.1\\n    generators: [ Name#lastName ]\\n#  - name: random\\n#    nullRate: 0.1\\n#    generators: [ Regexify#[a-z]{4,10} ]\\n  - name: firstname\\n    locale: ja-JP\\n    generators: [ Name#firstName ]\\n  - name: phone numbers\\n    type: array\\n    minLength: 2\\n    maxLength: 5\\n    generators: [ PhoneNumber#phoneNumber, PhoneNumber#cellPhone ]\\n  - name: address\\n    type: struct\\n    fields:\\n      - name: country\\n        generators: [ Address#country ]\\n      - name: city\\n        generators: [ Address#city ]\\n      - name: street address\\n        generators: [ Address#streetAddress ]\"},{\"prop\":\"out\",\"direct\":\"IN\",\"type\":\"INTEGER\",\"value\":\"sinks:\\n  textfile:\\n    batchsize: 10000\\n    filepath: res\\n    mode: overwrite # errorifexists\\n    create_parent_dirs: true\\n  cli:\\n    batchsize: 10000\\nformats:\\n  csv:\\n    quote: \\\"@\\\"\\n    separator: $$$$$$$\\n  json:\\n  yaml:\\n  xml:\\n    pretty: true\"}],\"varPool\":null,\"rawScript\":\"java -jar /Users/monreid/workspace/datafaker-gen/datafaker-gen-core/target/datafaker-gen-core-1.0-SNAPSHOT-jar-with-dependencies.jar\",\"resourceList\":[]}\n";

        taskPluginManager.loadPlugin();
        TaskExecutionContext taskExecutionContext = TaskExecutionContext.builder()
                .taskInstanceId(26)
                .taskName("step01")
                .firstSubmitTime(1717384886606L)
                .startTime(0L)
                .taskType("DATAFAKER")
                .workflowInstanceHost("10.37.129.2:5678")
                .host("10.37.129.2:1234")
                .logPath("/Users/monreid/workspace/dolphinscheduler/logs/20240603/13746077030144/1/12/26.log")
                .processId(0)
                .executePath("/Users/monreid/workspace/dolphinscheduler/data/20240603/13746077030144/1/12/26")
                .processDefineCode(13746077030144L)
                .processDefineVersion(1)
                .processInstanceId(12)
                .scheduleTime(0L)
                .executorId(1)
                .cmdTypeIfComplement(0)
                .tenantCode("default")
                .processDefineId(0)
                .projectId(0)
                .environmentConfig("export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home")
                .projectCode(13743727827712L)
//                .taskParams("{\"localParams\":[],\"rawScript\":\"java -jar datafaker-gen-core-1.0-SNAPSHOT-jar-with-dependencies.jar\",\"resourceList\":[]}")
                .taskParams(TASK_PARAMETER)
                .tenantCode("monreid")
                .prepareParamsMap(
                        new HashMap<>()
//                        Map.of(
//                        "system.task.definition.name", new Property("system.task.definition.name", "IN", "VARCHAR", "step01"),
//                        "system.project.name", new Property("system.project.name", "IN", "VARCHAR", null),
//                        "system.project.code", new Property("system.project.code", "IN", "VARCHAR", "13743727827712"),
//                        "system.workflow.instance.id", new Property("system.workflow.instance.id", "IN", "VARCHAR", "12"),
//                        "system.biz.curdate", new Property("system.biz.curdate", "IN", "VARCHAR", "20240603"),
//                        "system.biz.date", new Property("system.biz.date", "IN", "VARCHAR", "20240602"),
//                        "system.task.instance.id", new Property("system.task.instance.id", "IN", "VARCHAR", "26"),
//                        "system.workflow.definition.name", new Property("system.workflow.definition.name", "IN", "VARCHAR", "py_test"),
//                        "system.task.definition.code", new Property("system.task.definition.code", "IN", "VARCHAR", "13746073770240"),
//                        "system.workflow.definition.code", new Property("system.workflow.definition.code", "IN", "VARCHAR", "13746077030144"),
//                        "system.datetime", new Property("system.datetime", "IN", "VARCHAR", "20240603112126")
//                )

                )
                .taskTimeout(Integer.MAX_VALUE)
                .workerGroup("default")
                .delayTime(0)
                .currentExecutionStatus(TaskExecutionStatus.SUBMITTED_SUCCESS)
                .endTime(0L)
                .dryRun(0)
                .cpuQuota(-1)
                .memoryMax(-1)
                .testFlag(0)
                .logBufferEnable(false)
                .dispatchFailTimes(0)
                .build();

        System.out.println(taskExecutionContext);

        System.out.println(taskExecutionContext);

        DefaultWorkerTaskExecutorFactory defaultWorkerTaskExecutorFactory = new DefaultWorkerTaskExecutorFactory(taskExecutionContext,
                workerConfig,
                workerMessageSender,
                taskPluginManager,
                storageOperate,
                workerRegistryClient);
        DefaultWorkerTaskExecutor workerTaskExecutor = defaultWorkerTaskExecutorFactory.createWorkerTaskExecutor();
        workerTaskExecutor.run();
    }


    @Test
    void dbttest() {
        taskPluginManager.loadPlugin();

        TaskExecutionContext taskExecutionContext = TaskExecutionContext.builder()
                .taskInstanceId(26)
                .taskName("step01")
                .firstSubmitTime(1717384886606L)
                .startTime(0L)
                .taskType("SHELL")
                .workflowInstanceHost("10.37.129.2:5678")
                .host("10.37.129.2:1234")
                .logPath("/Users/monreid/workspace/dolphinscheduler/logs/20240603/13746077030144/1/12/26.log")
                .processId(0)
                .executePath("/Users/monreid/workspace/dolphinscheduler/data/20240603/13746077030144/1/12/26")
                .processDefineCode(13746077030144L)
                .tenantCode("monreid")
                .processDefineVersion(1)
                .processInstanceId(12)
                .scheduleTime(0L)
                .executorId(1)
                .cmdTypeIfComplement(0)
                .processDefineId(0)
                .projectId(0)
                .projectCode(13743727827712L)
//                .taskParams("{\"localParams\":[],\"rawScript\":\"print(\\\"11\\\")\",\"resourceList\":[]}")
                .taskParams("{\"localParams\":[],\"rawScript\":\" cd dbt_project && dbt build\",\"resourceList\":[]}")
                .prepareParamsMap(
                        new HashMap<>()
//                        Map.of(
//                        "system.task.definition.name", new Property("system.task.definition.name", "IN", "VARCHAR", "step01"),
//                        "system.project.name", new Property("system.project.name", "IN", "VARCHAR", null),
//                        "system.project.code", new Property("system.project.code", "IN", "VARCHAR", "13743727827712"),
//                        "system.workflow.instance.id", new Property("system.workflow.instance.id", "IN", "VARCHAR", "12"),
//                        "system.biz.curdate", new Property("system.biz.curdate", "IN", "VARCHAR", "20240603"),
//                        "system.biz.date", new Property("system.biz.date", "IN", "VARCHAR", "20240602"),
//                        "system.task.instance.id", new Property("system.task.instance.id", "IN", "VARCHAR", "26"),
//                        "system.workflow.definition.name", new Property("system.workflow.definition.name", "IN", "VARCHAR", "py_test"),
//                        "system.task.definition.code", new Property("system.task.definition.code", "IN", "VARCHAR", "13746073770240"),
//                        "system.workflow.definition.code", new Property("system.workflow.definition.code", "IN", "VARCHAR", "13746077030144"),
//                        "system.datetime", new Property("system.datetime", "IN", "VARCHAR", "20240603112126")
//                )

                )
                .taskTimeout(Integer.MAX_VALUE)
                .workerGroup("default")
                .delayTime(0)
                .currentExecutionStatus(TaskExecutionStatus.SUBMITTED_SUCCESS)
                .endTime(0L)
                .dryRun(0)
                .cpuQuota(-1)
                .memoryMax(-1)
                .testFlag(0)
                .logBufferEnable(false)
                .dispatchFailTimes(0)
                .build();

        System.out.println(taskExecutionContext);

        System.out.println(taskExecutionContext);
//
//        MyDefaultWorkerTaskExecutorFactory defaultWorkerTaskExecutorFactory = new MyDefaultWorkerTaskExecutorFactory(taskExecutionContext,
//                workerConfig,
//                workerMessageSender,
//                taskPluginManager,
//                storageOperate);
//        MyDefaultWorkerTaskExecutor workerTaskExecutor = defaultWorkerTaskExecutorFactory.createWorkerTaskExecutor();
//        workerTaskExecutor.run();
    }

    public static void main(String[] args) {
        String TASK_PARAMETER =
                "{\"localParams\":[{\"prop\":\"config\",\"direct\":\"IN\",\"type\":\"INTEGER\",\"value\":\"default_locale: en-EN\\nfields:\\n  - name: lastname\\n    nullRate: 0.1\\n    generators: [ Name#lastName ]\\n#  - name: random\\n#    nullRate: 0.1\\n#    generators: [ Regexify#[a-z]{4,10} ]\\n  - name: firstname\\n    locale: ja-JP\\n    generators: [ Name#firstName ]\\n  - name: phone numbers\\n    type: array\\n    minLength: 2\\n    maxLength: 5\\n    generators: [ PhoneNumber#phoneNumber, PhoneNumber#cellPhone ]\\n  - name: address\\n    type: struct\\n    fields:\\n      - name: country\\n        generators: [ Address#country ]\\n      - name: city\\n        generators: [ Address#city ]\\n      - name: street address\\n        generators: [ Address#streetAddress ]\"},{\"prop\":\"out\",\"direct\":\"IN\",\"type\":\"INTEGER\",\"value\":\"sinks:\\n  textfile:\\n    batchsize: 10000\\n    filepath: res\\n    mode: overwrite # errorifexists\\n    create_parent_dirs: true\\n  cli:\\n    batchsize: 10000\\nformats:\\n  csv:\\n    quote: \\\"@\\\"\\n    separator: $$$$$$$\\n  json:\\n  yaml:\\n  xml:\\n    pretty: true\"}],\"varPool\":null,\"rawScript\":\"echo 1\",\"resourceList\":[]}\n";


        DBTParameters DBTParameters2 = JSONUtils.parseObject(TASK_PARAMETER, DBTParameters.class);
        Map<String, Property> localParametersMap = DBTParameters2.getLocalParametersMap();
        List<Property> localParams = DBTParameters2.localParams;

        String  ss="default_locale: en-EN\n" +
                "fields:\n" +
                "  - name: lastname\n" +
                "    nullRate: 0.1\n" +
                "    generators: [ Name#lastName ]\n" +
                "#  - name: random\n" +
                "#    nullRate: 0.1\n" +
                "#    generators: [ Regexify#[a-z]{4,10} ]\n" +
                "  - name: firstname\n" +
                "    locale: ja-JP\n" +
                "    generators: [ Name#firstName ]\n" +
                "  - name: phone numbers\n" +
                "    type: array\n" +
                "    minLength: 2\n" +
                "    maxLength: 5\n" +
                "    generators: [ PhoneNumber#phoneNumber, PhoneNumber#cellPhone ]\n" +
                "  - name: address\n" +
                "    type: struct\n" +
                "    fields:\n" +
                "      - name: country\n" +
                "        generators: [ Address#country ]\n" +
                "      - name: city\n" +
                "        generators: [ Address#city ]\n" +
                "      - name: street address\n" +
                "        generators: [ Address#streetAddress ]";

        String out="sinks:\n" +
                "  textfile:\n" +
                "    batchsize: 10000\n" +
                "    filepath: res\n" +
                "    mode: overwrite # errorifexists\n" +
                "    create_parent_dirs: true\n" +
                "  cli:\n" +
                "    batchsize: 10000\n" +
                "formats:\n" +
                "  csv:\n" +
                "    quote: \"@\"\n" +
                "    separator: $$$$$$$\n" +
                "  json:\n" +
                "  yaml:\n" +
                "  xml:\n" +
                "    pretty: true";

        localParams.add(new Property("config", Direct.IN, DataType.INTEGER, ss));
        localParams.add(new Property("out", Direct.IN, DataType.INTEGER, out));

        System.out.println(JSONUtils.toJsonString(DBTParameters2));
    }
}