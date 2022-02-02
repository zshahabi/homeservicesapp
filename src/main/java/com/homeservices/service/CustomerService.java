package com.homeservices.service;

import com.homeservices.config.SpringConfig;
import com.homeservices.data.entity.Address;
import com.homeservices.data.entity.Customer;
import com.homeservices.data.enums.UserStatus;
import com.homeservices.data.repository.CustomerRepository;
import com.homeservices.dto.DTORegister;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public record CustomerService(CustomerRepository repository , AddressService addressService)
{

    public boolean register(final DTORegister dtoRegister)
    {
        try
        {
            Address address = addressService.addAddress(dtoRegister.getAddress());

            Customer user = new Customer();
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

    public List<Customer> getAllCustomer()
    {
        CriteriaBuilder criteriaBuilder = SpringConfig.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Customer> query = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> from = query.from(Customer.class);
        CriteriaQuery<Customer> select = query.select(from);
        try
        {
            return execute(select).getResultList();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public Customer getUserById(long id)
    {
        CriteriaBuilder criteriaBuilder = SpringConfig.getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Customer> query = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> from = query.from(Customer.class);
        CriteriaQuery<Customer> select = query.select(from);

        select.where(criteriaBuilder.equal(from.get("id") , id));

        try
        {
            return execute(select).getSingleResult();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public Customer getUserByUEmailAndPassword(final String email , final String password)
    {
        CriteriaBuilder criteriaBuilder = SpringConfig.getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Customer> query = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> from = query.from(Customer.class);
        CriteriaQuery<Customer> select = query.select(from);

        select.where(criteriaBuilder.equal(from.get("email") , email) , criteriaBuilder.equal(from.get("password") , password));

        try
        {
            return execute(select).getSingleResult();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public List<Customer> getUserByStatus(UserStatus userStatus)
    {
        CriteriaBuilder criteriaBuilder = SpringConfig.getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Customer> query = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> from = query.from(Customer.class);
        CriteriaQuery<Customer> select = query.select(from);

        select.where(criteriaBuilder.equal(from.get("userStatus") , userStatus));

        try
        {
            return execute(select).getResultList();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private TypedQuery<Customer> execute(final CriteriaQuery<Customer> criteriaQuery)
    {
        return SpringConfig.getEntityManager().createQuery(criteriaQuery);
    }
}
