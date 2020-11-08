package com.friendship41.memberserver.controller

import com.friendship41.memberserver.data.MemberAuthInfo
import com.friendship41.memberserver.service.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/member")
class MemberController(@Autowired private val memberService: MemberService) {
    @RequestMapping("")
    fun getMember(): Any? {
        val authInfo = SecurityContextHolder.getContext().authentication.principal as User
        return this.memberService.getDefaultMemberInfo(authInfo.username.toInt())
    }
}

@RestController
class RegisterController(@Autowired private val memberService: MemberService) {
    @PostMapping("/register")
    fun postRegister(@RequestBody memberAuthInfo: MemberAuthInfo): Any {
        return this.memberService.registerDefailtMember(memberAuthInfo)
    }
}
