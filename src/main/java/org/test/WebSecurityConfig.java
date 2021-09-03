package org.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;



@EnableWebSecurity
public class WebSecurityConfig {

  // Two hardcoded users for test
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser("user1").password(passwordEncoder().encode("user1Pass")).roles("USER");
    auth.inMemoryAuthentication().withUser("user2").password(passwordEncoder().encode("user2Pass")).roles("USER");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
  }

  @Configuration
  public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

    // any authenticated user can access these
    private static final String[] AUTHENTICATED = {
        "/whoami" // a user has access to his own data
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests()
             .antMatchers(AUTHENTICATED).authenticated()
             .anyRequest().denyAll()
             .and()
          .httpBasic()
             .realmName("MyRealm");
    }
  }

  @Configuration
  @Order(1) // we test this first
  public static class WebAppsWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

    private static final String[] BROWSER = {
        "/docs/**", // entry point for swagger documentation
        "/swagger-ui/**",
        "/api-docs/**",
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.requestMatchers()
              .antMatchers(BROWSER).antMatchers("/logout").antMatchers("/login")
              .and()
          .authorizeRequests()
              .antMatchers(BROWSER).authenticated()
              .and()
          .formLogin().defaultSuccessUrl("/docs")
              .and()
          .logout();
    }
  }
}



