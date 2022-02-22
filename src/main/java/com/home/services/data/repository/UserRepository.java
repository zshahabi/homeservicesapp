package com.home.services.data.repository;

import com.home.services.data.entity.User;
import com.home.services.data.enums.Roles;
import com.home.services.data.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    User findByEmail(final String email);

    User findById(final long id);

    List<User> findByRolesContains(final Roles role);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying
    @Query("update User user set user.userStatus = :STATUS where user.id = :USER_ID")
    int changeUserStatus(@Param("STATUS") final UserStatus userStatus , @Param("USER_ID") final long userId);
}
