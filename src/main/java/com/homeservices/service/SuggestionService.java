package com.homeservices.service;

import com.homeservices.data.entity.Experts;
import com.homeservices.data.entity.Order;
import com.homeservices.data.entity.Suggestion;
import com.homeservices.data.repository.OrderRepository;
import com.homeservices.data.repository.SuggestionRepository;
import com.homeservices.dto.DTOAddSuggestion;
import com.homeservices.exception.NotFoundOrderException;
import com.homeservices.exception.NotFoundSuggestionException;
import com.homeservices.exception.NotFoundUserException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public record SuggestionService(SuggestionRepository repository , ExpertService expertService ,
                                OrderRepository orderRepository)
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

                suggestion = repository.save(suggestion);

                return suggestion.getId() > 0;
            }
            else throw new NotFoundOrderException(dtoAddSuggestion.getOrder());
        }
        else throw new NotFoundUserException("expert" , dtoAddSuggestion.getExpert());
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

    
}
