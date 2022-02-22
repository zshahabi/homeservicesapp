package com.home.services.controller;

import com.home.services.data.entity.Comments;
import com.home.services.data.entity.Order;
import com.home.services.data.entity.Suggestion;
import com.home.services.data.entity.User;
import com.home.services.data.enums.Roles;
import com.home.services.data.enums.UserStatus;
import com.home.services.data.repository.UserRepository;
import com.home.services.dto.DTOAddOrder;
import com.home.services.dto.DTOAddSubService;
import com.home.services.dto.DTOAddSuggestion;
import com.home.services.dto.DTOCustomerRegister;
import com.home.services.dto.DTOExpertRegister;
import com.home.services.dto.DTOSearchUser;
import com.home.services.dto.mapper.CommentsMapper;
import com.home.services.dto.mapper.MainServiceForAddSubServiceMapper;
import com.home.services.dto.mapper.ShowSuggestionMapper;
import com.home.services.dto.mapper.UsersMapper;
import com.home.services.exception.FoundEmailException;
import com.home.services.exception.FoundMainServiceException;
import com.home.services.exception.FoundSubServiceException;
import com.home.services.exception.ImageSizeException;
import com.home.services.exception.InvalidIdException;
import com.home.services.exception.InvalidImageException;
import com.home.services.exception.InvalidPasswordException;
import com.home.services.exception.InvalidPostalCodeException;
import com.home.services.exception.InvalidPriceException;
import com.home.services.exception.InvalidUserStatusException;
import com.home.services.exception.NotFoundOrderException;
import com.home.services.exception.NotFoundSubServiceException;
import com.home.services.exception.NotFoundSuggestionException;
import com.home.services.exception.NotFoundUserException;
import com.home.services.exception.ThePaymentAmountIsInsufficient;
import com.home.services.exception.ThisOrderHasBeenPaidException;
import com.home.services.other.Str;
import com.home.services.service.CommentService;
import com.home.services.service.CustomerService;
import com.home.services.service.ExpertService;
import com.home.services.service.MainServicesService;
import com.home.services.service.OrderService;
import com.home.services.service.SubServiceService;
import com.home.services.service.SuggestionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping(value = "/")
public record Views(OrderService orderService , SubServiceService subServiceService ,
                    SuggestionService suggestionService , ShowSuggestionMapper showSuggestionMapper ,
                    MainServicesService mainServicesService ,
                    MainServiceForAddSubServiceMapper mainServiceForAddSubServiceMapper , ExpertService expertService ,
                    CustomerService customerService , UsersMapper usersMapper , CommentService commentService ,
                    CommentsMapper commentsMapper)
{
    @RequestMapping(value = {"/" , "/home" , "/index"}, method = RequestMethod.GET)
    public String index()
    {
        return "index";
    }

    @RequestMapping("/login")
    public String login()
    {
        return "login";
    }

    @RequestMapping(value = "/service-view")
    public String serviceView(final ModelMap model , Authentication authentication)
    {
        final UserDetails details = (UserDetails) authentication.getPrincipal();
        final Collection<? extends GrantedAuthority> authorities = details.getAuthorities();

        Roles role;

        final List<Order> orders = (((role = hasRole(authorities , Roles.ADMIN)) != null) || ((role = hasRole(authorities , Roles.EXPERT)) != null)) ? orderService.orderRepository().findAll() : orderService.orderRepository().findByCustomerEmail(authentication.getName());

        if (role == null) role = Roles.CUSTOMER;

        model.put("role" , role.name().toLowerCase(Locale.ROOT));
        model.put("orders" , orders);

        return "services";
    }

    private Roles hasRole(final Collection<? extends GrantedAuthority> authorities , final Roles role)
    {
        for (final GrantedAuthority authority : authorities)
        {
            if (authority.getAuthority().equals(role.name())) return role;
        }

        return null;
    }

    @RequestMapping(value = "/add-new-order", method = RequestMethod.GET)
    @RolesAllowed({"ADMIN" , "EXPERT"})
    public String addNewOrderView(final ModelMap model)
    {
        model.put("subServiceNames" , getSubServiceNames());
        return "add-new-order";
    }

    @RequestMapping(value = "/add-new-order", method = RequestMethod.POST)
    @RolesAllowed({"ADMIN" , "EXPERT"})
    public String addNewOrder(final ModelMap model , @ModelAttribute("dtoAddNewOrder") DTOAddOrder dtoAddOrder)
    {
        boolean addOrder = false;
        try
        {
            addOrder = orderService.addOrder(dtoAddOrder);
        }
        catch (NotFoundUserException | NotFoundSubServiceException | InvalidPostalCodeException e)
        {
            model.put("error" , e.getMessage());
        }

        model.put("result" , addOrder);

        model.put("subServiceNames" , getSubServiceNames());
        return "add-new-order";
    }

    private List<String> getSubServiceNames()
    {
        return subServiceService.subServiceRepository().getSubServiceByNames();
    }

    @RequestMapping(value = {"/add-suggestion" , "/add-suggestion/{ORDER_ID}"}, method = RequestMethod.GET)
    @RolesAllowed("EXPERT")
    public String addSuggestion(final ModelMap modelMap , @PathVariable(value = "ORDER_ID") final String strOrderId)
    {
        long orderId = 0;
        try
        {
            orderId = Long.parseLong(strOrderId);
        }
        catch (Exception e)
        {
            modelMap.put("error" , "Invalid order id");
        }

        if (orderId > 0)
        {
            modelMap.put("orderId" , orderId);

            final Optional<Order> orderFindById = orderService.orderRepository().findById(orderId);
            orderFindById.ifPresentOrElse(order -> modelMap.put("orderName" , order.getName()) , () -> modelMap.put("error" , "Not found order"));
        }

        return "add-suggestion";
    }

    @RequestMapping(value = {"/add-suggestion" , "/add-suggestion/{ORDER_ID}"}, method = RequestMethod.POST)
    @RolesAllowed("EXPERT")
    public String addSuggestion(final ModelMap modelMap , @PathVariable(value = "ORDER_ID") final String strOrderId , final Authentication authentication , @ModelAttribute("addSuggestion") final DTOAddSuggestion dtoAddSuggestion)
    {
        long orderId = 0;
        try
        {
            orderId = Long.parseLong(strOrderId);
        }
        catch (Exception e)
        {
            modelMap.put("error" , "Invalid order id");
        }

        dtoAddSuggestion.setOrderId(orderId);

        (orderService.orderRepository().findById(orderId)).ifPresent(order -> modelMap.put("orderName" , order.getName()));

        boolean result = false;
        try
        {
            result = suggestionService.addSuggestion(dtoAddSuggestion , suggestionService.customerRepository().findByEmail(authentication.getName()));
        }
        catch (NotFoundOrderException e)
        {
            modelMap.put("error" , e.getMessage());
        }

        modelMap.put("result" , result);
        return "add-suggestion";
    }

    @RequestMapping(value = {"/show-suggestion" , "/show-suggestion/{ORDER_ID}"}, method = RequestMethod.GET)
    @RolesAllowed({"ADMIN" , "EXPERT"})
    public String showSuggestionOrder(final ModelMap modelMap , @PathVariable(value = "ORDER_ID") final String strOrderId)
    {
        final long orderId = checkStrId(modelMap , strOrderId , "Invalid order id");

        if (orderId > 0)
        {
            (orderService.orderRepository().findById(orderId)).ifPresent(order ->
            {
                modelMap.put("orderName" , order.getName());
                modelMap.put("orderId" , orderId);
            });

            final List<Suggestion> suggestionOrder = suggestionService.suggestionRepository().findByOrderId(orderId);

            if (suggestionOrder.size() > 0)
                modelMap.put("showSuggestions" , showSuggestionMapper.toDtoShowSuggestion(suggestionOrder));
            else modelMap.put("error" , "Not found suggestion");
        }

        return "show-suggestion";
    }

    @RequestMapping(value = {"/remove-suggestion" , "/remove-suggestion/{EXPERT_ID}/{SUGGESTION_ID}"}, method = RequestMethod.GET)
    @RolesAllowed({"ADMIN" , "EXPERT"})
    public String removeSuggestion(final ModelMap modelMap , @PathVariable(value = "EXPERT_ID") final String strExpertId , @PathVariable(value = "SUGGESTION_ID") final String strSuggestionId)
    {
        final long expertId = checkStrId(modelMap , strExpertId , "Invalid expert id");
        final long suggestionId = checkStrId(modelMap , strSuggestionId , "Invalid suggestion id");

        boolean result = false;
        if (expertId > 0 && suggestionId > 0)
        {
            try
            {
                result = suggestionService.removeSuggestion(expertId , suggestionId);
            }
            catch (NotFoundSuggestionException | NotFoundUserException e)
            {
                modelMap.put("error" , e.getMessage());
            }
        }
        modelMap.put("result" , result);

        return "remove-suggestion";
    }

    @RequestMapping(value = {"/accept-suggestion" , "/accept-suggestion/{EXPERT_ID}/{ORDER_ID}/{SUGGESTION_ID}"}, method = RequestMethod.GET)
    @RolesAllowed({"ADMIN" , "EXPERT"})
    public String acceptSuggestion(final ModelMap modelMap , @PathVariable(value = "EXPERT_ID") final String strExpertId , @PathVariable(value = "ORDER_ID") final String strOrderId , @PathVariable(value = "SUGGESTION_ID") final String strSuggestionId)
    {
        final long expertId = checkStrId(modelMap , strExpertId , "Invalid expert id");
        final long suggestionId = checkStrId(modelMap , strSuggestionId , "Invalid suggestion id");
        final long orderId = checkStrId(modelMap , strOrderId , "Invalid order id");

        boolean result = false;
        if (expertId > 0 && suggestionId > 0 && orderId > 0)
        {
            try
            {
                result = orderService.acceptExpert(expertId , orderId , suggestionId);
            }
            catch (NotFoundUserException | NotFoundOrderException | NotFoundSuggestionException e)
            {
                modelMap.put("error" , e.getMessage());
            }
        }
        modelMap.put("result" , result);

        return "accept-suggestion";
    }

    private long checkStrId(final ModelMap modelMap , final String strId , final String errorMessage)
    {
        try
        {
            return Long.parseLong(strId);
        }
        catch (Exception e)
        {
            modelMap.put("error" , errorMessage);
        }
        return 0;
    }

    @RequestMapping(value = {"/order-payment" , "/order-payment/{ORDER_ID}"}, method = RequestMethod.GET)
    @RolesAllowed({"ADMIN"})
    public String acceptSuggestion(final ModelMap modelMap , @PathVariable(value = "ORDER_ID") final String strOrderId)
    {
        final long orderId = checkStrId(modelMap , strOrderId , "Invalid order id");
        if (orderId > 0)
        {
            boolean result = false;
            try
            {
                result = orderService.payment(orderId , 0);// price yani key padakht online anjam shode , agar 0 bashe az hesab customer kasr mishe
            }
            catch (NotFoundOrderException | NotFoundSuggestionException | ThePaymentAmountIsInsufficient | ThisOrderHasBeenPaidException e)
            {
                modelMap.put("error" , e.getMessage());
            }

            modelMap.put("result" , result);
        }

        return "order-payment";
    }

    @RequestMapping(value = "/add-subservice", method = RequestMethod.GET)
    @RolesAllowed({"ADMIN"})
    public String addSubService(final ModelMap modelMap)
    {
        modelMap.put("mainServices" , mainServiceForAddSubServiceMapper.toDtoMainServiceForAddSubServices(mainServicesService.mainServices()));

        return "add-subservice";
    }

    @RequestMapping(value = "/add-subservice", method = RequestMethod.POST)
    @RolesAllowed({"ADMIN"})
    public String addSubService(final ModelMap modelMap , @ModelAttribute("addSubService") final DTOAddSubService dtoAddSubService)
    {
        modelMap.put("mainServices" , mainServiceForAddSubServiceMapper.toDtoMainServiceForAddSubServices(mainServicesService.mainServices()));

        boolean result = false;
        try
        {
            result = subServiceService.addNewSubService(dtoAddSubService);
        }
        catch (InvalidIdException | InvalidPriceException | FoundSubServiceException e)
        {
            modelMap.put("error" , e.getMessage());
        }

        modelMap.put("result" , result);

        return "add-subservice";
    }


    @RequestMapping(value = "/add-main-service", method = RequestMethod.GET)
    @RolesAllowed({"ADMIN"})
    public String addMainService()
    {
        return "add-main-service";
    }

    @RequestMapping(value = "/add-main-service", method = RequestMethod.POST)
    @RolesAllowed({"ADMIN"})
    public String addMainService(final ModelMap modelMap , @RequestParam(value = "name") final String name)
    {
        boolean result = false;
        try
        {
            result = mainServicesService.addCustomMainService(name);
        }
        catch (FoundMainServiceException | NullPointerException e)
        {
            modelMap.put("error" , e.getMessage());
        }

        modelMap.put("result" , result);

        return "add-main-service";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register()
    {
        return "register";
    }

    @RequestMapping(value = "/register-expert", method = RequestMethod.POST)
    public String register(final ModelMap modelMap , @ModelAttribute("addNewUser") final DTOExpertRegister dtoRegister)
    {
        final Roles role = checkRole(modelMap , dtoRegister.getUserType());

        boolean result = false;
        if (role != null)
        {
            if (role.equals(Roles.EXPERT))
            {
                try
                {

                    final MultipartFile image = dtoRegister.getImage();

                    if (image != null)
                    {
                        dtoRegister.setImg(image.getBytes());

                        result = expertService.register(dtoRegister);
                    }
                    else modelMap.put("error" , "Image is null");
                }
                catch (ImageSizeException | NullPointerException | FoundEmailException | InvalidPasswordException | InvalidPostalCodeException | IOException | InvalidImageException e)
                {
                    if (e instanceof IOException) modelMap.put("error" , "Image is invalid!");
                    else modelMap.put("error" , e.getMessage());
                }
            }
            else modelMap.put("error" , "Invalid user type");
        }

        modelMap.put("result" , result);

        return "register";
    }

    @RequestMapping(value = "/register-customer", method = RequestMethod.POST)
    public String register(final ModelMap modelMap , @ModelAttribute("addNewUser") final DTOCustomerRegister dtoRegister)
    {
        final Roles role = checkRole(modelMap , dtoRegister.getUserType());

        boolean result = false;
        if (role != null)
        {
            if (role.equals(Roles.CUSTOMER))
            {
                try
                {
                    result = customerService.register(dtoRegister);
                }
                catch (FoundEmailException | InvalidPasswordException | NullPointerException | InvalidPostalCodeException e)
                {
                    modelMap.put("error" , e.getMessage());
                }
            }
            else modelMap.put("error" , "Invalid user type");
        }

        modelMap.put("result" , result);

        return "register";
    }

    private Roles checkRole(final ModelMap modelMap , final String roleStr)
    {
        try
        {
            if (Str.notEmpty(roleStr) && !roleStr.toUpperCase(Locale.ROOT).equals("ADMIN"))
                return Roles.valueOf(roleStr.toUpperCase(Locale.ROOT));
            else throw new Exception();
        }
        catch (Exception e)
        {
            modelMap.put("error" , "Invalid user type");
        }

        return null;
    }

    @RequestMapping(value = "/users/{ROLE}", method = RequestMethod.GET)
    @RolesAllowed({"ADMIN"})
    public String users(final ModelMap modelMap , @PathVariable(value = "ROLE") final String roleStr)
    {
        final Roles role = checkRole(modelMap , roleStr);
        if (role != null)
        {
            modelMap.put("role" , role.name().toLowerCase(Locale.ROOT));

            List<User> users = null;

            if (role.equals(Roles.EXPERT)) users = expertService.expertRepository().findByRolesContains(Roles.EXPERT);
            else if (role.equals(Roles.CUSTOMER))
                users = customerService.userRepository().findByRolesContains(Roles.CUSTOMER);
            else modelMap.put("error" , "Invalid role");

            if (users != null && users.size() > 0) modelMap.put("users" , usersMapper.toDtoUsers(users));
        }

        return "users";
    }

    @RequestMapping(value = "/accept-user/{USER_ID}", method = RequestMethod.GET)
    @RolesAllowed({"ADMIN"})
    public String acceptUser(final ModelMap modelMap , @PathVariable(value = "USER_ID") final String strUserId)
    {
        final long userId = checkStrId(modelMap , strUserId , "Invalid user id");

        boolean result = false;
        if (userId > 0)
        {
            final UserRepository userRepository = customerService.userRepository();

            final User userFindById = userRepository.findById(userId);

            if (userFindById != null)
            {
                if (!userFindById.getUserStatus().equals(UserStatus.ACCEPTED))
                    result = userRepository.changeUserStatus(UserStatus.ACCEPTED , userId) > 0;
                else modelMap.put("error" , "This user is accepted");
            }
            else modelMap.put("error" , "Invalid user id");
        }

        modelMap.put("result" , result);

        modelMap.put("operationName" , "Accept user");

        return "operation-users";
    }

    @RequestMapping(value = "/remove-user/{USER_ID}", method = RequestMethod.GET)
    @RolesAllowed({"ADMIN"})
    public String removeUser(final ModelMap modelMap , @PathVariable(value = "USER_ID") final String strUserId)
    {
        final long userId = checkStrId(modelMap , strUserId , "Invalid user id");

        boolean result = false;
        if (userId > 0)
        {
            final UserRepository userRepository = customerService.userRepository();

            final User userFindById = userRepository.findById(userId);

            if (userFindById != null)
            {
                userRepository.delete(userFindById);
                result = true;
            }
            else modelMap.put("error" , "Invalid user id");
        }

        modelMap.put("result" , result);

        modelMap.put("operationName" , "Remove user");

        return "operation-users";
    }

    @RequestMapping(value = "/users/{ROLE}/search", method = RequestMethod.GET)
    @RolesAllowed({"ADMIN"})
    public String searchUsers(final ModelMap modelMap , @PathVariable(value = "ROLE") final String strRole)
    {
        modelMap.put("role" , strRole);

        checkRole(modelMap , strRole);

        return "search-users";
    }

    @RequestMapping(value = "/users/{ROLE}/search", method = RequestMethod.POST)
    @RolesAllowed({"ADMIN"})
    public String searchUsers(final ModelMap modelMap , @PathVariable(value = "ROLE") final String strRole , @ModelAttribute("searchUsers") final DTOSearchUser dtoSearchUser)
    {
        modelMap.put("role" , strRole);

        final Roles role = checkRole(modelMap , strRole);

        if (role != null)
        {
            List<User> users = null;

            dtoSearchUser.setUserStatus(dtoSearchUser.getUserStatus().toUpperCase(Locale.ROOT).replace(" " , "_"));

            if (role.equals(Roles.EXPERT))
            {
                try
                {
                    users = expertService.searchExperts(dtoSearchUser);
                }
                catch (InvalidUserStatusException | NullPointerException e)
                {
                    modelMap.put("error" , e.getMessage());
                }
            }
            else if (role.equals(Roles.CUSTOMER))
            {
                try
                {
                    users = customerService.searchCustomer(dtoSearchUser);
                }
                catch (InvalidUserStatusException e)
                {
                    modelMap.put("error" , e.getMessage());
                }
            }
            else modelMap.put("error" , "Invalid role");


            if (users != null)
            {
                modelMap.put("users" , usersMapper.toDtoUsers(users));
                return "users";
            }
        }

        return "search-users";
    }

    @RequestMapping(value = "/order-comments/{ORDER_ID}", method = RequestMethod.GET)
    public String getComments(final ModelMap modelMap , @PathVariable(value = "ORDER_ID") final String strOrderId , final Authentication authentication)
    {
        final long orderId = checkStrId(modelMap , strOrderId , "Invalid order id");

        if (orderId > 0)
        {
            final Optional<Order> orderFindById = orderService.orderRepository().findById(orderId);

            if (orderFindById.isPresent())
            {
                final List<Comments> comments;

                if (hasRole(authentication.getAuthorities() , Roles.ADMIN) != null || hasRole(authentication.getAuthorities() , Roles.EXPERT) != null)
                    comments = commentService.getCommentsByOrder(orderId);
                else comments = commentService.getCommentsByUser(authentication.getName());

                modelMap.put("comments" , commentsMapper.toDtoComments(comments));

                final Order order = orderFindById.get();
                modelMap.put("loggedEmail" , authentication.getName());
                modelMap.put("orderName" , order.getName());
                modelMap.put("orderId" , order.getId());
            }
            else modelMap.put("error" , "Invalid order id");
        }

        return "comments";
    }

    @RequestMapping(value = "/remove-comment/{COMMENT_ID}", method = RequestMethod.GET)
    public String removeComment(final ModelMap modelMap , @PathVariable(value = "COMMENT_ID") final String strCommentId , final Authentication authentication)
    {
        final long commentId = checkStrId(modelMap , strCommentId , "Invalid comment id");

        boolean result = false;
        if (commentId > 0)
        {
            final Comments comment = commentService.commentRepository().findByIdAndUserEmail(commentId , authentication.getName());
            if (comment != null)
            {
                commentService.commentRepository().delete(comment);
                result = true;
            }
            else modelMap.put("error" , "Not found comment");
        }
        modelMap.put("operationName" , "Remove comment");
        modelMap.put("result" , result);

        return "operation-users";
    }
}
