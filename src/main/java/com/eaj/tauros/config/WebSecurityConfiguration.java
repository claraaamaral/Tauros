package com.eaj.tauros.config;

import com.eaj.tauros.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MyUserDetailsService userDetailsService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String loginPage = "/login";
        String logoutPage = "/logout";
        String index = "/index";
        http.
                authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(loginPage).permitAll()
                .antMatchers(index).permitAll()
                .antMatchers("/listar/chamados-responsavel-setor").hasAuthority("RESPONSAVELSETOR")
                .antMatchers("/cadastro/usuario").permitAll()
                .antMatchers("/detalhes/chamado/{id}").permitAll()
                .antMatchers("/deletar/chamado/{id}").permitAll()
                .antMatchers("/atender/chamado/**").hasAnyAuthority("ADMIN","OPERADOR","RESPONSAVELSETOR" )
                .antMatchers("/problema/**").hasAnyAuthority("ADMIN","RESPONSAVELSETOR")
                .antMatchers("/relatorio/**").hasAnyAuthority("ADMIN","RESPONSAVELSETOR")
                .antMatchers("/problemas/**").hasAnyAuthority("ADMIN","RESPONSAVELSETOR")
                .antMatchers("/chamados").hasAuthority("ADMIN")
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/responsavel/**").hasAuthority("RESPONSAVELSETOR")
                .antMatchers("/deletar/usuario/**").hasAnyAuthority("ADMIN","RESPONSAVELSETOR")
                .anyRequest()
                .authenticated()
                .and().csrf().disable()
                .formLogin()
                .loginPage(loginPage)
                .loginPage("/")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl(index)
                .defaultSuccessUrl(index)
                .usernameParameter("user_name")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(logoutPage))
                .logoutSuccessUrl(loginPage);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**/**", "/static/**", "/css/**", "/static/js/**", "/images/**", "/error","/vendor/**");
    }

}
