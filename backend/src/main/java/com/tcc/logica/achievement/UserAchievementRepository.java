package com.tcc.logica.achievement;

import com.tcc.logica.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {

    @Query("select ua from UserAchievement ua join fetch ua.achievement where ua.user = :user")
    List<UserAchievement> findByUser(@Param("user") AppUser user);

    boolean existsByUserAndAchievement(AppUser user, Achievement achievement);
}
