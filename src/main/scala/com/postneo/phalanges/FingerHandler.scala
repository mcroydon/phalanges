package com.postneo
package phalanges

import com.twitter.stats.Stats

import net.lag.configgy.Configgy
import net.lag.logging.Logger

import org.jboss.netty.buffer.ChannelBuffer
import org.jboss.netty.channel.{ChannelHandlerContext, ExceptionEvent, MessageEvent, SimpleChannelUpstreamHandler}

class FingerHandler extends SimpleChannelUpstreamHandler {
    
    private val config = Configgy.config
    private val log = Logger.get(getClass.getName)

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
        Stats.incr("indexes_served")
        Users.index()
    }
    
    def user(username: String) = {
        log.debug("User " + username + " requested.")
        Stats.incr("users_served")
        Users.user(username)
    }
    
    def error() = {
        log.warning("Request not understood.")
        "Your request was not understood." + Util.CRLF + Util.CRLF
    }
}
