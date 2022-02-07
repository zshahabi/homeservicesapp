package com.homeservices.service;

import com.homeservices.data.entity.Customer;
import com.homeservices.data.entity.Experts;
import com.homeservices.data.entity.Order;
import com.homeservices.data.entity.SubService;
import com.homeservices.data.entity.Suggestion;
import com.homeservices.data.repository.CustomerRepository;
import com.homeservices.data.repository.OrderRepository;
import com.homeservices.data.repository.SuggestionRepository;
import com.homeservices.dto.DTOAddSuggestion;
import com.homeservices.exception.NotFoundOrderException;
import com.homeservices.exception.NotFoundSuggestionException;
import com.homeservices.exception.NotFoundUserException;
import com.homeservices.exception.ThisExcerptIsNotAnExpertInThisFieldException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public record SuggestionService(SuggestionRepository repository , ExpertService expertService ,
                                OrderRepository orderRepository , CustomerRepository customerRepository)
{

    public boolean addSuggestion(final DTOAddSuggestion dtoAddSuggestion) throws NotFoundUserException, NotFoundOrderException
    {
        Optional<Experts> byExpertId = expertService.repository().findById(dtoAddSuggestion.getExpert());
        if (byExpertId.isPresent())
        {
            Optional<Order> byOrderId = orderRepository.findById(dtoAddSuggestion.getOrder());
            if (byOrderId.isPresent())
            {
                Suggestion suggestion = new Suggestion();
                suggestion.setSuggestion(dtoAddSuggestion.getSuggestion());
                suggestion.setExpert(byExpertId.get());
                suggestion.setOrder(byOrderId.get());
                suggestion.setPrice(dtoAddSuggestion.getPrice());
                suggestion.setTimeDo(dtoAddSuggestion.getTimeDo());
                suggestion.setStartTime(dtoAddSuggestion.getStartTime());

                suggestion = repository.save(suggestion);

                return suggestion.getId() > 0;
            }
            else throw new NotFoundOrderException(dtoAddSuggestion.getOrder());
        }
        else throw new NotFoundUserException("expert" , dtoAddSuggestion.getExpert());
    }

    public List<Suggestion> getSuggestionsCustomer(final long customer , final long orderId) throws NotFoundOrderException, NotFoundSuggestionException
    {
        final Optional<Order> byOrderId = orderRepository.findById(orderId);
        if (byOrderId.isPresent())
        {
            final Order order = byOrderId.get();
            if (order.getCustomer().getId() == customer)
            {
                final List<Suggestion> suggestions = repository.findByOrderId(orderId);
                if (suggestions.size() > 0) return suggestions;
                else throw new NotFoundSuggestionException();
            }
            else throw new NotFoundOrderException(orderId);
        }
        else throw new NotFoundOrderException(orderId);
    }

    public boolean removeSuggestion(final long expertId , final long suggestionId) throws NotFoundUserException, NotFoundSuggestionException
    {
        Optional<Experts> byExpertId = expertService.repository().findById(expertId);
        if (byExpertId.isPresent())
        {
            Optional<Suggestion> bySuggestionId = repository.findById(suggestionId);
            if (bySuggestionId.isPresent())
            {
                repository.delete(bySuggestionId.get());

                return true;
            }
            else throw new NotFoundSuggestionException();
        }
        else throw new NotFoundUserException("expert" , expertId);
    }

    public List<Suggestion> findAllOrder(final long orderId) throws NotFoundOrderException, NotFoundSuggestionException
    {
        final Optional<Order> byOrderId = orderRepository.findById(orderId);
        if (byOrderId.isPresent())
        {
            final List<Suggestion> suggestions = repository.findByOrderId(orderId);
            if (suggestions != null && suggestions.size() > 0) return suggestions;
            else throw new NotFoundSuggestionException();
        }
        else throw new NotFoundOrderException(orderId);
    }

    public Suggestion findAllOrderExpert(final long orderId , final long expertId) throws NotFoundOrderException, NotFoundUserException, NotFoundSuggestionException
    {
        final Optional<Order> byOrderId = orderRepository.findById(orderId);
        if (byOrderId.isPresent())
        {
            Optional<Experts> byExpertId = expertService.repository().findById(expertId);
            if (byExpertId.isPresent())
            {
                Suggestion byExpertIdAndOrderId = repository.findByExpertIdAndOrderId(expertId , orderId);
                if (byExpertIdAndOrderId != null) return byExpertIdAndOrderId;
                else throw new NotFoundSuggestionException();
            }
            else throw new NotFoundUserException("expert" , expertId);
        }
        else throw new NotFoundOrderException(orderId);
    }

    public boolean addSuggestionInSubServiceOrder(final DTOAddSuggestion dtoAddSuggestion , final String subServiceName) throws NotFoundUserException, ThisExcerptIsNotAnExpertInThisFieldException, NotFoundOrderException
    {
        Optional<Experts> byExpertId = expertService.repository().findById(dtoAddSuggestion.getExpert());
        if (byExpertId.isPresent())
        {
            final Experts expert = byExpertId.get();
            final Set<SubService> subServices = expert.getSubServices();

            boolean ok = false;
            for (SubService subService : subServices)
            {
                if (subService.getName().equals(subServiceName))
                {
                    ok = true;
                    break;
                }
            }

            if (ok) return addSuggestion(dtoAddSuggestion);
            else throw new ThisExcerptIsNotAnExpertInThisFieldException();

        }
        else throw new NotFoundUserException("expert" , dtoAddSuggestion.getExpert());
    }

    public List<Suggestion> getAllSuggestions(final long orderId , final long customer) throws NotFoundOrderException, NotFoundUserException, NotFoundSuggestionException
    {
        final Optional<Order> byOrderId = orderRepository.findById(orderId);
        if (byOrderId.isPresent())
        {
            final Optional<Customer> byCustomerId = customerRepository.findById(customer);
            if (byCustomerId.isPresent())
            {
                final List<Suggestion> suggestions = repository.findByOrderIdAndOrderCustomerId(orderId , customer , Sort.by(Sort.Direction.DESC));
                if (suggestions.size() > 0) return suggestions;
                else throw new NotFoundSuggestionException();
            }
            else throw new NotFoundUserException("customer" , customer);
        }
        else throw new NotFoundOrderException(orderId);
    }

    public List<Suggestion> getAllSuggestionsSortPrice(final long orderId , final long customer) throws NotFoundOrderException, NotFoundUserException, NotFoundSuggestionException
    {
        final Optional<Order> byOrderId = orderRepository.findById(orderId);
        if (byOrderId.isPresent())
        {
            final Optional<Customer> byCustomerId = customerRepository.findById(customer);
            if (byCustomerId.isPresent())
            {
                final List<Suggestion> suggestions = repository.findByOrderIdAndOrderCustomerId(orderId , customer , Sort.by(Sort.Direction.DESC , "price"));
                if (suggestions.size() > 0) return suggestions;
                else throw new NotFoundSuggestionException();
            }
            else throw new NotFoundUserException("customer" , customer);
        }
        else throw new NotFoundOrderException(orderId);
    }
}
