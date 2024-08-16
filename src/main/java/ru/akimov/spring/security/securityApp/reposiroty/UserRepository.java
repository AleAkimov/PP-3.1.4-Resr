package ru.akimov.spring.security.securityApp.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.akimov.spring.security.securityApp.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
}