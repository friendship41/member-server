package com.friendship41.memberserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter
import org.springframework.security.oauth2.provider.token.RemoteTokenServices

@Configuration
@EnableResourceServer
class MemberResourceServerConfig: ResourceServerConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                .antMatchers("/member")
                .access("#oauth2.hasScope('read_profile')")
                .anyRequest()
                .permitAll()
                .and()
                .csrf().disable()
    }

    @Primary
    @Bean
    fun tokenService(): RemoteTokenServices {
        val tokenService = RemoteTokenServices()
        tokenService.setCheckTokenEndpointUrl("https://192.168.0.200:42222/oauth/check_token")
        tokenService.setClientId("member_server")
        tokenService.setClientSecret("thisismember")
        tokenService.setAccessTokenConverter(CustomAccessTokenConverter())
        return tokenService
    }
}

class CustomAccessTokenConverter: DefaultAccessTokenConverter() {
    override fun extractAuthentication(map: MutableMap<String, *>?): OAuth2Authentication {
        val oAuth2Authentication = super.extractAuthentication(map)
        oAuth2Authentication.details = map
        return oAuth2Authentication
    }
}
