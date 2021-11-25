package com.userportal.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.userportal.login.LoginSuccessHandler;
import com.userportal.login.UserLoginService;
import com.userportal.login.oauth2.LoginOAuth2UserService;
import com.userportal.login.oauth2.OAuth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	private LoginOAuth2UserService loginOAuth2UserService;
	
	@Autowired
	private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	
	
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public LoginSuccessHandler loginSuccessHandler() {
		return new LoginSuccessHandler();
	}
	
	@Autowired
	UserLoginService loginService;
	/*
	 * @Bean public UserLoginService userLoginService() { return new
	 * UserLoginService(); }
	 */
	
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(loginService);
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.authenticationProvider(authenticationProvider());
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.authorizeRequests()
			//.antMatchers("/users/page/**").hasAnyAuthority("user")
			.antMatchers("/users/**").hasAnyAuthority("admin","manager","user")
			.antMatchers("/logUser/**").hasAnyAuthority("admin","manager","user")
			.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.successHandler(loginSuccessHandler())
				.permitAll()
			.and()
				.oauth2Login()
					.loginPage("/login")
					.userInfoEndpoint()
					.userService(loginOAuth2UserService)
					.and()
						.successHandler(oAuth2LoginSuccessHandler)
			.and()
				.logout().permitAll()
			.and()
				.rememberMe()
				.key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")
				.userDetailsService(loginService)
				.tokenValiditySeconds(7 * 24 * 60 * 60);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
	}
}
