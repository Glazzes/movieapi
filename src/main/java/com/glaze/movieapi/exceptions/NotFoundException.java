package com.glaze.movieapi.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundException extends RuntimeException {
    private final String messageKey;
    private final Object[] args;
    public NotFoundException(String messageKey) {
        this.messageKey = messageKey;
        this.args = new String[]{};
    }

    public NotFoundException(String messageKey, Object... args) {
        this.messageKey = messageKey;
        this.args = args;
    }
}
