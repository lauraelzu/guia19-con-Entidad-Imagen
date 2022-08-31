package com.egg.EggNews3;

import com.egg.EggNews3.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadWeb extends WebSecurityConfigurerAdapter {
    
    @Autowired
    public UsuarioServicio usuarioServicio;
    
    //configurar el manejador de seguridad de Spring Security
    //en userDetailsService se pasa como parámetro el servicio que se usará para autenticar a los usuarios (loadUserByUserName de UsuarioServicio)
    //y además se debe encriptar la clave del usuario autenticado con el encoder BCryptPasswordEncoder
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)throws Exception{                                
        auth.userDetailsService(usuarioServicio)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
    
    //configuracion login y logout
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
            .authorizeRequests()
                .antMatchers("/admin/*").hasRole("ADMIN")
                .antMatchers("/inicio").hasAnyRole()
                .antMatchers("/css/*", "/js/*", "/img/*", "/**")
                .permitAll()
            .and().formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/logincheck")
                .usernameParameter("dni")
                .passwordParameter("clave")
                .defaultSuccessUrl("/index")
                .permitAll()
            .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
            .and().csrf()
                .disable();
    }
}
