package ru.akimov.spring.security.securityApp.service;


import ru.akimov.spring.security.securityApp.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    void saveUser(User user, String[] roles);

    User getUserById(int id);

    void updateUser(User user, String[] roles);

    void deleteUserById(int id);

    int countAdmins();
}