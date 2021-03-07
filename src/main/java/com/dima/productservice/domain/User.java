package com.dima.productservice.domain;

import com.dima.productservice.service.BackToStockService;
import lombok.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {
    private String name;
    private Boolean premium;
    private Integer age;

    private List<Product> products = new ArrayList<>();

}
