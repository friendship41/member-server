package com.friendship41.memberserver.controller

import com.friendship41.memberserver.common.BusinessException
import com.friendship41.memberserver.common.CommonErrorCode
import com.friendship41.memberserver.data.ReqBodyPostFriend
import com.friendship41.memberserver.service.FriendService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/friend")
class FriendController(@Autowired private val friendService: FriendService) {
    @PostMapping("memberNo")
    fun postMemberFriend(@RequestBody reqBodyPostFriend: ReqBodyPostFriend) =
            reqBodyPostFriend.memberFriendList?.forEach(friendService::saveMemberFriend)
                    ?: throw BusinessException(CommonErrorCode.INVALID_INPUT_VALUE.toErrorResponse())
}
