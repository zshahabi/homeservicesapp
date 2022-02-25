package com.home.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class HomeServiceApplication extends SpringBootServletInitializer
{

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder builder)
    {
        return builder.sources(HomeServiceApplication.class);
    }

    public static void main(String[] args)
    {
        System.out.println(new BCryptPasswordEncoder().encode("1234"));
        final ConfigurableApplicationContext context = SpringApplication.run(HomeServiceApplication.class , args);
//        final Address address = new Address();
//        address.setStreet("STREET customer");
//        address.setAlley("Alley customer");
//        address.setPostalCode(235464646);
//
//        final User user = new User();
//        user.getRoles().add(Roles.CUSTOMER);
//        user.setEmail("customer@gmail.com");
//        user.setAccountCredit(10);
//        user.setFamily("family customer");
//        user.setPassword(new BCryptPasswordEncoder().encode("1234"));
//        user.setName("name customer");
//        user.setAddress(address);
//        user.setUserStatus(UserStatus.ACCEPTED);

//        context.getBean(UserRepository.class).save(user);
    }

}
