package com.homeservices.service;

import com.homeservices.data.entity.Customer;
import com.homeservices.data.entity.ExpertSelectedByTheCustomer;
import com.homeservices.data.entity.Experts;
import com.homeservices.data.repository.CustomerRepository;
import com.homeservices.data.repository.ExpertRepository;
import com.homeservices.data.repository.ExpertSelectedByTheCustomerRepository;
import com.homeservices.exception.FoundSelectedByTheCustomerException;
import com.homeservices.exception.NotFoundException;
import com.homeservices.exception.NotFoundUserException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public record ExpertSelectedByTheCustomerService(ExpertSelectedByTheCustomerRepository repository ,
                                                 ExpertRepository expertRepository ,
                                                 CustomerRepository customerRepository)
{

    public boolean addSelected(final long expertId , final long customerId) throws NotFoundUserException, FoundSelectedByTheCustomerException
    {
        final Optional<Experts> byExpertId = expertRepository.findById(expertId);
        if (byExpertId.isPresent())
        {
            final Optional<Customer> byCustomerId = customerRepository.findById(customerId);
            if (byCustomerId.isPresent())
            {
                final ExpertSelectedByTheCustomer byExpertIdAndCustomerId = repository.findByExpertIdAndCustomerId(expertId , customerId);

                if (byExpertIdAndCustomerId == null)
                {
                    ExpertSelectedByTheCustomer selectedByTheCustomer = new ExpertSelectedByTheCustomer();
                    selectedByTheCustomer.setCustomer(byCustomerId.get());
                    selectedByTheCustomer.setExpert(byExpertId.get());
                    selectedByTheCustomer = repository.save(selectedByTheCustomer);

                    return selectedByTheCustomer.getId() > 0;
                }
                else throw new FoundSelectedByTheCustomerException();
            }
            else throw new NotFoundUserException("customer" , customerId);
        }
        else throw new NotFoundUserException("expert" , expertId);
    }

    public List<ExpertSelectedByTheCustomer> getSelected(final long customer) throws NotFoundUserException, NotFoundException
    {
        final Optional<Customer> byCustomerId = customerRepository.findById(customer);
        if (byCustomerId.isPresent())
        {
            final List<ExpertSelectedByTheCustomer> selectedByTheCustomer = repository.findByCustomerId(customer);
            if (selectedByTheCustomer.size() > 0) return selectedByTheCustomer;
            else throw new NotFoundException();
        }
        else throw new NotFoundUserException("customer" , customer);
    }
}
