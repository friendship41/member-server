package com.friendship41.memberserver.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Document
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
interface CommonMemberInfoRepository: MongoRepository<CommonMemberInfo, Int>

data class ReqBodyRegisterCommonMember(
        var memberAuthInfo: MemberAuthInfo,
        var commonMemberInfo: CommonMemberInfo
)

data class MemberAuthInfo(
        var memberPassword: String,
        var memberRole: String?
)
