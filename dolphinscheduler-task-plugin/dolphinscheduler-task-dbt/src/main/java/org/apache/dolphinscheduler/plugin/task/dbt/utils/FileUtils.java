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

package org.apache.dolphinscheduler.plugin.task.dbt.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import static org.apache.dolphinscheduler.plugin.task.api.TaskConstants.RWXR_XR_X;

@Slf4j
public class FileUtils {

    private FileUtils() {
    }

    public static String getFileName(String path) {
        return new File(path).getName();
    }

    public static void writeConfigToFile(String scriptFileFullPath, String content) {
        File scriptFile = new File(scriptFileFullPath);
        Path path = scriptFile.toPath();
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new RuntimeException(String
                        .format("Flink Script file exists in path: %s before creation and cannot be deleted", path), e);
            }
        }

        Set<PosixFilePermission> perms = PosixFilePermissions.fromString(RWXR_XR_X);
        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
        try {
            if (SystemUtils.IS_OS_WINDOWS) {
                Files.createFile(path);
            } else {
                if (!scriptFile.getParentFile().exists()) {
                    scriptFile.getParentFile().mkdirs();
                }
                Files.createFile(path, attr);
            }

            if (StringUtils.isNotEmpty(content)) {
                String replacedScript = content.replaceAll("\\r\\n", "\n");
                FileUtils.writeStringToFile(scriptFile, replacedScript, StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            throw new RuntimeException("Generate flink SQL script error", e);
        }
    }

    private static void writeStringToFile(File file, String content, StandardOpenOption standardOpenOption) {
        try {
            log.info("Writing content: " + content);
            log.info("To file: " + file.getAbsolutePath());
            Files.write(file.getAbsoluteFile().toPath(), content.getBytes(StandardCharsets.UTF_8), standardOpenOption);
        } catch (IOException e) {
            throw new RuntimeException("Error writing file: " + file.getAbsoluteFile(), e);
        }
    }
}
