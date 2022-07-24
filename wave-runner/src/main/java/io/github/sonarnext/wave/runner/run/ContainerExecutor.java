package io.github.sonarnext.wave.runner.run;

import io.github.sonarnext.wave.runner.config.ActionConfig;

public interface ContainerExecutor extends Executor {

    void init(ActionConfig actionConfig);

    String getImageId();

}
