package com.example.pets.service;

import com.example.pets.domain.Role;
import com.example.pets.domain.User;
import com.example.pets.repos.UserRepo;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/*
Need to rewrite the tests without spring context
 */

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userSevice;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private MailSender mailSender;

    @Test
    public void addUser() {
        User user = new User();

        user.setEmail("some@mail.ru");

        boolean isUserCreated = userSevice.addUser(user);

        assertTrue(isUserCreated);
        assertNotNull(user.getActivationCode());
        assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1))
                .send(
                        ArgumentMatchers.eq(user.getEmail()),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }

    @Test
    public void addUserFailTest() {
        User user = new User();

        user.setUsername("John");

        Mockito.doReturn(new User())
                .when(userRepo)
                .findByUsername("John");

        boolean isUserCreated = userSevice.addUser(user);

        assertFalse(isUserCreated);

        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailSender, Mockito.times(0))
                .send(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }

    @Test
    public void activateUser() {
        User user = new User();

        user.setActivationCode("bingo!");

        Mockito.doReturn(user)
                .when(userRepo)
                .findByActivationCode("activate");

        boolean isUserActivated = userSevice.activateUser("activate");

        assertTrue(isUserActivated);
        assertNull(user.getActivationCode());

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    @Test
    public void activateUserFailTest() {
        boolean isUserActivated = userSevice.activateUser("activate me");

        assertFalse(isUserActivated);

        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }
}