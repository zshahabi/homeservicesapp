package com.home.services.service;

import com.home.services.data.entity.User;
import com.home.services.data.enums.Roles;
import com.home.services.data.enums.UserStatus;
import com.home.services.data.repository.CreateQuerySearchUser;
import com.home.services.data.repository.UserRepository;
import com.home.services.dto.DTOCustomerRegister;
import com.home.services.dto.DTOSearchUser;
import com.home.services.dto.mapper.AddressMapper;
import com.home.services.exception.FoundEmailException;
import com.home.services.exception.InvalidPasswordException;
import com.home.services.exception.InvalidPostalCodeException;
import com.home.services.exception.InvalidUserStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record CustomerService(UserRepository userRepository ,
                              CheckEmptyUserInfo checkEmptyUserInfo ,
                              AddressMapper addressMapper ,
                              CreateQuerySearchUser createQuerySearchUser)
{
    @Autowired
    public CustomerService
    {
    }

    public long register(final DTOCustomerRegister dtoCustomerRegister) throws InvalidPasswordException, NullPointerException, FoundEmailException, InvalidPostalCodeException
    {
        if (checkEmptyUserInfo.check(dtoCustomerRegister))
        {
            if (!hasEmail(dtoCustomerRegister.getEmail()))
            {
                User customer = new User();

                customer.getRoles().add(Roles.CUSTOMER);

                customer.setName(dtoCustomerRegister.getName());
                customer.setFamily(dtoCustomerRegister.getFamily());
                customer.setEmail(dtoCustomerRegister.getEmail());
                customer.setPassword(new BCryptPasswordEncoder().encode(dtoCustomerRegister.getPassword()));
                customer.setUserStatus(UserStatus.NEW_USER);
                customer.setAddress(addressMapper.toAddress(dtoCustomerRegister));

                customer = userRepository.save(customer);

                return customer.getId();
            }
            else throw new FoundEmailException(dtoCustomerRegister.getEmail());
        }
        return 0;
    }

    public List<User> searchCustomer(final DTOSearchUser dtoSearchUser) throws InvalidUserStatusException
    {
        final List<?> customer = createQuerySearchUser.createQuery(dtoSearchUser);

        if (customer != null && customer.size() > 0) return (List<User>) customer;
        else throw new NullPointerException("Not found customer");
    }

    public boolean hasEmail(final String email)
    {
        return (userRepository.findByEmail(email) != null);
    }
}
