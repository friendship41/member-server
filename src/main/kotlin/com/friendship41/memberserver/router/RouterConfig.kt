package com.friendship41.memberserver.router

import com.friendship41.memberserver.handler.FriendHandler
//import com.friendship41.memberserver.service.CommonMemberService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.router

@Configuration
class RouterConfig(private val friendHandler: FriendHandler) {
//    @Bean
//    fun commonMemberRouter() = RouterFunctions.nest(
//        RequestPredicates.path("/member"),
//        router {
//            listOf(
//                // TODO: 핸들러 등록
//                GET("common"),
//                POST("")
//            )
//        }
//    )

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
