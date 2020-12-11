package com.friendship41.memberserver.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Configuration
class SecurityConfig {
    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity, entryPoint: UnauthorizedAuthenticationEntryPoint): SecurityWebFilterChain {
        return http
            .exceptionHandling()
            .authenticationEntryPoint(entryPoint)
            .and()
            .addFilterAt(jwtAuthenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
            .authorizeExchange().pathMatchers(HttpMethod.POST, "/member").permitAll()
            .and()
            .authorizeExchange().pathMatchers(HttpMethod.POST, "/friend").permitAll()
            .anyExchange().authenticated()
            .and()
            .httpBasic().disable()
            .csrf().disable()
            .formLogin().disable()
            .logout().disable()
            .build()
    }

    @Bean
    fun jwtAuthenticationWebFilter(): AuthenticationWebFilter {
        val authenticationWebFilter = AuthenticationWebFilter(ReactiveAuthenticationManager { Mono.just(it) })
        // TODO: Jwt 컨버터 구현
        // 참고 : https://stackoverflow.com/questions/46793245/how-preauthorize-is-working-in-an-reactive-application-or-how-to-live-without-t
        authenticationWebFilter.setServerAuthenticationConverter {
            Mono.just(UsernamePasswordAuthenticationToken("test", "test", listOf(
                SimpleGrantedAuthority("USER")
            )))
        }
        return authenticationWebFilter
    }
}

@Component
class UnauthorizedAuthenticationEntryPoint: ServerAuthenticationEntryPoint {
    override fun commence(exchange: ServerWebExchange, e: AuthenticationException): Mono<Void> = Mono
        .fromRunnable { exchange.response.statusCode = HttpStatus.UNAUTHORIZED }
}

