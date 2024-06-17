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

package org.apache.dolphinscheduler.plugin.task.dbt;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.dolphinscheduler.plugin.task.api.model.ResourceInfo;
import org.apache.dolphinscheduler.plugin.task.api.parameters.AbstractParameters;
import org.apache.dolphinscheduler.plugin.task.dbt.utils.FileUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class DBTParameters extends AbstractParameters {

    /**
     * shell script
     */
    private String rawScript;

    /**
     * resource list
     */
    private List<ResourceInfo> resourceList;

    /**
     * arguments
     */
    private String modelContent;


    /**
     * arguments
     */
    private String modelSchemaContent;



    private Map<String, String> resourceMap;

    public static final String DBT_PROJECT = "dbt_project.zip";

    public String getRawScript() {
        return rawScript;
    }

    public void setRawScript(String rawScript) {
        this.rawScript = rawScript;
    }

    public List<ResourceInfo> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<ResourceInfo> resourceList) {
        this.resourceList = resourceList;
    }

    @Override
    public boolean checkParameters() {
        // check DBT_PROJECT and DATAFAKER exist
        getResource(DBT_PROJECT);
        return modelContent != null && !modelContent.isEmpty();
    }

    public String dbtprojectResource() {
        return getResource(DBT_PROJECT);
    }


    private String getResource(String resource) {
        if (resourceMap != null && resourceMap.containsKey(resource)) {
            return resourceMap.get(resource);
        }
        resourceMap = getResourceList().stream()
                .collect(Collectors.toMap(v -> FileUtils.getFileName(v.getResourceName()), v -> v.getResourceName().replace("file:","")));
        if (!resourceMap.containsKey(resource)) {
            throw new IllegalArgumentException("resource " + resource + " not found");
        }
        return resourceMap.get(resource);
    }

    @Override
    public List<ResourceInfo> getResourceFilesList() {
        return resourceList;
    }

}
