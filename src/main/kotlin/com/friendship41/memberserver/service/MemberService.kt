package com.friendship41.memberserver.service

import com.friendship41.memberserver.data.MemberAuthInfo
import com.friendship41.memberserver.data.MemberAuthInfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MemberService(@Autowired private val memberAuthInfoRepository: MemberAuthInfoRepository) {
    fun getDefaultMemberInfo(user_name: String):MemberAuthInfo? {
        val memberAuthInfo = this.memberAuthInfoRepository.findByMemberId(user_name)
        if (memberAuthInfo != null) {
            return memberAuthInfo
        }
        return this.memberAuthInfoRepository.findByMemberEmail(user_name)
    }
}
