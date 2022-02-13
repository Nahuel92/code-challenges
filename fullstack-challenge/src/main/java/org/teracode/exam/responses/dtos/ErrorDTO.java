package org.teracode.exam.responses.dtos;

public class ErrorDTO {
    private final String error;

    public ErrorDTO(final String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
