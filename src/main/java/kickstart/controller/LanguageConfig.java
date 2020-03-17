package kickstart.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Konfiguration der Sprache ausgelieferter templates
 */
@Configuration
public class LanguageConfig implements WebMvcConfigurer {
	/**
	 * Setzt Standardsprache einer Session auf Englisch
	 * @return Spracheeinstellung einer Session
	 */
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.ENGLISH);
		//slr.setDefaultLocale(Locale.GERMAN);
		return slr;
	}

	/**
	 * Bindet Parameter "lang" an für die zu verwendende Sprache der Session.
	 * Die Standartsprache wird abgefangen.
	 * @return Sprachenänderungseinstellung
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	/**
	 * Fügt eine Sprachenänderungseinstellung der Möglichkeiten zum Abfangen der Standardsprache hinzu
	 * @param registry Menge aller Möglichkeiten
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
}