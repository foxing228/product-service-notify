package com.dima.productservice.service;

import com.dima.productservice.domain.Product;
import com.dima.productservice.domain.ProductCategory;
import com.dima.productservice.domain.User;
import com.dima.productservice.exception.NullEntityException;
import com.dima.productservice.exception.UserSubscribeException;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Service
public class BackToStockServiceImpl implements BackToStockService {

    private final Map<Product, List<User>> productSubscriptions = new HashMap<>();
    private final MailSenderService mailSenderService;
    private final String notifyMessage = "Product %s now is available for you ";
    private final String notifySubject = "Product available";

    private static final Integer HIGHEST_AGE = 70;

    @Autowired
    public BackToStockServiceImpl(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }


    @Override
    public void subscribe(Product product, User user) {
        if (Objects.isNull(product)  || Objects.isNull(user)) throw new NullEntityException("Product or user is null!");

        if(productSubscriptions.containsKey(product)) {
            List<User> productUsers = productSubscriptions.get(product);
            if(productUsers.contains(user)) throw new UserSubscribeException("User already subscribed");
            else productUsers.add(user);
        }
        else {
            productSubscriptions.put(product, new ArrayList<>(List.of(user)));
        }
    }

    @Override
    public List<User> subscribedUsers(Product product) {
        return Objects.nonNull(productSubscriptions.get(product)) ? productSubscriptions.get(product) : Collections.emptyList();
    }

    public Boolean isHighestPriorityUser(User user, Product product) {
        if (user.getPremium() || (user.getAge() > HIGHEST_AGE && product.getCategory().equals(ProductCategory.MEDICAL))) return true;
        return false;
    }


    public void notifyUsers(Product product) {
        List<User> productUsers = subscribedUsers(product);
        List<User> highestPriorityUsers = productUsers.stream()
                .filter(u -> isHighestPriorityUser(u, product)).collect(Collectors.toList());

        highestPriorityUsers.stream()
                .forEach(u -> mailSenderService.sendMail(u.getEmail(), notifySubject, String.format(notifyMessage, product.getName())));

        productUsers.stream()
                .filter(u -> u.getAge()>HIGHEST_AGE)
                .forEach(u -> mailSenderService.sendMail(u.getEmail(), notifySubject, String.format(notifyMessage, product.getName())));

        productUsers.stream()
                .filter(u -> (!isHighestPriorityUser(u, product) && u.getAge()>HIGHEST_AGE))
                .forEach(u -> mailSenderService.sendMail(u.getEmail(), notifySubject, String.format(notifyMessage, product.getName())));
    }

}
