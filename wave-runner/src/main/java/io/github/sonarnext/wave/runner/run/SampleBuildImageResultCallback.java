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
package io.github.sonarnext.wave.runner.run;


import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.model.BuildResponseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author magaofei
 * @date 2022/3/6
 */
public class SampleBuildImageResultCallback extends BuildImageResultCallback {

    private static final Logger logger = LoggerFactory.getLogger(SampleBuildImageResultCallback.class);

    @Override
    public void onNext(BuildResponseItem item) {
        if (item.getStream() != null) {
            logger.info("{}", item.getStream());
        }
        super.onNext(item);
    }

    @Override
    public void onComplete() {
        logger.info("build image finish");
        super.onComplete();
    }
}
