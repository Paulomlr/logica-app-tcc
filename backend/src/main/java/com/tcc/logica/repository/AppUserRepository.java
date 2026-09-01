package com.tcc.logica.repository;

import com.tcc.logica.entity.AppUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByGoogleSub(String googleSub);

    Optional<AppUser> findByEmail(String email);
}
