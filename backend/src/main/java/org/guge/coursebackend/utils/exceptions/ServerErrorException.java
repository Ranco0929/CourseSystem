package org.guge.coursebackend.utils.exceptions;

public class ServerErrorException extends Exception {

    public ServerErrorException() {
        super();
    }

    public ServerErrorException(String s) {
        super(s);
    }

    public ServerErrorException(String s, Throwable t) {
        super(s, t);
    }
}
