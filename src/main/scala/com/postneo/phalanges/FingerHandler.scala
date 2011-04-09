package com.postneo
package phalanges

import phalanges.storage.InMemoryUserStore

import java.util.concurrent.TimeUnit
import com.yammer.metrics.Instrumented

import net.lag.configgy.ConfigMap
import net.lag.logging.Logger

import org.jboss.netty.buffer.ChannelBuffer
import org.jboss.netty.channel.{ChannelHandlerContext, ExceptionEvent, MessageEvent, SimpleChannelUpstreamHandler}

class FingerHandler(config: ConfigMap) extends SimpleChannelUpstreamHandler with Instrumented {
    
    private val log = Logger.get(getClass.getName)
    
    private val indexesMeter = metrics.meter("indexes", "served", TimeUnit.SECONDS)
    private val usersMeter = metrics.meter("users", "served", TimeUnit.SECONDS)
    
    private val storage = new InMemoryUserStore
    storage.loadUsersFromJSON(config.getString("users_json", "config/user_map.json"))

    override def messageReceived(context: ChannelHandlerContext, e: MessageEvent): Unit = {
        val channel = e.getChannel()
        // Strip any leading or trailing whitespace
        val message = e.getMessage().asInstanceOf[String].trim
        
        message match {
            case Util.CRLF | "" => channel.write(index())
            case username: String => channel.write(user(username))
            case _ => channel.write(error() + index())
        }
        channel.close
    }
    
    override def exceptionCaught(context: ChannelHandlerContext, e: ExceptionEvent): Unit =  {
        log.warning("Exception caught: " + e)
        e.getChannel().close()
    }

    def index() = {
        log.debug("Index requested.")
        indexesMeter.mark(1)
        var response = Util.pad("Login") + Util.TAB + Util.pad("Name") + Util.CRLF
        for (u <- storage.getAllUsers) {
            response +=  u._2.to_index_string()
        }
        response
    }
    
    def user(username: String) = {
        log.debug("User " + username + " requested.")
        usersMeter.mark(1)
        val user = storage.getUser(username)
        user match {
            case None => "The user " + username + " was not found."
            case Some(u) => u.to_detail_string
        }
    }
    
    def error() = {
        log.warning("Request not understood.")
        "Your request was not understood." + Util.CRLF + Util.CRLF
    }
}
