package com.home.services.service;

import com.home.services.data.entity.Customer;
import com.home.services.data.enums.UserStatus;
import com.home.services.data.repository.CustomerRepository;
import com.home.services.dto.DTOCustomerRegister;
import com.home.services.dto.mapper.AddressMapper;
import com.home.services.exception.FoundEmailException;
import com.home.services.exception.InvalidPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class CustomerService
{
    public final CustomerRepository customerRepository;
    private final CheckEmptyUserInfo checkEmptyUserInfo;
    private final AddressMapper addressMapper;

    @Autowired
    public CustomerService(final CustomerRepository customerRepository , final CheckEmptyUserInfo checkEmptyUserInfo , final AddressMapper addressMapper)
    {
        this.customerRepository = customerRepository;
        this.checkEmptyUserInfo = checkEmptyUserInfo;
        this.addressMapper = addressMapper;
    }

    public boolean register(final DTOCustomerRegister dtoCustomerRegister) throws InvalidPasswordException, NullPointerException, FoundEmailException
    {
        if (checkEmptyUserInfo.check(dtoCustomerRegister))
        {
            if (hasEmail(dtoCustomerRegister.getEmail()))
            {
                Customer customer = new Customer();
                customer.setName(dtoCustomerRegister.getName());
                customer.setFamily(dtoCustomerRegister.getFamily());
                customer.setEmail(dtoCustomerRegister.getEmail());
                customer.setPassword(dtoCustomerRegister.getPassword());
                customer.setUserStatus(UserStatus.WAITING_ACCEPT);
                customer.setAddress(addressMapper.toAddress(dtoCustomerRegister.getAddress()));

                customer = customerRepository.save(customer);

                return customer.getId() > 0;
            }
            else throw new FoundEmailException(dtoCustomerRegister.getEmail());
        }
        return false;
    }

    public boolean hasEmail(final String email)
    {
        return (customerRepository.findByEmail(email) != null);
    }
}
