package com.stock.authorization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.netflix.zuul.context.RequestContext;

public class AuthorizationFilter extends BasicAuthenticationFilter{

	public static final String HEADER_STRING = "Authorization";
	
	private RestTemplate restTemplate;
	
	public AuthorizationFilter(AuthenticationManager authenticationManager, RestTemplate restTemplate) {
		super(authenticationManager);
		this.restTemplate = restTemplate;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
					throws IOException, ServletException {
		String header = request.getHeader(HEADER_STRING);
		if (header == null) {
			chain.doFilter(request, response);
			return;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", header);
		HttpEntity<String> entityReq = new HttpEntity<String>("", headers);
		ResponseEntity<Object> obj = null;

		try {
			obj = restTemplate.exchange("http://STOCK-AUTH/authorize", HttpMethod.GET, entityReq, Object.class);	
		} catch (HttpClientErrorException e) {
			//response.setStatus(e.getStatusCode().value());
			chain.doFilter(request, response);
			return;
		}

		Authentication authentication = new UsernamePasswordAuthenticationToken(obj, null, new ArrayList<GrantedAuthority>());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		RequestContext requestContext = RequestContext.getCurrentContext();
		requestContext.addZuulRequestHeader("idUser", ((LinkedHashMap<String, String>)obj.getBody()).get("idUser"));
		chain.doFilter(request, response);
	}

}
