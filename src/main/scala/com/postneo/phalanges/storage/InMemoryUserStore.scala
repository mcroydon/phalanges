package com.postneo
package phalanges
package storage

import phalanges.User

import net.lag.logging.Logger

import scala.collection.mutable.HashMap

import scala.util.parsing.json.JSON

class InMemoryUserStore extends UserStore {

    private val log = Logger.get(getClass.getName)

    private val users = new HashMap[String, User]
    
    def loadUsersFromJSON(filename: String) = {
        log.debug("Loading users from " + filename)
        val parsedUsers = JSON.parseFull(io.Source.fromFile(filename).mkString)
        // TODO: Loading still fragile, needs to check for well-formed data before parsing.
        for (item <- parsedUsers.get.asInstanceOf[Map[String, Map[String, String]]]) {
            log.debug("Loading user data: " + item)
            // TODO: Is there a more idiomatic way of accessing data within item?
            users += item._1 -> new User(item._1, item._2.getOrElse("name", "N/A"), item._2.getOrElse("plan", ""))
        }
    }
    
    def getUser(username: String) = {
        users.get(username)
    }
    
    def getAllUsers = {
        users.iterator
    }
}