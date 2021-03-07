package com.dima.productservice.service;

import com.dima.productservice.domain.Product;
import com.dima.productservice.domain.ProductCategory;
import com.dima.productservice.domain.User;
import com.dima.productservice.exception.NullEntityException;
import com.dima.productservice.exception.UserSubscribeException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
public class BackToStockServiceImplTest {

    @InjectMocks
    private BackToStockServiceImpl backToStockService;

    private Product product;
    private User user;
    private User user2;

    @Before
    public void createTestEntities() {
        this.product = new Product(1L, "Java for professional", ProductCategory.BOOKS);
        this.user = new User("Dima", true, 19, "someemail@email.com");
        this.user2 = new User("Kirill", false, 20, "someemail@email.com");
    }


    @Test(expected = NullEntityException.class )
    public void testSubscribeIfProductOrUserIsNull() {
        product = null;
        user = null;
        backToStockService.subscribe(product, user);
    }

    @Test(expected = UserSubscribeException.class)
    public void testSubscribeIfUserAlreadySubscribed() {
        backToStockService.subscribe(product, user);
        backToStockService.subscribe(product, user);
    }

    @Test
    public void testSubscribeIfUserNotSubscribed() {
        backToStockService.subscribe(product, user);

        assertEquals(1, backToStockService.getProductSubscriptions().size());
        assertTrue(backToStockService.getProductSubscriptions().get(product).contains(user));

        backToStockService.subscribe(product, user2);

        assertTrue(backToStockService.getProductSubscriptions().get(product).contains(user2));
        assertEquals(backToStockService.getProductSubscriptions().get(product).size(), 2);
    }

    @Test
    public void testSubscribedUsers() {
        backToStockService.subscribe(product, user);

        assertEquals(backToStockService.subscribedUsers(product).size(), 1);

        backToStockService.subscribe(product, user2);

        assertEquals(backToStockService.subscribedUsers(product).size(), 2);
    }

    @Test
     public void getProductSubscriptions() {
        backToStockService.subscribe(product, user);
        backToStockService.subscribe(product, user2);
        assertEquals(backToStockService.getProductSubscriptions().size(), 1);
    }

    @Test
    public void testIsHighestPriorityUser() {
        backToStockService.subscribe(product, user);
        assertTrue(backToStockService.isHighestPriorityUser(user, product));
        assertFalse(backToStockService.isHighestPriorityUser(user2, product));
    }
}