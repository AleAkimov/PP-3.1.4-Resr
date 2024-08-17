package ru.akimov.spring.security.securityApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.akimov.spring.security.securityApp.dao.UserDaoImpl;
import ru.akimov.spring.security.securityApp.exeption.RegistrationWrongUsernameException;
import ru.akimov.spring.security.securityApp.exeption.RegistrationWrongUsernameResponse;
import ru.akimov.spring.security.securityApp.exeption.UserAlreadyExistsException;
import ru.akimov.spring.security.securityApp.model.Role;
import ru.akimov.spring.security.securityApp.model.User;
import ru.akimov.spring.security.securityApp.service.RoleService;
import ru.akimov.spring.security.securityApp.service.UserService;

import java.util.List;


@RestController
@RequestMapping("/admin/api")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final RoleService roleService;
    private final UserDaoImpl userDaoImpl;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, UserDaoImpl userDaoImpl) {
        this.userService = userService;
        this.roleService = roleService;
        this.userDaoImpl = userDaoImpl;
    }

    @GetMapping
    public ResponseEntity<List<User>> showAllUsers() {
        List<User> userList = userService.getAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    //+
    @PostMapping("/new")
    public ResponseEntity<HttpStatus> addUser(@RequestBody User user) {
        String[] roles = user.getRoles().stream()
                .map(Role::getRole)
                .toArray(String[]::new);
        try {
            userService.saveUser(user, roles);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(HttpStatus.valueOf("User with this email already exists.")); // Конфликт, пользователь уже существует.
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(HttpStatus.valueOf("An error occurred while adding the user.")); // Возвращаем ошибку сервера.
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> showAllRoles() {
        List<Role> roleSet = roleService.getAllRoles();
        return new ResponseEntity<>(roleSet, HttpStatus.OK);
    }

    //+
    @PatchMapping("/edit/{id}")
    public ResponseEntity<HttpStatus> updateUser(
            @RequestBody User user,
            @PathVariable("id") int id) {
        String[] roles = user.getRoles().stream()
                .map(Role::getRole)
                .toArray(String[]::new);

        userService.updateUser(user, roles);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //+
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/admin-count")
    public ResponseEntity<Integer> countAdmins() {
        int adminCount = userService.countAdmins(); // Вам нужно создать метод countAdmins в UserService
        return new ResponseEntity<>(adminCount, HttpStatus.OK);
    }

    @GetMapping("/auth")
    public ResponseEntity<User> showUserPage(Authentication auth) {
        String email = auth.getName();
        User user = userDaoImpl.getUserByEmail(email);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler
    private ResponseEntity<RegistrationWrongUsernameResponse> exceptionHandler(RegistrationWrongUsernameException e) {
        return new ResponseEntity<>(new RegistrationWrongUsernameResponse("Username already exists"),
                HttpStatus.NOT_ACCEPTABLE);
    }
}