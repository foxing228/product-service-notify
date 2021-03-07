package com.dima.productservice.service;

import com.dima.productservice.domain.Product;
import com.dima.productservice.domain.User;

import java.util.List;

public interface BackToStockService {
    void subscribe(Product product, User user);

    List<User> subscribedUsers(Product product);
}
