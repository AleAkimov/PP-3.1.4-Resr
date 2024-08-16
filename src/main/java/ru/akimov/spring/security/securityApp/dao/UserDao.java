package ru.akimov.spring.security.securityApp.dao;

import ru.akimov.spring.security.securityApp.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();

    void saveUser(User user);

    User getUserById(int id);

    void updateUser(User user);

    void deleteUserById(int id);

    List<User> findByUsername(String username, Class<User> clazz);
}