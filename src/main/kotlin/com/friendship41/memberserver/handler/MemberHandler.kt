package com.friendship41.memberserver.handler

import com.friendship41.memberserver.data.CommonMemberInfo
import com.friendship41.memberserver.data.CommonMemberInfoRepository
import com.friendship41.memberserver.data.ReqBodyPostMember
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import java.util.*

@Component
class MemberHandler(@Autowired private val commonMemberInfoRepository: CommonMemberInfoRepository,
                    @Autowired private val webClient: WebClient) {

    fun registerCommonMember(request: ServerRequest): Mono<ServerResponse> = request
        .bodyToMono(ReqBodyPostMember::class.java)
        .zipWhen {
            this.postMemberAuthInfoFromAuthServer(this.getMemberAuthKey(it.commonMemberInfo),
                it.memberAuthInfo.memberPassword,
                it.memberAuthInfo.memberRole)
        }
        .map {
            this.createDefaultCommonMemberInfoEntity(it.t1.commonMemberInfo, it.t2) }
        .flatMap { commonMemberInfoRepository.save(it) }
        .flatMap { ok().bodyValue(it) }

    // TODO: 이 부분 이용해서 본인 정보 조회 구현 필요
//    fun getCommonMember(request: ServerRequest): Mono<ServerResponse> = ReactiveSecurityContextHolder
//        .getContext().flatMap {
//            ok().bodyValue(it.authentication)
//        }

    fun getCommonMember(request: ServerRequest): Mono<ServerResponse> = commonMemberInfoRepository
        .findById(request.pathVariable("memberNo").toInt())
        .flatMap { ok().bodyValue(it) }
        .switchIfEmpty(notFound().build())

    private fun getMemberAuthKey(commonMemberInfo: CommonMemberInfo): String = commonMemberInfo.memberId
        ?: commonMemberInfo.memberEmail
        ?: throw Exception("no memberId and memberEmail")

    private fun createDefaultCommonMemberInfoEntity(commonMemberInfo: CommonMemberInfo,
                                             response: Map<*, *>): CommonMemberInfo {
        commonMemberInfo.joinDate = Date()
        commonMemberInfo.joinPath = commonMemberInfo.joinPath ?: "NORMAL"
        commonMemberInfo.memberNo = response["memberNo"] as Int
        return commonMemberInfo
    }

    private fun postMemberAuthInfoFromAuthServer(memberAuthKey: String,
                                                memberPassword: String,
                                                memberRole: String?): Mono<Map<*, *>> = webClient.mutate()
        .baseUrl("https://stage41.xyz:42222")
        .build()
        .post()
        .uri("/authInfo")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(mapOf(
            Pair("memberAuthKey", memberAuthKey),
            Pair("memberPassword", memberPassword),
            Pair("memberRole", memberRole ?: "USER")))
        .retrieve()
        .bodyToMono(Map::class.java)
}
