package com.nanoka.warehouse.Model.Payload;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder

public class ListResponse implements Serializable {
    private String message;
    private Boolean error;
    private List<Object> object;
}
