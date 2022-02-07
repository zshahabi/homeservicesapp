package com.homeservices.service;

import com.homeservices.config.SpringConfig;
import com.homeservices.dto.DTOAddSuggestion;
import com.homeservices.exception.NotFoundOrderException;
import com.homeservices.exception.NotFoundUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SuggestionServiceTest
{

    private SuggestionService suggestionService;

    @BeforeEach
    void setUp()
    {
        SpringConfig.config();
        suggestionService = SpringConfig.newInstance(SuggestionService.class);
    }

    @Test
    void addSuggestion()
    {
        DTOAddSuggestion dtoAddSuggestion = new DTOAddSuggestion();
        dtoAddSuggestion.setSuggestion("Gregre");
        dtoAddSuggestion.setExpert(2);
        dtoAddSuggestion.setPrice(474);
        dtoAddSuggestion.setOrder(4);

        try
        {
            boolean addSuggestion = suggestionService.addSuggestion(dtoAddSuggestion);

            assertTrue(addSuggestion);
        }
        catch (NotFoundUserException | NotFoundOrderException e)
        {
            e.printStackTrace();
        }
    }
}