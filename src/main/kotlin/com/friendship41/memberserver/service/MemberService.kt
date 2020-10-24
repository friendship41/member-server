package com.friendship41.memberserver.service

import com.friendship41.memberserver.data.MemberAuthInfo
import com.friendship41.memberserver.data.MemberAuthInfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class MemberService(@Autowired private val memberAuthInfoRepository: MemberAuthInfoRepository) {
    fun getDefaultMemberInfo(user_name: String):MemberAuthInfo? {
        val memberAuthInfo = this.memberAuthInfoRepository.findByMemberId(user_name)
        if (memberAuthInfo != null) {
            return memberAuthInfo
        }
        return this.memberAuthInfoRepository.findByMemberEmail(user_name)
    }

    fun registerDefailtMember(memberAuthInfo: MemberAuthInfo): Any {
        if (memberAuthInfo.memberId == null) {
            memberAuthInfo.memberId = UUID.randomUUID().toString()
        }
        if (memberAuthInfo.memberEmail == null) {
            memberAuthInfo.memberEmail = UUID.randomUUID().toString()
        }
        if (memberAuthInfo.memberRole == null) {
            memberAuthInfo.memberRole = "USER"
        }
        memberAuthInfo.memberJoinDate = Date()
        return this.memberAuthInfoRepository.save(memberAuthInfo)
    }
}
