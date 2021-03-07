package com.dima.productservice.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Product {
    private Long id;
    private String name;
    private ProductCategory category;
}