//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
//import com.friendship41.memberserver.common.CommonErrorCode
//import com.friendship41.memberserver.common.logger
//import com.friendship41.memberserver.data.ErrorResponse
//import io.jsonwebtoken.*
//import io.jsonwebtoken.security.SignatureException
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.http.HttpStatus
//import org.springframework.http.MediaType
//import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.authentication.BadCredentialsException
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
//import org.springframework.security.config.http.SessionCreationPolicy
//import org.springframework.security.core.Authentication
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.security.core.userdetails.User
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
//import org.springframework.stereotype.Component
//import org.springframework.web.client.RestTemplate
//import org.springframework.web.filter.GenericFilterBean
//import java.security.KeyFactory
//import java.security.PublicKey
//import java.security.spec.X509EncodedKeySpec
//import java.util.*
//import java.util.stream.Collectors
//import javax.annotation.PostConstruct
//import javax.servlet.FilterChain
//import javax.servlet.ServletRequest
//import javax.servlet.ServletResponse
//import javax.servlet.http.HttpServletRequest
//import javax.servlet.http.HttpServletResponse
//
//@Configuration
//class WebSecurityConfig(
//        @Autowired private val jwtTokenProvider: JwtTokenProvider): WebSecurityConfigurerAdapter() {
//    override fun configure(http: HttpSecurity) {
//        http
//                .httpBasic().disable()
//                .formLogin().disable()
//                .logout().disable()
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests().antMatchers("/member/**").hasAuthority("USER")
//                .and()
//                .addFilterBefore(
//                        JwtAuthenticationFilter(jwtTokenProvider),
//                        UsernamePasswordAuthenticationFilter::class.java)
//
//    }
//
//    @Bean
//    override fun authenticationManagerBean(): AuthenticationManager {
//        return super.authenticationManagerBean()
//    }
//}
//
//class JwtAuthenticationFilter(private val jwtTokenProvider: JwtTokenProvider): GenericFilterBean() {
//    private val objectMapper = ObjectMapper()
//
//    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
//        val rawToken = this.jwtTokenProvider.resolveToken(request as HttpServletRequest)
//        if (rawToken != null) {
//            val tokenClaims = try {
//                this.jwtTokenProvider.validateJwt(rawToken)
//            } catch (e: BadCredentialsException) {
//                val httpServletResponse = response as HttpServletResponse
//                httpServletResponse.status = HttpStatus.UNAUTHORIZED.value()
//                httpServletResponse.contentType = MediaType.APPLICATION_JSON_VALUE
//                httpServletResponse.writer.print(this.objectMapper.writeValueAsString(
//                        ErrorResponse(
//                                e.message ?: CommonErrorCode.BAD_CREDENTIALS.message,
//                                CommonErrorCode.BAD_CREDENTIALS.status,
//                                CommonErrorCode.BAD_CREDENTIALS.code)))
//                httpServletResponse.writer.flush()
//                return
//            }
//            SecurityContextHolder.getContext().authentication =
//                    this.jwtTokenProvider.getAuthentication(tokenClaims)
//            (SecurityContextHolder.getContext().authentication as UsernamePasswordAuthenticationToken)
//                    .details = tokenClaims
//        }
//
//        chain.doFilter(request, response)
//    }
//}
//
//const val HEADER_NAME_AUTHORIZATION = "AUTHORIZATION"
//const val BEARER = "BEARER "
//
//@Component
//class JwtTokenProvider {
//    lateinit var publicKey: PublicKey
//
//    @Value("\${auth-server.token_key.url}")
//    private val tokenKeyUrl: String? = null
//
//    @PostConstruct
//    fun getPublicKeyFromAuthServer() {
//        val tempRestTemplate = RestTemplate()
//        val response = tempRestTemplate.getForObject(
//                this.tokenKeyUrl
//                        ?: throw RuntimeException("[getPublicKeyFromAuthServer] application properties 'tokenKeyUrl' not exist"),
//                String::class.java)
//        val parser = jacksonObjectMapper().readValue(response, Map::class.java)
//        this.publicKey = this.createPublicKey(
//                parser["value"].toString(),
//                parser["alg"].toString())
//        logger().info("Success to get public key from auth server")
//        logger().info("public key : "+this.publicKey)
//    }
//
//    fun resolveToken(request: HttpServletRequest): String? {
//        val headerName = this.validateAuthHeader(request.headerNames) ?: return null
//        return this.getTokenWithValidation(request.getHeader(headerName))
//    }
//
//    fun validateJwt(jwtToken: String): Jws<Claims> = try {
//        Jwts.parserBuilder()
//                .setSigningKey(this.publicKey)
//                .build()
//                .parseClaimsJws(jwtToken)
//    } catch (e: SignatureException) {
//        logger().error("Invalid JWT signature: $jwtToken")
//        throw BadCredentialsException("Invalid JWT signature: $jwtToken")
//    } catch (e: MalformedJwtException) {
//        logger().error("Invalid token: $jwtToken")
//        throw BadCredentialsException("Invalid token: $jwtToken")
//    } catch (e: ExpiredJwtException) {
//        logger().error("Expired JWT token: $jwtToken")
//        throw BadCredentialsException("Expired JWT token: $jwtToken")
//    } catch (e: UnsupportedJwtException) {
//        logger().error("Unsupported JWT token: $jwtToken")
//        throw BadCredentialsException("Unsupported JWT token: $jwtToken")
//    } catch (e: IllegalArgumentException) {
//        logger().error("JWT token compact of handler are invalid: $jwtToken")
//        throw BadCredentialsException("JWT token compact of handler are invalid: $jwtToken")
//    } catch (e: Exception) {
//        logger().error("Invalid token: $jwtToken")
//        throw BadCredentialsException("Invalid token: $jwtToken")
//    }
//
//    fun getAuthentication(jwsClaims: Jws<Claims>): Authentication {
//        val authorities = (jwsClaims.body["authorities"] as ArrayList<*>).stream()
//                .map{ SimpleGrantedAuthority(it.toString()) }
//                .collect(Collectors.toList())
//
//        return UsernamePasswordAuthenticationToken(
//                User(
//                        jwsClaims.body["memberNo"].toString(),
//                        "",
//                        authorities),
//                "",
//                authorities
//        )
//    }
//
//    private fun createPublicKey(base64EncodedPublicKey: String, algorithm: String): PublicKey =
//            KeyFactory.getInstance(algorithm)
//                    .generatePublic(X509EncodedKeySpec(
//                            Base64.getDecoder().decode(base64EncodedPublicKey)))
//
//    private fun validateAuthHeader(headerNames: Enumeration<String>): String? {
//        for (header in headerNames) {
//            if (header.toUpperCase() == HEADER_NAME_AUTHORIZATION) {
//                return header
//            }
//        }
//        return null
//    }
//
//    private fun getTokenWithValidation(headerToken: String): String? {
//        if (headerToken.substring(0, BEARER.length).toUpperCase() != BEARER) {
//            return null
//        }
//        return headerToken.substring(BEARER.length)
//    }
//}
