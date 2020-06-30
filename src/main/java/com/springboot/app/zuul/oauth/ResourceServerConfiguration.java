package com.springboot.app.zuul.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@RefreshScope
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

  @Value("${config.security.oauth.jwk.key}")
  private String jwtKey;

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources.tokenStore(tokenStore());
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().antMatchers("/api/security/oauth/**").permitAll()
        .antMatchers(HttpMethod.GET,"/api/productos/listar", "/api/items/listar", "/api/usuarios/usuarios").permitAll()
        .antMatchers(HttpMethod.GET, "/api/productos/ver/{id}",
            "/api/items/ver/{id}/cantidad/{cantidad}",
            "/api/usuarios/usuarios/{id}").hasAnyRole("ADMIN","USER")
    .antMatchers("/api/productos/**", "/api/items/**","/api/usuarios/**").hasRole("ADMIN")
    .anyRequest().authenticated(); //cualquier otra ruta requiere autenticación.

  }

  //para poder crear el token y almacenarlo
  @Bean
  public JwtTokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }

  //Aquí se firma el token y se convierte el token con toda la información
  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
    jwtAccessTokenConverter.setSigningKey(jwtKey);
    return jwtAccessTokenConverter;
  }
}
