package com.home.services.controller;

import com.home.services.data.entity.Comments;
import com.home.services.data.entity.Order;
import com.home.services.data.entity.SubService;
import com.home.services.data.entity.Suggestion;
import com.home.services.data.entity.User;
import com.home.services.data.entity.UserRegisterValidationCode;
import com.home.services.data.enums.OrderStatus;
import com.home.services.data.enums.Roles;
import com.home.services.data.enums.UserStatus;
import com.home.services.data.repository.UserRepository;
import com.home.services.dto.DTOAddOrder;
import com.home.services.dto.DTOAddSubService;
import com.home.services.dto.DTOAddSuggestion;
import com.home.services.dto.DTOCustomerRegister;
import com.home.services.dto.DTOExpertRegister;
import com.home.services.dto.DTOPayment;
import com.home.services.dto.DTOSearchUser;
import com.home.services.dto.DTOUsers;
import com.home.services.dto.mapper.CommentsMapper;
import com.home.services.dto.mapper.MainServiceForAddSubServiceMapper;
import com.home.services.dto.mapper.ShowExpertMapper;
import com.home.services.dto.mapper.ShowSuggestionMapper;
import com.home.services.dto.mapper.SubServiceMapper;
import com.home.services.dto.mapper.UsersMapper;
import com.home.services.exception.FoundEmailException;
import com.home.services.exception.FoundExpertOnThisSubServiceException;
import com.home.services.exception.FoundMainServiceException;
import com.home.services.exception.FoundSubServiceException;
import com.home.services.exception.ImageSizeException;
import com.home.services.exception.InvalidIdException;
import com.home.services.exception.InvalidImageException;
import com.home.services.exception.InvalidPasswordException;
import com.home.services.exception.InvalidPostalCodeException;
import com.home.services.exception.InvalidPriceException;
import com.home.services.exception.InvalidUserStatusException;
import com.home.services.exception.NotFoundExpertOnThisSubServiceException;
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
import com.home.services.service.SendMail;
import com.home.services.service.SubServiceService;
import com.home.services.service.SuggestionService;
import com.home.services.service.UserRegisterValidationCodeService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
@RequestMapping(value = "/")
public record Views(OrderService orderService , SubServiceService subServiceService ,
                    SuggestionService suggestionService , ShowSuggestionMapper showSuggestionMapper ,
                    MainServicesService mainServicesService ,
                    MainServiceForAddSubServiceMapper mainServiceForAddSubServiceMapper , ExpertService expertService ,
                    CustomerService customerService , UsersMapper usersMapper , CommentService commentService ,
                    CommentsMapper commentsMapper , SubServiceMapper subServiceMapper ,
                    ShowExpertMapper showExpertMapper , SetVarForHeader setVarForHeader , SendMail sendMail ,
                    UserRegisterValidationCodeService userRegisterValidationCodeService)
{

    @RequestMapping(value = {"/" , "/home" , "/index"}, method = RequestMethod.GET)
    public String index(final ModelMap modelMap , final Authentication authentication)
    {
        setVarForHeader.set(modelMap , authentication);
        return "index";
    }

    @RequestMapping("/login")
    public String login(final ModelMap modelMap , final Authentication authentication)
    {
        setVarForHeader.set(modelMap , authentication);
        return "login";
    }

    @RequestMapping(value = "/service-view")
    public String serviceView(final ModelMap model , Authentication authentication)
    {
        setVarForHeader.set(model , authentication , "/home");

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
    public String addNewOrderView(final ModelMap model , final Authentication authentication)
    {
        setVarForHeader.set(model , authentication , "/service-view");

        model.put("userEmail" , authentication.getName());

        model.put("subServiceNames" , getSubServiceNames());
        return "add-new-order";
    }

    @RequestMapping(value = "/add-new-order", method = RequestMethod.POST)
    public String addNewOrder(final ModelMap model , final Authentication authentication , @ModelAttribute("dtoAddNewOrder") DTOAddOrder dtoAddOrder)
    {
        setVarForHeader.set(model , authentication , "/service-view");

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
    public String addSuggestion(final ModelMap modelMap , final Authentication authentication , @PathVariable(value = "ORDER_ID") final String strOrderId)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

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
        setVarForHeader.set(modelMap , authentication , "/service-view");

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
    public String showSuggestionOrder(final ModelMap modelMap , final Authentication authentication , @PathVariable(value = "ORDER_ID") final String strOrderId)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

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
    public String removeSuggestion(final ModelMap modelMap , final Authentication authentication , @PathVariable(value = "EXPERT_ID") final String strExpertId , @PathVariable(value = "SUGGESTION_ID") final String strSuggestionId)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

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
    public String acceptSuggestion(final ModelMap modelMap , final Authentication authentication , @PathVariable(value = "EXPERT_ID") final String strExpertId , @PathVariable(value = "ORDER_ID") final String strOrderId , @PathVariable(value = "SUGGESTION_ID") final String strSuggestionId)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

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
    public String acceptSuggestion(final ModelMap modelMap , final Authentication authentication , @PathVariable(value = "ORDER_ID") final String strOrderId)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

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
    public String addSubService(final ModelMap modelMap , final Authentication authentication)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

        modelMap.put("mainServices" , mainServiceForAddSubServiceMapper.toDtoMainServiceForAddSubServices(mainServicesService.mainServices()));

        return "add-subservice";
    }

    @RequestMapping(value = "/add-subservice", method = RequestMethod.POST)
    @RolesAllowed({"ADMIN"})
    public String addSubService(final ModelMap modelMap , final Authentication authentication , @ModelAttribute("addSubService") final DTOAddSubService dtoAddSubService)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

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
    public String addMainService(final ModelMap modelMap , final Authentication authentication)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

        return "add-main-service";
    }

    @RequestMapping(value = "/add-main-service", method = RequestMethod.POST)
    @RolesAllowed({"ADMIN"})
    public String addMainService(final ModelMap modelMap , final Authentication authentication , @RequestParam(value = "name") final String name)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

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
    public String register(final ModelMap modelMap , final Authentication authentication)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

        return "register";
    }

    @RequestMapping(value = "/register-expert", method = RequestMethod.POST)
    public String register(final ModelMap modelMap , final Authentication authentication , @ModelAttribute("addNewUser") final DTOExpertRegister dtoRegister)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

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

                        long userId = expertService.register(dtoRegister);
                        if (userId > 0)
                        {
                            result = true;
                            sendEmail(userId , dtoRegister.getEmail());
                        }
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

    private void sendEmail(final long userId , final String email)
    {
        new Thread(() ->
        {
            final long code = sendMail.random.nextLong(99999999);
            final User userFindById = customerService.userRepository().findById(userId);

            final UserRegisterValidationCode validationCode = new UserRegisterValidationCode();
            validationCode.setCode(code);
            validationCode.setUser(userFindById);
            validationCode.setCreditAt(LocalDateTime.now().minusMinutes(45));

            userRegisterValidationCodeService.userRegisterValidationCodeRepository().save(validationCode);

            final String url = String.format("%s/validation-code/%s/%d" , sendMail.getEnvironment().getProperty("base.url") , code , userId);

            System.out.println(480);
            sendMail.send(email , "Validation code service home" , "Please click " + url , SendMail.CONTENT_TEXT_PLAIN);

        }).start();
    }

    @RequestMapping(value = "/register-customer", method = RequestMethod.POST)
    public String register(final ModelMap modelMap , final Authentication authentication , @ModelAttribute("addNewUser") final DTOCustomerRegister dtoRegister)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

        final Roles role = checkRole(modelMap , dtoRegister.getUserType());

        boolean result = false;
        if (role != null)
        {
            if (role.equals(Roles.CUSTOMER))
            {
                try
                {
                    long userId = customerService.register(dtoRegister);
                    if (userId > 0)
                    {
                        result = true;
                        sendEmail(userId , dtoRegister.getEmail());
                    }
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
    public String users(final ModelMap modelMap , final Authentication authentication , @PathVariable(value = "ROLE") final String roleStr)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

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
    public String acceptUser(final ModelMap modelMap , final Authentication authentication , @PathVariable(value = "USER_ID") final String strUserId)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

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
    public String removeUser(final ModelMap modelMap , final Authentication authentication , @PathVariable(value = "USER_ID") final String strUserId)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

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
    public String searchUsers(final ModelMap modelMap , final Authentication authentication , @PathVariable(value = "ROLE") final String strRole)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

        modelMap.put("role" , strRole);

        checkRole(modelMap , strRole);

        return "search-users";
    }

    @RequestMapping(value = "/users/{ROLE}/search", method = RequestMethod.POST)
    @RolesAllowed({"ADMIN"})
    public String searchUsers(final ModelMap modelMap , final Authentication authentication , @PathVariable(value = "ROLE") final String strRole , @ModelAttribute("searchUsers") final DTOSearchUser dtoSearchUser)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

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
        setVarForHeader.set(modelMap , authentication , "/service-view");

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
        setVarForHeader.set(modelMap , authentication);

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

    @RequestMapping(value = "/add-comment/{ORDER_ID}", method = RequestMethod.GET)
    public String addComment(final ModelMap modelMap , final Authentication authentication , @PathVariable(value = "ORDER_ID") final String strOrderId)
    {
        setVarForHeader.set(modelMap , authentication);

        final long orderId = checkStrId(modelMap , strOrderId , "Invalid order id");

        if (orderId > 0)
        {
            final Optional<Order> orderFindById = orderService.orderRepository().findById(orderId);
            orderFindById.ifPresentOrElse(order -> modelMap.put("orderName" , order.getName()) , () -> modelMap.put("error" , "Invalid order id"));

            modelMap.put("orderId" , orderId);
        }

        return "add-comment";
    }

    @RequestMapping(value = "/add-comment/{ORDER_ID}", method = RequestMethod.POST)
    public String addComment(final ModelMap modelMap , @PathVariable(value = "ORDER_ID") final String strOrderId , @RequestParam("textComment") final String textComment , final Authentication authentication)
    {
        setVarForHeader.set(modelMap , authentication);

        final long orderId = checkStrId(modelMap , strOrderId , "Invalid order id");

        final AtomicBoolean result = new AtomicBoolean(false);

        if (orderId > 0)
        {
            final Optional<Order> orderFindById = orderService.orderRepository().findById(orderId);
            orderFindById.ifPresentOrElse(order ->
            {
                modelMap.put("orderName" , order.getName());

                try
                {
                    result.set(commentService.addComment(orderId , authentication.getName() , textComment));
                }
                catch (NotFoundOrderException | AccessDeniedException e)
                {
                    modelMap.put("error" , e.getMessage());
                }

            } , () -> modelMap.put("error" , "Invalid order id"));

            modelMap.put("orderId" , orderId);
        }

        modelMap.put("result" , result.get());

        return "add-comment";
    }

    @RequestMapping(value = "/sub-services", method = RequestMethod.GET)
    @RolesAllowed({"ADMIN" , "EXPERT"})
    public String subServices(final ModelMap modelMap , final Authentication authentication)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

        final User userFindByEmail = customerService.userRepository().findByEmail(authentication.getName());
        final Roles role = userFindByEmail.getRoles().get(0);

        final List<SubService> subServices = subServiceService.subServiceRepository().findAll();

        modelMap.put("role" , role.name().toLowerCase(Locale.ROOT));
        modelMap.put("subServices" , subServiceMapper.toDtoSubServices(subServices));

        return "sub-services";
    }

    @RequestMapping(value = "/sub-services/show-experts/{SUB_SERVICE_ID}", method = RequestMethod.GET)
    @RolesAllowed({"ADMIN"})
    public String showExpertsSubService(final ModelMap modelMap , final Authentication authentication , @PathVariable(value = "SUB_SERVICE_ID") final String strSubServiceId)
    {
        setVarForHeader.set(modelMap , authentication , "/sub-services");

        final long subServiceId = checkStrId(modelMap , strSubServiceId , "Invalid sub service id");

        if (subServiceId > 0)
        {
            final List<User> expertsFindByRole = customerService.userRepository().findByRolesContainsAndSubServicesId(Roles.EXPERT , subServiceId);
            final Optional<SubService> subServiceFindById = subServiceService.subServiceRepository().findById(subServiceId);
            subServiceFindById.ifPresent(subService ->
            {
                modelMap.put("subServiceName" , subService.getName());
                modelMap.put("subServiceId" , subService.getId());
            });

            modelMap.put("experts" , showExpertMapper.toDtoShowExperts(expertsFindByRole));
        }

        return "show-expers-sub-service";
    }

    @RequestMapping(value = "/sub-services/add-expert/{SUB_SERVICE_ID}", method = RequestMethod.GET)
    @RolesAllowed({"ADMIN"})
    public String addExpertSubService(final ModelMap modelMap , final Authentication authentication , @PathVariable(value = "SUB_SERVICE_ID") final String strSubServiceId)
    {
        setVarForHeader.set(modelMap , authentication , "/sub-services");

        final long subServiceId = checkStrId(modelMap , strSubServiceId , "Invalid sub service id");

        if (subServiceId > 0)
        {
            final Optional<SubService> subServicesFindById = subServiceService.subServiceRepository().findById(subServiceId);

            subServicesFindById.ifPresent(subService ->
            {
                modelMap.put("subServiceId" , subServiceId);
                modelMap.put("subServiceName" , subService.getName());
            });

        }
        return "add-expert-sub-service";
    }

    @RequestMapping(value = "/sub-services/add-expert/{SUB_SERVICE_ID}", method = RequestMethod.POST)
    @RolesAllowed({"ADMIN"})
    public String addExpertSubService(final ModelMap modelMap , final Authentication authentication , @PathVariable(value = "SUB_SERVICE_ID") final String strSubServiceId , @RequestParam(value = "expertEmail") final String expertEmail)
    {
        setVarForHeader.set(modelMap , authentication , "/sub-services");

        final long subServiceId = checkStrId(modelMap , strSubServiceId , "Invalid sub service id");

        final AtomicBoolean result = new AtomicBoolean(false);

        if (subServiceId > 0)
        {
            final Optional<SubService> subServicesFindById = subServiceService.subServiceRepository().findById(subServiceId);

            subServicesFindById.ifPresent(subService ->
            {
                modelMap.put("subServiceId" , subServiceId);
                modelMap.put("subServiceName" , subService.getName());

                try
                {
                    result.set(expertService.addExpertSubService(subServiceId , expertEmail));
                }
                catch (NotFoundSubServiceException | NotFoundUserException | FoundExpertOnThisSubServiceException e)
                {
                    modelMap.put("error" , e.getMessage());
                }

            });

        }

        modelMap.put("result" , result.get());

        return "add-expert-sub-service";
    }

    @RequestMapping(value = "/sub-services/remove-expert/{SUB_SERVICE_ID}/{EXPERT_ID}", method = RequestMethod.GET)
    @RolesAllowed({"ADMIN"})
    public String removeExpert(final ModelMap modelMap , final Authentication authentication , @PathVariable(value = "SUB_SERVICE_ID") final String strSubServiceId , @PathVariable(value = "EXPERT_ID") final String strExpertId)
    {
        setVarForHeader.set(modelMap , authentication , "/sub-services");

        final long subServiceId = checkStrId(modelMap , strSubServiceId , "Invalid sub service id");
        final long expertId = checkStrId(modelMap , strExpertId , "Invalid expert id");

        boolean result = false;

        if (subServiceId > 0 && expertId > 0)
        {
            try
            {
                result = expertService.removeExpertSubService(subServiceId , expertId);
            }
            catch (NotFoundSubServiceException | NotFoundUserException | NotFoundExpertOnThisSubServiceException e)
            {
                modelMap.put("error" , e.getMessage());
            }
        }

        modelMap.put("result" , result);
        modelMap.put("operationName" , "Remove expert sub service");

        return "operation-users";
    }

    @RequestMapping(value = "/my-profile", method = RequestMethod.GET)
    public String removeExpert(final ModelMap modelMap , final Authentication authentication)
    {
        if (authentication != null && authentication.isAuthenticated())
        {
            setVarForHeader.set(modelMap , authentication);

            final User usersFindByEmail = expertService.expertRepository().findByEmail(authentication.getName());

            final DTOUsers dtoUser = usersMapper.toDtoUser(usersFindByEmail);

            modelMap.put("user" , dtoUser);
        }

        return "my-profile";
    }

    @RequestMapping(value = {"/users/change-account-credit" , "/users/change-account-credit/{USER_ID}"}, method = RequestMethod.POST)
    @RolesAllowed({"ADMIN"})
    public String changeAccountCredit(final ModelMap modelMap , final Authentication authentication , @RequestParam(value = "accountCredit") final String strAccountCredit , @PathVariable(value = "USER_ID") final String strUserId)
    {
        setVarForHeader.set(modelMap , authentication);

        final long userId = checkStrId(modelMap , strUserId , "Invalid user id");

        boolean result = false;
        if (userId > 0)
        {
            try
            {
                final int accountCredit = Integer.parseInt(strAccountCredit);
                if (accountCredit < 0) throw new Exception();

                System.out.println(accountCredit);

                final UserRepository userRepository = expertService.expertRepository();

                final User userFindById = userRepository.findById(userId);
                userFindById.setAccountCredit(accountCredit);
                userRepository.save(userFindById);

                result = true;
            }
            catch (Exception e)
            {
                modelMap.put("error" , "Invalid account credit");
            }
        }

        modelMap.put("result" , result);
        modelMap.put("operationName" , "Change account credit");

        return "operation-users";
    }

    //
    //
    // PART 4-5
    //
    //
    @RequestMapping(value = "/customer-order-payment/{ORDER_ID}", method = RequestMethod.GET)
    @RolesAllowed({"CUSTOMER"})
    public String customerOrderPayment(final ModelMap modelMap , final Authentication authentication , @PathVariable(value = "ORDER_ID") final String strOrderId)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

        final long orderId = checkStrId(modelMap , strOrderId , "Invalid order id");

        final AtomicBoolean result = new AtomicBoolean(false);

        final AtomicBoolean onlinePayment = new AtomicBoolean(false);

        if (orderId > 0)
        {
            final Optional<Order> orderFindById = orderService.orderRepository().findById(orderId);

            orderFindById.ifPresentOrElse(order ->
            {
                final User customer = order.getCustomer();

                if (customer.getEmail().equals(authentication.getName()))
                {
                    final int orderPrice = order.getPrice();
                    final int accountCredit = customer.getAccountCredit();

                    if (accountCredit >= orderPrice)
                    {
                        try
                        {
                            orderService.payment(orderId , 0);
                            result.set(true);
                        }
                        catch (NotFoundOrderException | NotFoundSuggestionException | ThePaymentAmountIsInsufficient | ThisOrderHasBeenPaidException e)
                        {
                            modelMap.put("error" , e.getMessage());
                        }
                    }
                    else
                    {
                        onlinePayment.set(true);
                        modelMap.put("price" , order.getPrice());
                        modelMap.put("orderId" , orderId);
                        modelMap.put("orderName" , order.getName());
                    }
                }
                else modelMap.put("error" , "Access denied");

            } , () -> modelMap.put("error" , "Invalid order id"));
        }

        if (onlinePayment.get()) return "online-peymant";
        else
        {
            modelMap.put("result" , result.get());
            modelMap.put("operationName" , "Order payment");
            return "operation-users";
        }
    }

    @RequestMapping(value = "/customer-order-payment/{ORDER_ID}", method = RequestMethod.POST)
    @RolesAllowed({"CUSTOMER"})
    public String customerOrderPayment(final ModelMap modelMap , final Authentication authentication , @PathVariable(value = "ORDER_ID") final String strOrderId , @ModelAttribute("payment") final DTOPayment dtoPayment)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

        final long orderId = checkStrId(modelMap , strOrderId , "Invalid order id");

        final AtomicBoolean result = new AtomicBoolean(false);

        if (orderId > 0)
        {
            final Optional<Order> orderFindById = orderService.orderRepository().findById(orderId);

            orderFindById.ifPresentOrElse(order ->
            {
                try
                {
                    orderService.payment(orderId , order.getSubService().getPrice());
                    result.set(true);
                }
                catch (NotFoundOrderException | NotFoundSuggestionException | ThePaymentAmountIsInsufficient | ThisOrderHasBeenPaidException e)
                {
                    modelMap.put("error" , e.getMessage());
                }

            } , () -> modelMap.put("error" , "Invalid order id"));
        }

        modelMap.put("result" , result.get());
        modelMap.put("operationName" , "Order payment");
        return "operation-users";
    }

    @RequestMapping(value = "/validation-code/{CODE}/{USER_ID}", method = RequestMethod.GET)
    public String validationCode(final ModelMap modelMap , final Authentication authentication , @PathVariable(value = "CODE") final String strCode , @PathVariable(value = "USER_ID") final String strUserId)
    {
        setVarForHeader.set(modelMap , authentication , "/home");

        boolean result = false;

        final long userId = checkStrId(modelMap , strUserId , "Invalid user id");

        if (userId > 0)
        {
            try
            {
                final long code = Long.parseLong(strCode);

                System.out.println(code);
                System.out.println(userId);

                final UserRegisterValidationCode validationCode = userRegisterValidationCodeService.getValidationCode(code , userId);

                if (validationCode != null)
                {
                    validationCode.setExpired(true);
                    userRegisterValidationCodeService.userRegisterValidationCodeRepository().save(validationCode);

                    final User user = validationCode.getUser();

                    final Roles role = user.getRoles().get(0);
                    if (role.equals(Roles.EXPERT)) user.setUserStatus(UserStatus.WAITING_ACCEPT);
                    else user.setUserStatus(UserStatus.ACCEPTED);

                    customerService.userRepository().save(user);

                    result = true;
                }
                else modelMap.put("error" , "Invalid validation code");
            }
            catch (Exception e)
            {
                modelMap.put("error" , "invalid code");
            }
        }

        modelMap.put("operationName" , "Validation code");
        modelMap.put("result" , result);

        return "operation-users";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(final ModelMap modelMap , final Authentication authentication , final HttpServletResponse response , final HttpServletRequest request)
    {
        setVarForHeader.set(modelMap , authentication);

        if (authentication != null) new SecurityContextLogoutHandler().logout(request , response , authentication);

        return "login";
    }

    @RequestMapping(value = "/set-done-order/{ORDER_ID}", method = RequestMethod.GET)
    @RolesAllowed({"ADMIN" , "EXPERT"})
    public String setDoneOrder(final ModelMap modelMap , final Authentication authentication , @PathVariable(value = "ORDER_ID") final String strOrderId)
    {
        setVarForHeader.set(modelMap , authentication , "/service-view");

        final long orderId = checkStrId(modelMap , strOrderId , "Invalid order id");

        boolean result = false;
        if (orderId > 0)
        {
            try
            {
                orderService.changeStatus(orderId , OrderStatus.DONE);
                result = true;
            }
            catch (NotFoundOrderException e)
            {
                modelMap.put("error" , e.getMessage());
            }
        }

        modelMap.put("operationName" , "Set done order");
        modelMap.put("result" , result);
        return "operation-users";
    }
}