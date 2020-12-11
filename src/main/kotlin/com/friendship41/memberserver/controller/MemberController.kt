//package com.friendship41.memberserver.controller
//
//import com.friendship41.memberserver.data.ReqBodyRegisterCommonMember
//import com.friendship41.memberserver.service.CommonMemberService
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.security.core.userdetails.User
//import org.springframework.web.bind.annotation.PostMapping
//import org.springframework.web.bind.annotation.RequestBody
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//
//@RestController
//@RequestMapping("/member")
//class MemberController(@Autowired private val commonMemberService: CommonMemberService) {
//    @RequestMapping("common")
//    fun getMember(): Any? {
//        val authInfo = SecurityContextHolder.getContext().authentication.principal as User
//        return this.commonMemberService.getDefaultMemberInfo(authInfo.username.toInt())
//    }
//}
//
//@RestController
//class RegisterController(@Autowired private val commonMemberService: CommonMemberService) {
//    @PostMapping("/register")
//    fun postRegister(@RequestBody reqBodyRegisterCommonMember: ReqBodyRegisterCommonMember): Any? {
//        return commonMemberService.registerCommonMember(
//                reqBodyRegisterCommonMember.commonMemberInfo,
//                reqBodyRegisterCommonMember.memberAuthInfo)
//    }
//}
