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
        auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("select `email`,`password`,`enable` from `users` where `email` = ?")
                .authoritiesByUsernameQuery("select `email`,`roles` from `authorities` where `email` = ?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.cors().disable()
                .authorizeRequests()
                .antMatchers("/" , "/register" , "/css/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/login").usernameParameter("email").passwordParameter("password").permitAll().and().logout().permitAll();
    }

}
