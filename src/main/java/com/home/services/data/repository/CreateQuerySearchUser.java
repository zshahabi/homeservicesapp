package com.home.services.data.repository;

import com.home.services.data.enums.UserStatus;
import com.home.services.dto.DTOSearchUser;
import com.home.services.exception.InvalidUserStatusException;
import com.home.services.other.Str;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public final class CreateQuerySearchUser
{

    @PersistenceContext
    private final EntityManager entityManager;

    private CreateQuerySearchUser(final EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    public List<?> createQuery(final String entityName , final DTOSearchUser dtoSearchUser) throws InvalidUserStatusException
    {
        final UserStatus userStatus;

        if (Str.notEmpty(dtoSearchUser.getUserStatus()))
        {
            userStatus = UserStatus.nameToUserStatus(dtoSearchUser.getUserStatus());
            if (userStatus == null) throw new InvalidUserStatusException(dtoSearchUser.getUserStatus());
        }
        else userStatus = null;

        final StringBuilder strQuery = new StringBuilder("select varSelect from ").append(entityName).append(" varSelect ");

        boolean hasName = false, hasFamily = false, hasEmail = false, hasUserStatus = false, hasSubServiceName = false;
        boolean hasWhere = false;

        if (Str.notEmpty(dtoSearchUser.getName()))
        {
            strQuery.append("where ");
            hasWhere = true;
            hasName = true;
            strQuery.append(" varSelect.name = :NAME");
        }
        if (Str.notEmpty(dtoSearchUser.getFamily()))
        {
            if (hasWhere) strQuery.append(" or");
            else
            {
                strQuery.append("where ");
                hasWhere = true;
            }

            hasFamily = true;

            strQuery.append(" varSelect.family = :FAMILY");
        }
        if (Str.notEmpty(dtoSearchUser.getEmail()))
        {
            if (hasWhere) strQuery.append(" or");
            else
            {
                strQuery.append("where ");
                hasWhere = true;
            }

            hasEmail = true;

            strQuery.append(" varSelect.email = :EMAIL");
        }

        if (Str.notEmpty(dtoSearchUser.getUserStatus()))
        {
            if (hasWhere) strQuery.append(" or");

            hasUserStatus = true;

            strQuery.append(" varSelect.userStatus = :STATUS");
        }

        final Query query = entityManager.createQuery(strQuery.toString());

        if (hasName) query.setParameter("NAME" , dtoSearchUser.getName());
        if (hasFamily) query.setParameter("FAMILY" , dtoSearchUser.getFamily());
        if (hasEmail) query.setParameter("EMAIL" , dtoSearchUser.getEmail());
        if (hasUserStatus) query.setParameter("STATUS" , userStatus);

        return query.getResultList();
    }
}
