package ru.akimov.spring.security.securityApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.akimov.spring.security.securityApp.model.User;
import ru.akimov.spring.security.securityApp.service.RoleService;
import ru.akimov.spring.security.securityApp.service.UserService;
import ru.akimov.spring.security.securityApp.validation.OnUpdate;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getAllUsers(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("role", roleService.getAllRoles());
        return "users";
    }

    @GetMapping("/new")
    public String getAddNewUserPage(@AuthenticationPrincipal User user, Model model) {
        User newUser = new User();
        model.addAttribute("user", user);
        model.addAttribute("newUser", newUser);
        model.addAttribute("role", roleService.getAllRoles());
        return "new";
    }

    @PostMapping("/saveUser")
    public String addNewUser(@ModelAttribute("user") User user,
                             BindingResult bindingResult,
                             @RequestParam(value = "select_role", required = false) String[] roles) {
        if (bindingResult.hasErrors()) {

            return "new";
        }
        userService.saveUser(user, roles);
        return "redirect:/admin";
    }

@PutMapping("/edit/{id}")
public String updateUser(@PathVariable("id") int id,
                         @Validated(OnUpdate.class) @ModelAttribute("user") User user,
                         BindingResult bindingResult,
                         @RequestParam(value = "select_role", required = false) String[] roles,
                         RedirectAttributes redirectAttributes) {


    if (bindingResult.hasErrors()) {
        redirectAttributes.addFlashAttribute("error", "Пожалуйста, исправьте ошибки.");
        return "redirect:/admin/users/edit/" + id;
    }


    User existingUser = userService.getUserById(id);
    existingUser.setEmail(user.getEmail());
    existingUser.setName(user.getName());
    existingUser.setCompany(user.getCompany());

    if (roles == null || roles.length == 0) {
        redirectAttributes.addFlashAttribute("error", "Пожалуйста, выберите хотя бы одну роль.");
        return "redirect:/admin/users/edit/" + id;
    }


    userService.updateUser(existingUser, roles);
    redirectAttributes.addFlashAttribute("success", "Пользователь успешно обновлен!");
    return "redirect:/admin";
}

    @DeleteMapping(value = "/deleteUser/{id}")
    public String deleteUserById(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

}