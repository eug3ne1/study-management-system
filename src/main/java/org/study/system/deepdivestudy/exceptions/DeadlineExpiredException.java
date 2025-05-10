package org.study.system.deepdivestudy.exceptions;

public class DeadlineExpiredException extends RuntimeException {
    public DeadlineExpiredException(String message) {
        super(message);
    }
}
