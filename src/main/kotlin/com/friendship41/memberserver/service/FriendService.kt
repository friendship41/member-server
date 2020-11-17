package com.friendship41.memberserver.service

import com.friendship41.memberserver.data.MemberFriend
import com.friendship41.memberserver.data.MemberFriendRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FriendService(@Autowired private val memberFriendRepository: MemberFriendRepository) {
    fun saveMemberFriend(memberFriend: MemberFriend): MemberFriend = memberFriendRepository.save(memberFriend)
}
