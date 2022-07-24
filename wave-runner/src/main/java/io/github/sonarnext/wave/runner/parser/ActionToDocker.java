/*
 * Copyright 2021 magaofei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.sonarnext.wave.runner.parser;

import io.github.sonarnext.wave.runner.config.ActionConfig;
import io.github.sonarnext.wave.runner.config.DockerConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author magaofei
 * @date 2021/2/6
 */
public class ActionToDocker {

    public static DockerConfig convert(ActionConfig actionConfig) {
        DockerConfig dockerConfig = new DockerConfig();
        Set<String> tag = new HashSet<>();

        dockerConfig.setTag(tag);
        for (Map.Entry<String, ActionConfig.Job> jobEntry : actionConfig.getJobs().entrySet()) {
            String buildName = jobEntry.getKey();
            tag.add(buildName);

            ActionConfig.Job job = jobEntry.getValue();
            if (job.getSteps().isEmpty()) {
                return dockerConfig;
            }
            dockerConfig.setImage(job.getRunsOn());

            for (ActionConfig.Step step : job.getSteps()) {
                dockerConfig.getRuns().addAll((step.getRun()));
            }

            dockerConfig.setVolume(job.getVolumes());
            dockerConfig.setEnv(job.getEnvironment());
        }

        dockerConfigConvertToDockerfile(dockerConfig);
        return dockerConfig;
    }

    public static void dockerConfigConvertToDockerfile(DockerConfig dockerConfig) {
        List<String> runs = dockerConfig.getRuns();
        String cmd = String.join(" && ", runs);
        String dockerfile = String.format(
                "FROM %s\n" +
                "WORKDIR %s\n" +
                "CMD /bin/sh -c \" %s \"", dockerConfig.getImage(), dockerConfig.getEnv().get("workDir"), cmd);
        dockerConfig.setDockerfile(dockerfile);
    }
}
