package com.nanoka.warehouse.Model.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nanoka.warehouse.Model.Enum.MovementType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movements")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Movement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    Integer quantity;

    @Column(precision = 10, scale = 2)
    BigDecimal price;

    @Enumerated(EnumType.STRING)
    MovementType movementType;

    LocalDateTime date;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
