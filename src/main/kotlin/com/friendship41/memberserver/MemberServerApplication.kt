package com.friendship41.memberserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MemberServerApplication

fun main(args: Array<String>) {
    runApplication<MemberServerApplication>(*args)
}
