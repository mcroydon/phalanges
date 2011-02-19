package com.postneo
package phalanges

import net.lag.configgy.Configgy

import scala.util.parsing.json.JSON
import scala.collection.mutable.ListBuffer

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
    
    /**
     * Load users from a JSON file whose location is configured via Configgy.
     */
    def getUsersFromJSON() = {
        // TODO: This is far too fragile in too many places.  Handle potentially malformed JSON better.
        //       This should also get better when internal storage is something closer to Map.
        Configgy.configure("config/phalanges.conf")
        val userJSONLocation = Configgy.config.getString("user_json", "config/users.json")
        val parsedUsers = JSON.parseFull(io.Source.fromFile(userJSONLocation).mkString)
        val buffer: ListBuffer[User] = new ListBuffer
        for (item <- parsedUsers.get.asInstanceOf[List[Map[String, String]]]) {
            buffer += new User(item.getOrElse("username", "default"), item.getOrElse("name", "Default User"), item.getOrElse("plan", "No plan."))
        }
        buffer.toList
    }
    
    val users = getUsersFromJSON()
    
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
        "The user " + username + " was not found." + Util.CRLF
    }

}
