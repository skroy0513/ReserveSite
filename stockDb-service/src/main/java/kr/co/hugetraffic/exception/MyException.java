package kr.co.hugetraffic.exception;

public class MyException extends RuntimeException{

    public MyException(String message) {
        super(message);
    }

    public MyException(String message, Throwable cause) {
        super(message, cause);
    }
}