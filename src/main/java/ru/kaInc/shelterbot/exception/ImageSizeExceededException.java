package ru.kaInc.shelterbot.exception;

public class ImageSizeExceededException extends Exception {
    public ImageSizeExceededException(String message, long i) {
        super(message);
    }
}