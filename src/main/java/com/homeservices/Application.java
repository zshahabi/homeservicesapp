package com.homeservices;

import com.homeservices.data.enums.UserStatus;
import com.homeservices.dto.DTOAddress;
import com.homeservices.dto.DTORegister;
import com.homeservices.service.MainServicesService;
import com.homeservices.service.CustomerService;
import org.springframework.stereotype.Component;

@Component
public record Application(MainServicesService mainServicesService , CustomerService customerService)
{
    public void start()
    {
        System.out.println("Application Started!");

        mainServicesService.addMainService();
    }


    private void addCustomer()
    {
        DTORegister dtoRegister = new DTORegister();
        dtoRegister.setName("admin");
        dtoRegister.setFamily("admin");
        dtoRegister.setEmail("admin@gmail.com");
        dtoRegister.setPassword("12345");
        dtoRegister.setUserStatus(UserStatus.accepted.name());
        dtoRegister.setAccountCredit(10000);

        DTOAddress dtoAddress = new DTOAddress();
        dtoAddress.setAlley("ALLEY");
        dtoAddress.setStreet("STREET");
        dtoAddress.setPostalCode(123456789);

        dtoRegister.setAddress(dtoAddress);

        customerService.register(dtoRegister);
    }

}
