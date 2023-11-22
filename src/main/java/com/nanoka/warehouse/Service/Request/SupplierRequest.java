package com.nanoka.warehouse.Service.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequest {
    Long id;
    String name;
    String address;
    String telephone;
    String email;
}
