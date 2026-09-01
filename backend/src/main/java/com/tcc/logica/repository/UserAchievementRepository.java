package com.tcc.logica.repository;

import com.tcc.logica.entity.Achievement;
import com.tcc.logica.entity.AppUser;
import com.tcc.logica.entity.UserAchievement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {

    @Query("select ua from UserAchievement ua join fetch ua.achievement where ua.user = :user")
    List<UserAchievement> findByUser(@Param("user") AppUser user);

    boolean existsByUserAndAchievement(AppUser user, Achievement achievement);
}
