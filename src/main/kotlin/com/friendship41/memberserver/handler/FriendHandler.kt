package com.friendship41.memberserver.handler

import com.friendship41.memberserver.data.MemberFriend
import com.friendship41.memberserver.data.MemberFriendRepository
import com.friendship41.memberserver.data.ReqBodyPostMemberFriend
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

@Component
class FriendHandler(@Autowired private val memberFriendRepository: MemberFriendRepository) {
    fun handlePost(request: ServerRequest): Mono<ServerResponse> = request
        .bodyToMono(ReqBodyPostMemberFriend::class.java)
        .flatMap(this::getOrCreateMemberFriend)
        .flatMap { memberFriendRepository.save(it) }
        .flatMap { ok().build() }


    private fun getOrCreateMemberFriend(postMemberFriend: ReqBodyPostMemberFriend): Mono<MemberFriend> =
        memberFriendRepository.findById(postMemberFriend.memberNo)
            .switchIfEmpty(Mono.just(MemberFriend(postMemberFriend.memberNo, mutableSetOf())))
            .doOnNext { it.friendMemberNoSet.add(postMemberFriend.friendMemberNo) }
}
