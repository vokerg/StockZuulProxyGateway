package com.stock.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableWebSecurity
public class AutorizationConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired
	RestTemplate restTemplate;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
				
		http.csrf().disable()
		.authorizeRequests()
		//.antMatchers("/**").permitAll()
		.antMatchers("/orders/**").authenticated()
		.antMatchers("/stocks/**").permitAll()
		//.antMatchers("/authorize/**").authenticated()
		.and()
		.addFilter(new AuthorizationFilter(authenticationManager(), restTemplate))
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	/*
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.build();
		//auth.userDetailsService(stockUserDetailService).passwordEncoder(bCryptPasswordEncoder);
	}
	*/
}
