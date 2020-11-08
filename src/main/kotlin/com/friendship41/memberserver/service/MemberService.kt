package com.friendship41.memberserver.service

import com.friendship41.memberserver.data.MemberAuthInfo
import com.friendship41.memberserver.data.MemberAuthInfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import java.nio.file.attribute.UserPrincipalNotFoundException
import java.util.*

const val NOINFO = "(NOINFO)"

@Service
class MemberService(@Autowired private val memberAuthInfoRepository: MemberAuthInfoRepository) {
    val bCryptPasswordEncoder = BCryptPasswordEncoder(4)

    fun getDefaultMemberInfo(memberNo: Int):MemberAuthInfo {
        return this.memberAuthInfoRepository.findById(memberNo)
                .orElseThrow { UserPrincipalNotFoundException("user Not Found, memberNo = $memberNo") }
    }

    fun registerDefailtMember(memberAuthInfo: MemberAuthInfo): Any {
        if (memberAuthInfo.memberId == null && memberAuthInfo.memberEmail == null) {
            return HttpClientErrorException(HttpStatus.BAD_REQUEST, "No MemberId And MemberEmail")
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
