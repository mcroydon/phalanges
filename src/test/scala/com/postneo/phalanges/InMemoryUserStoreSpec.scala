package com.postneo
package phalanges

import phalanges.storage.InMemoryUserStore

import org.specs.Specification

object InMemoryUserStoreSpec extends Specification {
    "InMemoryUserStore" should {
    
        "start empty" in {
            val store = new InMemoryUserStore
            store.getAllUsers.length mustEqual 0
        }
        
        "load users from JSON" in {
            val store = new InMemoryUserStore

            store.loadUsersFromJSON("config/user_map.json")
            store.getAllUsers.length mustEqual 2

            val user: User = store.getUser("mcroydon").getOrElse(new User("notmcroydon", "Not Matt Croydon", ""))
            user.username mustEqual "mcroydon"
        }
    }
}
