package ru.akimov.spring.security.securityApp.model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.akimov.spring.security.securityApp.validation.OnCreate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @NotBlank(message = "Поле не может быть пустым")
    @Column(name = "email")
    @NotBlank(message = "Поле не может быть пустым")
    private String email;
    @NotBlank(message = "Поле не может быть пустым")
    @Column(name = "name")
    private String name;
    @NotBlank(message = "Поле не может быть пустым")
    @Column(name = "company")
    private String company;
    @NotBlank(message = "Поле не может быть пустым", groups = OnCreate.class)
    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))

    private Set<Role> roles;

    public User() {

    }

    public User(int id, String name, String company) {
        this.id = id;
        this.name = name;
        this.company = company;
    }

    public User(String email, String name, String company, String password) {
        this.email = email;
        this.name = name;
        this.company = company;
        this.password = password;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getShortRoles() {
        if (roles.toString().equals("[ROLE_USER]")) {
            return "USER";
        } else if (roles.toString().equals("[ROLE_ADMIN]")) {
            return "ADMIN";
        }
        return "ADMIN USER";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User user = (User) obj;
        return (this.getId() == user.getId());
    }

    @Override
    public int hashCode() {
        return 1 + 29 * getId();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", company='" + company + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}