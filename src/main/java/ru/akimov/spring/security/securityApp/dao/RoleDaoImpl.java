package ru.akimov.spring.security.securityApp.dao;


import org.springframework.stereotype.Repository;
import ru.akimov.spring.security.securityApp.model.Role;
import ru.akimov.spring.security.securityApp.reposiroty.RoleRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {

    private final RoleRepository roleRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public RoleDaoImpl(EntityManager entityManager, RoleRepository roleRepository) {
        this.entityManager = entityManager;
        this.roleRepository = roleRepository;
    }

    @Override
    public void saveRole(Role role) {
        entityManager.persist(role);
    }

    @Override
    public List<Role> getAllRoles() {
        return entityManager.createQuery("select r FROM Role r", Role.class).getResultList();
    }

    public Role findByRoleName(String role) {
        return roleRepository.findByRole(role);
    }
}
