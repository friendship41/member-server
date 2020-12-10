package com.friendship41.memberserver.service

import com.friendship41.memberserver.common.BusinessException
import com.friendship41.memberserver.common.CommonErrorCode
import com.friendship41.memberserver.data.CommonMemberInfo
import com.friendship41.memberserver.data.CommonMemberInfoRepository
import com.friendship41.memberserver.data.MemberAuthInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.nio.file.attribute.UserPrincipalNotFoundException
import java.util.*
import kotlin.collections.HashMap

@Service
class CommonMemberService(
        @Autowired private val commonMemberInfoRepository: CommonMemberInfoRepository,
        @Autowired private val webClient: WebClient) {

    fun getDefaultMemberInfo(memberNo: Int): CommonMemberInfo {
        return this.commonMemberInfoRepository.findById(memberNo)
                .orElseThrow { throw UserPrincipalNotFoundException("user Not Found, memberNo = $memberNo") }
    }

    fun registerCommonMember(commonMemberInfo: CommonMemberInfo, memberAuthInfo: MemberAuthInfo): Any? {
        if (commonMemberInfo.joinClient == null) {
            throw BusinessException(CommonErrorCode.INVALID_INPUT_VALUE.toErrorResponse())
        }

        val memberAuthKey: String = commonMemberInfo.memberId
                ?: commonMemberInfo.memberEmail
                ?: throw BusinessException(CommonErrorCode.INVALID_INPUT_VALUE.toErrorResponse())

        val response = this.getMemberAuthInfoFromAuthServer(
                memberAuthKey,
                memberAuthInfo.memberPassword,
                memberAuthInfo.memberRole)

        return this.commonMemberInfoRepository.save(this.createCommonMemberInfoEntity(commonMemberInfo, response))
    }

    private fun createCommonMemberInfoEntity(commonMemberInfo: CommonMemberInfo,
                                             response: Map<*, *>): CommonMemberInfo {
        commonMemberInfo.joinDate = Date()
        commonMemberInfo.joinPath = commonMemberInfo.joinPath ?: "NORMAL"
        commonMemberInfo.memberNo = response["memberNo"] as Int
        return commonMemberInfo
    }

    private fun getMemberAuthInfoFromAuthServer(memberAuthKey: String,
                                                memberPassword: String,
                                                memberRole: String?): Map<*, *> = webClient.mutate()
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
            .flux()
            .toStream()
            .findFirst()
            .orElse(null)
}
