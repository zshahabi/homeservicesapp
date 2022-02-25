package com.home.services.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

    private final DataSource dataSource;

    @Autowired
    public SecurityConfig(final DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(new BCryptPasswordEncoder()).usersByUsernameQuery("select `email`,`password`,`enable` from `users` where `email` = ?").authoritiesByUsernameQuery("select `email`,`roles` from `authorities` where `email` = ?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.cors().disable()
                .authorizeRequests().antMatchers(
                        "/" ,
                        "/register" ,
                        "/register-customer" ,
                        "/register-expert" ,
                        "/css/**"
                )
                .permitAll()

                .antMatchers("/api/**").permitAll()

                // swagger
                .antMatchers("/swagger-ui/**" , "/v3/api-docs/**").hasAuthority("ADMIN")

                .antMatchers("/add-suggestion/**").hasAuthority("EXPERT")
                .antMatchers("/add-main-service").hasAuthority("ADMIN")
                .antMatchers("/add-subservice").hasAuthority("ADMIN")
                .antMatchers("/sub-services").hasAnyAuthority("ADMIN" , "EXPERT")
                .antMatchers("/customer-order-payment" , "/customer-order-payment/**").hasAuthority("CUSTOMER")
                .antMatchers("/sub-services/**").hasAnyAuthority("ADMIN")
                .antMatchers("/show-suggestion" , "/show-suggestion/**").hasAnyAuthority("ADMIN" , "EXPERT")
                .antMatchers("/users" , "/users/**").hasAuthority("ADMIN")
                .antMatchers("/edit-user" , "/edit-user/**").hasAuthority("ADMIN")
                .antMatchers("/remove-user" , "/remove-user/**").hasAuthority("ADMIN")
                .antMatchers("/accept-user" , "/accept-user/**").hasAuthority("ADMIN")
                .antMatchers("/order-payment" , "/order-payment/**").hasAuthority("ADMIN")
                .antMatchers("/change-account-credit" , "/change-account-credit/**").hasAnyAuthority("ADMIN" , "EXPERT" , "CUSTOMER")

                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").usernameParameter("email").passwordParameter("password").permitAll()
                .and().logout().permitAll();
    }

}
