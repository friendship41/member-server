package com.friendship41.memberserver.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Document
data class MemberFriend(
        @Id
        var memberNo: Int,
        var friendMemberNoSet: MutableSet<Int>
)

@Repository
interface MemberFriendRepository: ReactiveMongoRepository<MemberFriend, Int>

data class ReqBodyPostMemberFriend(
        val memberNo: Int,
        val friendMemberNo: Int
)
