package com.friendship41.memberserver.controller

import com.friendship41.memberserver.service.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/member")
class MemberController(@Autowired private val memberService: MemberService) {
    @RequestMapping("")
    fun getMember(): Any? {
        val detailMap =
                (SecurityContextHolder.getContext().authentication.details as OAuth2AuthenticationDetails)
                        .decodedDetails as Map<*, *>
        return this.memberService.getDefaultMemberInfo(detailMap["user_name"] as String)
    }
}
