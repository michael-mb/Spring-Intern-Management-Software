package kickstart.controller;

import org.salespointframework.SalespointSecurityConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * Konfiguration der Erlaubnis auf bestimmte Zugriffe
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends SalespointSecurityConfiguration {

	/**
	 * Zugriffsrechte werden festgelegt
	 * @param http HTTP Sicherheit
	 * @throws Exception Fehler beim Laden der Konfiguration
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/login" , "/index" , "/css/**", "/js/**").permitAll()
				.antMatchers("/materials" ,"/productstate" , "/purshase" , "/services" , "/orders" , "/financialoverview" ,
						"/ordertable", "/orderpage").hasRole("USER")
				.antMatchers("/register" , "/personallist").hasRole("ADMIN")
				.and()
			.formLogin()
				.loginPage("/login").loginProcessingUrl("/login").and()
				.logout().logoutUrl("/logout").logoutSuccessUrl("/");	
	}
}