package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    private User user;

    @Before
    public void setUp() {
        orderController = new OrderController();

        TestUtils.injectObjet(orderController, "userRepository", userRepository);
        TestUtils.injectObjet(orderController, "orderRepository", orderRepository);

        user = new User();
        user.setUsername("TestUsername");
        user.setPassword("password");

        Cart cart = new Cart();

        List<Item> itemList = new ArrayList<>();

        Item itemTest = new Item();
        itemTest.setName("ItemName");
        itemTest.setPrice(BigDecimal.valueOf(1.5));
        itemTest.setDescription("An test item.");
        itemList.add(itemTest);

        cart.setItems(itemList);
        cart.setTotal(BigDecimal.valueOf(1.5));
        user.setCart(cart);
        cart.setUser(user);

        List<UserOrder> userOrders = Lists.list(UserOrder.createFromCart(user.getCart()));

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn((List<UserOrder>) userOrders);
    }

    @Test
    public void testSubmitOrderHappyPath() {
        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder responseOrder = response.getBody();

        assertNotNull(responseOrder);
        assertEquals(user.getUsername(), responseOrder.getUser().getUsername());
        assertEquals(1, responseOrder.getItems().size());
        assertEquals(user.getCart().getTotal(), responseOrder.getTotal());
    }

    @Test
    public void testSubmitOrderInvalidUserShouldReturnNotFound() {
        ResponseEntity<UserOrder> response = orderController.submit("InvalidUserName");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testGetOrdersForUserShouldReturnAUserOrderList() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> responseOrderList = response.getBody();

        assertEquals(1, responseOrderList.size());
        assertEquals(user.getUsername(), responseOrderList.get(0).getUser().getUsername());
        assertEquals(1, responseOrderList.get(0).getItems().size());
        assertEquals(user.getCart().getTotal(), responseOrderList.get(0).getTotal());
    }

    @Test
    public void testGetOrdersForInvalidUserShouldReturnNotFound() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("InvalidUserName");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}
