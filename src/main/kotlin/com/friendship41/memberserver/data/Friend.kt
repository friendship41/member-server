package com.friendship41.memberserver.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class MemberFriend(
        @Id
        var memberNo: Int,
        var friendMemberNo: Int
)

@Repository
interface MemberFriendRepository: JpaRepository<MemberFriend, Int>

data class ReqBodyPostFriend(
        var memberFriendList: List<MemberFriend>?
)
