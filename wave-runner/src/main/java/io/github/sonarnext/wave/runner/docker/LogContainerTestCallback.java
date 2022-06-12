package io.github.sonarnext.wave.runner.docker;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author magaofei
 */
public class LogContainerTestCallback extends ResultCallback.Adapter<Frame> {

    private static final Logger logger = LoggerFactory.getLogger(LogContainerTestCallback.class);
    protected final StringBuilder log = new StringBuilder();
    private final boolean printLog;


    public LogContainerTestCallback(boolean printLog) {
        this.printLog = printLog;
    }

    @Override
    public void onNext(Frame frame) {
        String payload = new String(frame.getPayload());
        if (printLog) {
            logger.info("{}", payload);
        }
        log.append(payload);
    }

    @Override
    public String toString() {
        return log.toString();
    }

}