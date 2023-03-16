package ir.maktab.finalprojectphase3.data.dao;

import ir.maktab.finalprojectphase3.data.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminDao extends JpaRepository<Admin, Long> {
    boolean existsByUsername(String username);

    Optional<Admin> findByUsername(String username);
}

