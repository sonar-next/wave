package io.github.sonarnext.wave.runner.command;

import java.io.IOException;

public class CommandExitException extends IOException {

    private String result;

    public CommandExitException() {
        super();
    }

    public CommandExitException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandExitException(String message) {
        super(message);
    }

    public CommandExitException(String message, String result) {
        super(message);
        this.result = result;
    }

    public CommandExitException(Throwable cause) {
        super(cause);
    }

    public String getResult() {
        return result;
    }
}
