package com.nanoka.warehouse.Dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nanoka.warehouse.Model.Entity.Product;
import com.nanoka.warehouse.Model.Enum.MovementType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovementDto {
    Long id;
    Product product;
    Integer quantity;
    BigDecimal price;
    LocalDateTime date;
    MovementType type;
    UserDto user;
}
