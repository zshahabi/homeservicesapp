package com.homeservices.service;

import com.homeservices.config.SpringConfig;
import com.homeservices.data.entity.Suggestion;
import com.homeservices.dto.DTOAddSuggestion;
import com.homeservices.exception.NotFoundOrderException;
import com.homeservices.exception.NotFoundSuggestionException;
import com.homeservices.exception.NotFoundUserException;
import com.homeservices.exception.TheBidPriceIsLowerThanTheBasePriceException;
import com.homeservices.exception.ThisExcerptIsNotAnExpertInThisFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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

    @Test
    void getSuggestionsCustomer()
    {
        try
        {
            List<Suggestion> suggestionsCustomer = suggestionService.getSuggestionsCustomer(4 , 6);

            assertNotNull(suggestionsCustomer);
        }
        catch (NotFoundOrderException | NotFoundSuggestionException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void removeSuggestion()
    {
        try
        {
            boolean removeSuggestion = suggestionService.removeSuggestion(4 , 7);

            assertTrue(removeSuggestion);
        }
        catch (NotFoundUserException | NotFoundSuggestionException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void findAllOrder()
    {
        try
        {
            List<Suggestion> allOrder = suggestionService.findAllOrder(5);

            assertNotNull(allOrder);
        }
        catch (NotFoundOrderException | NotFoundSuggestionException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void findAllOrderExpert()
    {
        try
        {
            Suggestion allOrderExpert = suggestionService.findAllOrderExpert(5 , 1);

            assertNotNull(allOrderExpert);
        }
        catch (NotFoundOrderException | NotFoundUserException | NotFoundSuggestionException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void addSuggestionInAllSubServiceOrder()
    {
        DTOAddSuggestion dtoAddSuggestion = new DTOAddSuggestion();

        dtoAddSuggestion.setTimeDo("TIME_DO");
        dtoAddSuggestion.setPrice(445);
        dtoAddSuggestion.setExpert(45);

        try
        {
            boolean add = suggestionService.addSuggestionInSubServiceOrder(dtoAddSuggestion , "NAME");

            assertTrue(add);
        }
        catch (NotFoundUserException | NotFoundOrderException | ThisExcerptIsNotAnExpertInThisFieldException | TheBidPriceIsLowerThanTheBasePriceException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void getAllSuggestions()
    {
        try
        {
            List<Suggestion> allSuggestions = suggestionService.getAllSuggestions(5 , 8);

            assertNotNull(allSuggestions);
        }
        catch (NotFoundOrderException | NotFoundUserException | NotFoundSuggestionException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void getAllSuggestionsSortPrice()
    {
        try
        {
            List<Suggestion> allSuggestionsSortPrice = suggestionService.getAllSuggestionsSortPrice(2 , 2);

            assertNotNull(allSuggestionsSortPrice);
        }
        catch (NotFoundOrderException | NotFoundUserException | NotFoundSuggestionException e)
        {
            e.printStackTrace();
        }
    }
}