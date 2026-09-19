package com.tpe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Configuration
public class LocaleConfig
        implements WebMvcConfigurer {

    // =========================================
    // LOCALE RESOLVER
    // =========================================

    @Bean
    public LocaleResolver localeResolver() {

        CookieLocaleResolver localeResolver =
                new CookieLocaleResolver();

        /*
         * Default language = Turkish
         */
        localeResolver.setDefaultLocale(
                Locale.forLanguageTag("tr")
        );

        return localeResolver;
    }


    // =========================================
    // LANGUAGE CHANGE INTERCEPTOR
    // =========================================

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {

        LocaleChangeInterceptor interceptor =
                new LocaleChangeInterceptor();

        /*
         * Language can be changed with:
         *
         * ?lang=tr
         * ?lang=de
         * ?lang=en
         * ?lang=fr
         */
        interceptor.setParamName("lang");

        return interceptor;
    }


    // =========================================
    // REGISTER INTERCEPTOR
    // =========================================

    @Override
    public void addInterceptors(
            InterceptorRegistry registry) {

        registry.addInterceptor(
                localeChangeInterceptor()
        );
    }
}