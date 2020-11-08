package com.friendship41.memberserver.service

import com.friendship41.memberserver.common.BusinessException
import com.friendship41.memberserver.data.ErrorResponse
import com.friendship41.memberserver.data.MemberAuthInfo
import com.friendship41.memberserver.data.MemberAuthInfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.nio.file.attribute.UserPrincipalNotFoundException
import java.util.*

const val NOINFO = "(NOINFO)"

@Service
class MemberService(@Autowired private val memberAuthInfoRepository: MemberAuthInfoRepository) {
    val bCryptPasswordEncoder = BCryptPasswordEncoder(4)

    fun getDefaultMemberInfo(memberNo: Int):MemberAuthInfo {
        return this.memberAuthInfoRepository.findById(memberNo)
                .orElseThrow { throw UserPrincipalNotFoundException("user Not Found, memberNo = $memberNo") }
    }

    fun registerDefailtMember(memberAuthInfo: MemberAuthInfo): Any {
        if (memberAuthInfo.memberId == null && memberAuthInfo.memberEmail == null) {
            throw BusinessException(ErrorResponse(
                    "memberId, memberEmail both empty",
                    400,
                    "MSE001",
                    listOf(
                            ErrorResponse.ErrorCause("memberId", "${memberAuthInfo.memberId}", "empty"),
                            ErrorResponse.ErrorCause("memberEmail", "${memberAuthInfo.memberEmail}", "empty"))))
        }
        if (memberAuthInfo.memberId == null) {
            memberAuthInfo.memberId = NOINFO + UUID.randomUUID().toString().substring(NOINFO.length)
        }
        if (memberAuthInfo.memberEmail == null) {
            memberAuthInfo.memberEmail = NOINFO + UUID.randomUUID().toString().substring(NOINFO.length)
        }
        if (memberAuthInfo.memberRole == null) {
            memberAuthInfo.memberRole = "USER"
        }
        memberAuthInfo.memberJoinDate = Date()
        memberAuthInfo.memberPassword = bCryptPasswordEncoder.encode(memberAuthInfo.memberPassword)
        return this.memberAuthInfoRepository.save(memberAuthInfo)
    }
}
