/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.dolphinscheduler.plugin.task.datafaker;

import lombok.extern.slf4j.Slf4j;

import org.apache.dolphinscheduler.common.utils.JSONUtils;
import org.apache.dolphinscheduler.plugin.task.api.AbstractTask;
import org.apache.dolphinscheduler.plugin.task.api.ShellCommandExecutor;
import org.apache.dolphinscheduler.plugin.task.api.TaskCallBack;
import org.apache.dolphinscheduler.plugin.task.api.TaskException;
import org.apache.dolphinscheduler.plugin.task.api.TaskExecutionContext;
import org.apache.dolphinscheduler.plugin.task.api.model.TaskResponse;
import org.apache.dolphinscheduler.plugin.task.api.parameters.AbstractParameters;
import org.apache.dolphinscheduler.plugin.task.api.shell.IShellInterceptorBuilder;
import org.apache.dolphinscheduler.plugin.task.api.shell.ShellInterceptorBuilderFactory;
import org.apache.dolphinscheduler.plugin.task.api.utils.ParameterUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.apache.dolphinscheduler.plugin.task.api.TaskConstants.EXIT_CODE_FAILURE;

import org.apache.dolphinscheduler.plugin.task.datafaker.utils.FileUtils;
import org.zeroturnaround.zip.ZipUtil;

@Slf4j
public class DataFakerTask extends AbstractTask {

    /**
     * shell parameters
     */
    private DataFakerParameters dataFakerParameters;

    /**
     * shell command executor
     */
    private ShellCommandExecutor shellCommandExecutor;

    /**
     * taskExecutionContext
     */
    private TaskExecutionContext taskExecutionContext;


    /**
     * constructor
     *
     * @param taskExecutionContext taskExecutionContext
     */
    public DataFakerTask(TaskExecutionContext taskExecutionContext) {
        super(taskExecutionContext);

        this.taskExecutionContext = taskExecutionContext;
        this.shellCommandExecutor = new ShellCommandExecutor(this::logHandle, taskExecutionContext);
    }

    @Override
    public void init() {

        dataFakerParameters = JSONUtils.parseObject(taskExecutionContext.getTaskParams(), DataFakerParameters.class);
        log.info("Initialize data faker task params {}", JSONUtils.toPrettyJsonString(dataFakerParameters));
        String config = dataFakerParameters.getConfigContent();


        if (dataFakerParameters == null || !dataFakerParameters.checkParameters()) {
            throw new TaskException("shell task params is not valid");
        }
        try {
            String executePath = taskExecutionContext.getExecutePath();
            File file = new File(executePath);
            String parent = file.getParentFile().getParent();
            String taskName = taskExecutionContext.getTaskName();
            ZipUtil.unpack(new File(dataFakerParameters.dbtprojectResource()), new File(parent));
            String outPath = dataFakerParameters.outputResource();
            String out = org.apache.dolphinscheduler.common.utils.FileUtils.readFile2Str(Files.newInputStream(Paths.get(outPath))).replace("${filepath}",
                    parent + "/dbt_project/seeds/" + taskName + ".csv");
            FileUtils.writeConfigToFile(executePath + "/config.yaml", config);
            FileUtils.writeConfigToFile(executePath + "/output.yaml", out);
            taskExecutionContext.setExecutePath(executePath);
            dataFakerParameters.setRawScript(String.format(getScript(), dataFakerParameters.datafakerResource(),
                    dataFakerParameters.getFormat(), dataFakerParameters.getDataNumber(), dataFakerParameters.getSink(),
                    parent).replaceAll("\\r\\n", System.lineSeparator()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String getScript() {
        /**
         * (1) spark-submit [options] <app jar | python file> [app arguments]
         * (2) spark-sql [options] -f <filename>
         */

        String cmd = "export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home\n" +
                "java -jar %s -f %s -n %d -sink %s\n" +
                "cd  %s/dbt_project\n" +
                "dbt seed";

        return cmd;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handle(TaskCallBack taskCallBack) throws TaskException {
        try {
            IShellInterceptorBuilder<?, ?> shellActuatorBuilder = ShellInterceptorBuilderFactory.newBuilder()
                    .properties(ParameterUtils.convert(taskExecutionContext.getPrepareParamsMap()))
                    .appendScript(dataFakerParameters.getRawScript());
            TaskResponse commandExecuteResult = shellCommandExecutor.run(shellActuatorBuilder, taskCallBack);
            setExitStatusCode(commandExecuteResult.getExitStatusCode());
            setProcessId(commandExecuteResult.getProcessId());
            dataFakerParameters.dealOutParam(shellCommandExecutor.getTaskOutputParams());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("The current Shell task has been interrupted", e);
            setExitStatusCode(EXIT_CODE_FAILURE);
            throw new TaskException("The current Shell task has been interrupted", e);
        } catch (Exception e) {
            log.error("shell task error", e);
            setExitStatusCode(EXIT_CODE_FAILURE);
            throw new TaskException("Execute shell task error", e);
        }
    }

    @Override
    public void cancel() throws TaskException {
        // cancel process
        try {
            shellCommandExecutor.cancelApplication();
        } catch (Exception e) {
            throw new TaskException("cancel application error", e);
        }
    }

    @Override
    public AbstractParameters getParameters() {
        return dataFakerParameters;
    }

}
