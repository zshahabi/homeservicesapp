package com.homeservices.service;

import com.homeservices.data.entity.Experts;
import com.homeservices.data.entity.Order;
import com.homeservices.data.entity.Suggestion;
import com.homeservices.data.repository.SuggestionRepository;
import com.homeservices.dto.DTOAddSuggestion;
import com.homeservices.exception.NotFoundOrderException;
import com.homeservices.exception.NotFoundUserException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record SuggestionService(SuggestionRepository repository , ExpertService expertService ,
                                OrderService orderService)
{

    public boolean addSuggestion(final DTOAddSuggestion dtoAddSuggestion) throws NotFoundUserException, NotFoundOrderException
    {
        Optional<Experts> byExpertId = expertService.repository().findById(dtoAddSuggestion.getExpert());
        if (byExpertId.isPresent())
        {
            Optional<Order> byOrderId = orderService.repository().findById(dtoAddSuggestion.getOrder());
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

}
