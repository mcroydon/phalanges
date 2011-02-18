package com.postneo
package phalanges

import org.specs.Specification

object UsersSpec extends Specification {
    "Users" should {
        
        "end their responses in CRLF" in {
            val eol = Util.CRLF + "$"
            Users.index() must be matching eol
            Users.user("unknown") must be matching eol
            Users.user("mcroydon") must be matching eol
        }
        
        "tell us when a user cannot be found" in {
            Users.user("unknown") must be matching "not found"
        }
    }
}
