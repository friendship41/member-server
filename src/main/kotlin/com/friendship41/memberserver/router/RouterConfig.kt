package com.friendship41.memberserver.router

import com.friendship41.memberserver.handler.FriendHandler
import com.friendship41.memberserver.handler.MemberHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.router

@Configuration
@EnableWebFlux
class RouterConfig(private val memberHandler: MemberHandler, private val friendHandler: FriendHandler) {
    @Bean
    fun commonMemberRouter() = RouterFunctions.nest(
        RequestPredicates.path("/member"),
        router {
            listOf(
                POST("", memberHandler::registerCommonMember),
                GET("{memberNo}", memberHandler::getCommonMember)
            )
        }
    )

    @Bean
    fun friendRouter() = RouterFunctions.nest(
        RequestPredicates.path("/friend"),
        router {
            listOf(
                POST("", friendHandler::handlePost)
            )
        }
    )
}
