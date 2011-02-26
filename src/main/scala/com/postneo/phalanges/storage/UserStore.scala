package com.postneo
package phalanges
package storage

import phalanges.User

/**
 * A storage backend trait.
 */
trait UserStore {
    def loadUsersFromJSON(filename: String)
    def getUser(username: String): Option[User]
    def getAllUsers: Iterator[(String, User)]
}
