package com.order.management.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.order.management.filter.AuthorizationFilter;
import com.order.management.service.CustomerService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthorizationFilter authorizationFilter;

	@Autowired
	private CustomerService customerService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// auth.inMemoryAuthentication().withUser("admin").password("{noop}admin5").roles("ADMIN");
		// auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder.encode("admin5")).roles("ADMIN");
		auth.userDetailsService(customerService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors();
		http.headers().frameOptions().disable();

		http.csrf().disable();

		http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
		http.authorizeRequests().antMatchers("/authenticate").permitAll();
		http.authorizeRequests().antMatchers("/token/refresh").permitAll();
		http.authorizeRequests().antMatchers("/user").permitAll();

		http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);

		http.authorizeRequests().anyRequest().authenticated().and().httpBasic();

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
