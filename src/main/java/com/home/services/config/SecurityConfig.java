package com.home.services.config;

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

    public SecurityConfig(final DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("select email,password,enable from customer customer , Expert expert where customer.email = ? or expert.email = ?")
                .authoritiesByUsernameQuery("select email,roles from authorities where email = ?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.cors().disable()
                .authorizeRequests().antMatchers("/" , "/register").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/login").permitAll().and().logout().permitAll();
    }
}
