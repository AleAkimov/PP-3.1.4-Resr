package ru.akimov.spring.security.securityApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.akimov.spring.security.securityApp.dao.UserDao;
import ru.akimov.spring.security.securityApp.exeption.UserAlreadyExistsException;
import ru.akimov.spring.security.securityApp.model.Role;
import ru.akimov.spring.security.securityApp.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserDao userDao, @Lazy PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public void saveUser(User user, String[] roles) {
        if (!userDao.findByUsername(user.getEmail(), User.class).isEmpty()) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        Set<Role> role = new HashSet<>();
        role.add(roleService.getAllRoles().get(1));
        for (String s : roles) {
            if (s.equals("ROLE_ADMIN")) {
                role.add(roleService.getAllRoles().get(0));
            }
        }
        user.setRoles(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.saveUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(int id) {
        return userDao.getUserById(id);
    }


    @Override
    public void updateUser(User user, String[] roles) {
        User existingUser = userDao.getUserById(user.getId());
        Set<Role> role = new HashSet<>();
        role.add(roleService.getAllRoles().get(1));
        for (String s : roles) {
            if (s.equals("ROLE_ADMIN")) {
                role.add(roleService.getAllRoles().get(0));
            }
        }
        user.setRoles(role);
        if (!user.getPassword().equals(existingUser.getPassword()) && user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(existingUser.getPassword());
        }

        userDao.updateUser(user);

    }


    @Override
    public void deleteUserById(int id) {
        userDao.deleteUserById(id);
    }
}

