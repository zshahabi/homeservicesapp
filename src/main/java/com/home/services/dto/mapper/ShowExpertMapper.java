package com.home.services.dto.mapper;

import com.home.services.data.entity.User;
import com.home.services.dto.DTOShowExperts;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public final class ShowExpertMapper
{

    public List<DTOShowExperts> toDtoShowExperts(final List<User> users)
    {
        List<DTOShowExperts> dtoShowExperts = new ArrayList<>();
        if (users != null && users.size() > 0)
        {
            for (final User user : users)
            {
                final DTOShowExperts dtoShowExpert = new DTOShowExperts();
                dtoShowExpert.setId(user.getId());
                dtoShowExpert.setEmail(user.getEmail());

                dtoShowExperts.add(dtoShowExpert);
            }
        }

        return dtoShowExperts;
    }
}
