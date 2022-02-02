package com.homeservices.service;

import com.homeservices.data.entity.Address;
import com.homeservices.data.entity.Admin;
import com.homeservices.data.enums.UserStatus;
import com.homeservices.data.repository.AdminRepository;
import com.homeservices.dto.DTORegister;
import org.springframework.stereotype.Service;

@Service
public record AdminService(AdminRepository repository , AddressService addressService)
{

    public boolean register(DTORegister dtoRegister)
    {
        try
        {
            Address address = addressService.addAddress(dtoRegister.getAddress());

            Admin user = new Admin();
            user.setName(dtoRegister.getName());
            user.setFamily(dtoRegister.getFamily());
            user.setEmail(dtoRegister.getEmail());
            user.setPassword(dtoRegister.getPassword());
            user.setUserStatus(UserStatus.valueOf(dtoRegister.getUserStatus()));
            user.setAccountCredit(dtoRegister.getAccountCredit());
            user.setAddress(address);

            user = repository.save(user);

            return (user.getId() > 0);
        }
        catch (Exception ignored)
        {
        }

        return false;
    }
}
