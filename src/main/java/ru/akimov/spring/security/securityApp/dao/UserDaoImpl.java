package ru.akimov.spring.security.securityApp.dao;


import org.springframework.stereotype.Repository;
import ru.akimov.spring.security.securityApp.model.User;
import ru.akimov.spring.security.securityApp.reposiroty.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private final UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public UserDaoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("FROM User", User.class).getResultList();
    }

    @Override
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public User getUserById(int id) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new EntityNotFoundException("User not found for ID: " + id);
        }
        return entityManager.find(User.class, id);

    }

    @Override
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void deleteUserById(int id) {
        entityManager.remove(getUserById(id));
    }

    @Override
    public List findByUsername(String email, Class<User> clazz) {
        return entityManager.createQuery("select u from " + clazz.getSimpleName() + " u join fetch u.roles where u.email =:email", clazz)
                .setParameter("email", email)
                .getResultList();
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}

