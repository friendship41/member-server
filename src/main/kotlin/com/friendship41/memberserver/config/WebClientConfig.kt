package com.friendship41.memberserver.config

import com.friendship41.memberserver.common.logger
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.LoggingCodecSupport
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient

@Configuration
class WebClientConfig {
    @Bean
    fun webClient(): WebClient {
        val exchangeStrategies = ExchangeStrategies.builder()
                .codecs{ it.defaultCodecs().maxInMemorySize(1024*1024*50)}
                .build()

        exchangeStrategies
                .messageWriters().stream()
                .filter(LoggingCodecSupport::class.java::isInstance)
                .map { it as LoggingCodecSupport }
                .forEach { it.isEnableLoggingRequestDetails = true }

        return WebClient.builder()
                .clientConnector(
                        ReactorClientHttpConnector(
                                HttpClient
                                        .create()
                                        .tcpConfiguration { tcpClient ->
                                            tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 120_000)
                                                    .doOnConnected {
                                                        it.addHandlerLast(ReadTimeoutHandler(180))
                                                        it.addHandlerLast(WriteTimeoutHandler(180))}}))
                .exchangeStrategies(exchangeStrategies)
                .filter(ExchangeFilterFunction.ofRequestProcessor {
                    logger().info("Req to 3rd >> ${it.method()} ${it.url()}")
                    // req 파라미터, body 로깅하려면 이쪽
                    Mono.just(it)
                })
                .filter(ExchangeFilterFunction.ofResponseProcessor{
                    logger().info("Res from 3rd << ${it.statusCode()}")
                    // res 로깅하려면 이쪽
                    Mono.just(it)
                })
                .build()
    }
}
