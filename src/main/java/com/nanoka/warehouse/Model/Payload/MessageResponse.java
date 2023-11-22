package com.nanoka.warehouse.Model.Payload;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class MessageResponse implements Serializable {
    private String message;
    private Boolean error;
    private Object data;
}
