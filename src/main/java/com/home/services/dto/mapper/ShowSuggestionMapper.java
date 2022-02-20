package com.home.services.dto.mapper;

import com.home.services.data.entity.Suggestion;
import com.home.services.dto.DTOShowSuggestion;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public final class ShowSuggestionMapper
{
    public List<DTOShowSuggestion> toDtoShowSuggestion(final List<Suggestion> suggestions)
    {
        final List<DTOShowSuggestion> dtoShowSuggestions = new ArrayList<>();
        for (final Suggestion suggestion : suggestions)
        {
            final DTOShowSuggestion dtoShowSuggestion = new DTOShowSuggestion();
            dtoShowSuggestion.setSuggestion(suggestion.getSuggestion());
            dtoShowSuggestion.setId(suggestion.getId());
            dtoShowSuggestion.setExpertEmail(suggestion.getExpert().getEmail());
            dtoShowSuggestion.setPrice(suggestion.getPrice());
            dtoShowSuggestion.setStartTime(suggestion.getStartTime().toString());
            dtoShowSuggestion.setTimeDo(suggestion.getStartTime().toString());

            dtoShowSuggestions.add(dtoShowSuggestion);
        }

        return dtoShowSuggestions;
    }
}
