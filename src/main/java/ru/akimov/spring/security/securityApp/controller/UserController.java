package ru.akimov.spring.security.securityApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.akimov.spring.security.securityApp.dao.UserDao;
import ru.akimov.spring.security.securityApp.dao.UserDaoImpl;
import ru.akimov.spring.security.securityApp.model.User;
import ru.akimov.spring.security.securityApp.service.UserService;

@RestController
@RequestMapping("/user/api")
public class UserController {
    private final UserDaoImpl userDaoImpl;

    @Autowired
    public UserController(UserDaoImpl userDaoImpl) {
        this.userDaoImpl = userDaoImpl;
    }
//    @GetMapping
//    public String getUserPage(@AuthenticationPrincipal User user, Model model) {
//        model.addAttribute("user", user);
//        return "user";
//    }
    @GetMapping
    public ResponseEntity<User> showUserPage(Authentication auth) {
        User user = userDaoImpl.getUserByEmail(auth.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}