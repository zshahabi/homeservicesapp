package com.home.services.service;

import com.home.services.data.entity.Customer;
import com.home.services.data.enums.UserStatus;
import com.home.services.data.repository.CreateQuerySearchUser;
import com.home.services.data.repository.CustomerRepository;
import com.home.services.dto.DTOCustomerRegister;
import com.home.services.dto.DTOSearchUser;
import com.home.services.dto.mapper.AddressMapper;
import com.home.services.exception.FoundEmailException;
import com.home.services.exception.InvalidPasswordException;
import com.home.services.exception.InvalidUserStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class CustomerService
{
    public final CustomerRepository customerRepository;
    private final CheckEmptyUserInfo checkEmptyUserInfo;
    private final AddressMapper addressMapper;
    private final CreateQuerySearchUser createQuerySearchUser;

    @Autowired
    public CustomerService(final CustomerRepository customerRepository , final CheckEmptyUserInfo checkEmptyUserInfo , final AddressMapper addressMapper , final CreateQuerySearchUser createQuerySearchUser)
    {
        this.customerRepository = customerRepository;
        this.checkEmptyUserInfo = checkEmptyUserInfo;
        this.addressMapper = addressMapper;
        this.createQuerySearchUser = createQuerySearchUser;
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

    public List<Customer> searchCustomer(final DTOSearchUser dtoSearchUser) throws InvalidUserStatusException
    {
        final List<?> customer = createQuerySearchUser.createQuery("Customer" , dtoSearchUser);

        if (customer != null && customer.size() > 0) return (List<Customer>) customer;
        else throw new NullPointerException("Not found customer");
    }

    public boolean hasEmail(final String email)
    {
        return (customerRepository.findByEmail(email) != null);
    }
}
