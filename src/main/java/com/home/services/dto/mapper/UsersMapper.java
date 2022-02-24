package com.home.services.dto.mapper;

import com.home.services.data.entity.Address;
import com.home.services.data.entity.User;
import com.home.services.data.enums.Roles;
import com.home.services.dto.DTOUsers;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public final class UsersMapper
{
    public List<DTOUsers> toDtoUsers(final List<User> users)
    {
        final List<DTOUsers> dtoUsers = new ArrayList<>();

        for (final User user : users) dtoUsers.add(toDtoUser(user));

        return dtoUsers;
    }

    public DTOUsers toDtoUser(final User user)
    {
        final DTOUsers dtoUser = new DTOUsers();
        dtoUser.setId(user.getId());
        dtoUser.setName(user.getName());
        dtoUser.setFamily(user.getFamily());
        dtoUser.setEmail(user.getEmail());
        dtoUser.setStatus(user.getUserStatus().name());

        final Address address = user.getAddress();

        dtoUser.setAlley((address != null) ? address.getAlley() : "");
        dtoUser.setStreet((address != null) ? address.getStreet() : "");
        dtoUser.setPostalCode((address != null) ? String.valueOf(address.getPostalCode()) : "");

        dtoUser.setAccountCredit(user.getAccountCredit());
        dtoUser.setCreatedAt(user.getCreatedAt().toString());

        final Roles role = user.getRoles().get(0);
        if (role.equals(Roles.EXPERT))
        {
            dtoUser.setRating(user.getRating());
            dtoUser.setSubService("");
        }

        return dtoUser;
    }
}
