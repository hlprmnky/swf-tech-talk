package com.hlprmnky.widgco.common.exceptions;

import javax.ws.rs.NotFoundException;

public class WidgetNotFoundException extends NotFoundException {
    public WidgetNotFoundException(String message) {
        super(message);
    }
    public WidgetNotFoundException(Throwable t) {
        super(t);
    }
}
