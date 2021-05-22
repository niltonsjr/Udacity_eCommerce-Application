package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    private ModifyCartRequest modifyCartRequest;

    private String correctUsername = "UsernameTest";
    private String incorrectUsername = "incorrectUsername";
    private String password = "PasswordTest";
    private String itemName = "itemTest";

    private long correctItemId = 1L;
    private long incorrectItemId = 2L;

    public CartControllerTest() {
    }

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjet(cartController, "userRepository", userRepository);
        TestUtils.injectObjet(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjet(cartController, "itemRepository", itemRepository);

        modifyCartRequest = new ModifyCartRequest();

        User user = new User();
        user.setUsername(correctUsername);
        user.setPassword(password);

        Cart cart = new Cart();

        List<Item> itemList = new ArrayList<>();

        Item itemTest = new Item();
        itemTest.setName(itemName);
        itemTest.setPrice(BigDecimal.valueOf(1.5));
        itemTest.setDescription("An test item.");
        itemList.add(itemTest);

        cart.setItems(itemList);
        cart.setTotal(BigDecimal.valueOf(1.5));
        user.setCart(cart);
        cart.setUser(user);

        when(userRepository.findByUsername(correctUsername)).thenReturn(user);
        when(userRepository.findByUsername(incorrectUsername)).thenReturn(null);

        when(itemRepository.findById(correctItemId)).thenReturn(Optional.of(itemTest));
        when(itemRepository.findById(incorrectItemId)).thenReturn(Optional.empty());
    }

    @Test
    public void testAddToCartHappyPath() {
        modifyCartRequest.setUsername(correctUsername);
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cartResponse = response.getBody();
        assertNotNull(cartResponse);
        assertEquals(correctUsername, cartResponse.getUser().getUsername());
        assertEquals(itemName, cartResponse.getItems().get(0).getName());
    }

    @Test
    public void testAddToCartInvalidUserNameShouldReturnNotFoundResponse() {
        modifyCartRequest.setUsername(incorrectUsername);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testAddToCartInvalidItemIdShouldReturnNotFoundResponse() {
        modifyCartRequest.setUsername(correctUsername);
        modifyCartRequest.setItemId(incorrectItemId);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testRemoveFromCartHappyPath() {
        modifyCartRequest.setUsername(correctUsername);
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cartResponse = response.getBody();
        assertNotNull(cartResponse);
        assertEquals(correctUsername, cartResponse.getUser().getUsername());
        assertTrue(cartResponse.getItems().isEmpty());
    }

}
