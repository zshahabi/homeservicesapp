package com.home.services.service;

import com.home.services.data.entity.Order;
import com.home.services.data.entity.Suggestion;
import com.home.services.data.entity.User;
import com.home.services.data.repository.OrderRepository;
import com.home.services.data.repository.SuggestionRepository;
import com.home.services.data.repository.UserRepository;
import com.home.services.dto.DTOAddSuggestion;
import com.home.services.exception.NotFoundOrderException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public record SuggestionService(SuggestionRepository suggestionRepository , ExpertService expertService ,
                                OrderRepository orderRepository , UserRepository customerRepository)
{

    public boolean addSuggestion(final DTOAddSuggestion dtoAddSuggestion , final User expert) throws NotFoundOrderException
    {
        Optional<Order> orderFindById = orderRepository.findById(dtoAddSuggestion.getOrderId());
        if (orderFindById.isPresent())
        {
            Suggestion suggestion = new Suggestion();
            suggestion.setSuggestion(dtoAddSuggestion.getSuggestion());
            suggestion.setExpert(expert);
            suggestion.setOrder(orderFindById.get());
            suggestion.setPrice(dtoAddSuggestion.getPrice());
            suggestion.setTimeDo(LocalDateTime.now().plusHours(dtoAddSuggestion.getTimeDo()));
            suggestion.setStartTime(LocalDateTime.now().plusHours(dtoAddSuggestion.getStartTime()));

            suggestion = suggestionRepository.save(suggestion);
            return suggestion.getId() > 0;
        }
        else throw new NotFoundOrderException(dtoAddSuggestion.getOrderId());

    }

}
