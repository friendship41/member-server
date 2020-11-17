package com.friendship41.memberserver.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class CommonMemberInfo(
        @Id
        var memberNo: Int?,
        var memberId: String?,
        var memberEmail: String?,
        var memberName: String?,
        var joinDate: Date?,
        var joinPath: String?,
        var joinClient: String?
)

@Repository
interface CommonMemberInfoRepository: JpaRepository<CommonMemberInfo, Int>

data class ReqBodyRegisterCommonMember(
        var memberAuthInfo: MemberAuthInfo,
        var commonMemberInfo: CommonMemberInfo
)

data class MemberAuthInfo(
        var memberPassword: String,
        var memberRole: String?
)
