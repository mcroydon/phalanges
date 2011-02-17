package com.postneo
package phalanges

class User(uname: String, name: String, plan: String) {
    val username = uname
    
    def to_index_string() = {
        Util.pad(username) + Util.TAB + Util.pad(name) + Util.CRLF
    }

    def to_detail_string() = {
        to_index_string() + "Plan:" + Util.CRLF +  plan + Util.CRLF
    }

}

/**
 * A simple object-based user store.
 */
object Users {

    // TODO: Read from disk (Configgy, JSON, or similar).
    val users: List[User] = List(new User("mcroydon", "Matt Croydon", "Implement archaic protocols."))
    
    def index() = {
        var response = Util.pad("Login") + Util.TAB + Util.pad("Name") + Util.CRLF
        for (u <- users) {
            response +=  u.to_index_string()
        }
        response
    }
    
    // TODO: Replace with Map for less brute force user lookup.
    def user(username: String): String = {
        for (u <- users) {
            if (u.username == username) return u.to_detail_string()
        }
        render_not_found(username)
    }
    
    def render_not_found(username: String) = {
        "The user " + username + " was not found."
    }

}