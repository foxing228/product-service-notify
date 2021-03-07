package com.dima.productservice.service;

import com.dima.productservice.domain.Product;
import com.dima.productservice.domain.ProductCategory;
import com.dima.productservice.domain.User;
import com.dima.productservice.exception.NullEntityException;
import com.dima.productservice.exception.UserSubscribeException;
import jdk.jfr.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Service
public class BackToStockServiceImpl implements BackToStockService {

    private final Map<Product, List<User>> productSubscriptions = new HashMap<>();
    private static final Integer HIGHEST_AGE = 70;

    @Override
    public void subscribe(Product product, User user) {
        if (Objects.isNull(product)  || Objects.isNull(user)) throw new NullEntityException("Product or user is null!");

        if(productSubscriptions.containsKey(product)) {
            List<User> productUsers = productSubscriptions.get(product);
            if(productUsers.contains(user)) {
                throw new UserSubscribeException("User already subscribed");
            }
            productUsers.add(user);
        }
        productSubscriptions.put(product, new ArrayList<>(List.of(user)));
    }

    @Override
    public List<User> subscribedUsers(Product product) {
        return  Objects.nonNull(productSubscriptions.get(product)) ? productSubscriptions.get(product) : Collections.emptyList();
    }

    public List<User> findUsersWithHighestPriority(Product product) {
        if (Objects.isNull(product)) throw new NullEntityException("Product is null");
        if (!productSubscriptions.containsKey(product)) throw new RuntimeException("Product does not exist");
        return productSubscriptions.get(product).stream()
                .filter(u -> u.getPremium().equals(true) || (product.getCategory().equals(ProductCategory.MEDICAL) && u.getAge() > HIGHEST_AGE))
                .collect(Collectors.toList());
    }

}
