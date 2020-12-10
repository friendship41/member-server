package com.friendship41.memberserver.data

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Document
data class MemberFriend(
        var memberNo: Int?,
        var friendMemberNo: Int
)

@Repository
interface MemberFriendRepository: MongoRepository<MemberFriend, String>

data class ReqBodyPostFriend(
        var memberFriendList: List<MemberFriend>?
)
