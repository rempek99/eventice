package com.zimnyciechan.eventice.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal server error occurred")
public class InternalServerException extends RuntimeException {

}
