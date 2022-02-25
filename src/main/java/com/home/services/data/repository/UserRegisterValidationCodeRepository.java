package com.home.services.data.repository;

import com.home.services.data.entity.UserRegisterValidationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRegisterValidationCodeRepository extends JpaRepository<UserRegisterValidationCode, Long>
{
    @Query("select urvc from UserRegisterValidationCode urvc where urvc.code = :CODE and urvc.user.id = :USER_ID and urvc.expired = false")
    UserRegisterValidationCode getUserRegisterValidationCode(@Param("CODE") final long code , @Param("USER_ID") final long userId );
}
