package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjet(userController, "userRepository", userRepository);
        TestUtils.injectObjet(userController, "cartRepository", cartRepository);
        TestUtils.injectObjet(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserHappyPath() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        CreateUserRequest cur = new CreateUserRequest();
        cur.setUsername("test");
        cur.setPassword("testPassword");
        cur.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(cur);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void testCreateUserPasswordLengthLessThanSevenCharactersReturnsBadRequest(){
        CreateUserRequest cur = new CreateUserRequest();
        cur.setUsername("test");
        cur.setPassword("pass");
        cur.setConfirmPassword("pass");

        final ResponseEntity<User> response = userController.createUser(cur);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testCreateUserPasswordIsDifferentOfConfirmPasswordReturnsBadRequest(){
        CreateUserRequest cur = new CreateUserRequest();
        cur.setUsername("test");
        cur.setPassword("pass");
        cur.setConfirmPassword("password");

        final ResponseEntity<User> response = userController.createUser(cur);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testFindUserByUsernameReturnUserWhenUserExists(){
        User userCreated = new User();
        userCreated.setUsername("test");
        userCreated.setPassword("password");

        when(userRepository.findByUsername(anyString())).thenReturn(userCreated);

        final ResponseEntity<User> response = userController.findByUserName("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        assertEquals(User.class, response.getBody().getClass());
        assertEquals("test", userCreated.getUsername());
        assertEquals("password", userCreated.getPassword());
    }

    @Test
    public void testFindUserByIdReturnUserWhenUserExists(){
        User userCreated = new User();
        userCreated.setUsername("test");
        userCreated.setPassword("password");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userCreated));

        final ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        assertEquals(User.class, response.getBody().getClass());
        assertEquals("test", userCreated.getUsername());
        assertEquals("password", userCreated.getPassword());
    }
}
