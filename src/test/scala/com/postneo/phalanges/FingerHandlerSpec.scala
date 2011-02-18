package com.postneo
package phalanges

import org.specs.Specification
import org.specs.mock.Mockito
import org.mockito.Matchers._

import org.jboss.netty.channel.{Channel, ChannelHandlerContext, ChannelFuture, MessageEvent}

object FingerHandlerSpec extends Specification with Mockito {
    val mockChannelFuture = mock[ChannelFuture]
    val mockChannel = mock[Channel]
    mockChannel.write(anyString()) returns mockChannelFuture
    val mockChannelHandlerContext = mock[ChannelHandlerContext]
    
    "FingerHandler" should {
        "write to a channel" in {
            val mockMessageEvent = mock[MessageEvent]
            mockMessageEvent.getMessage() returns Util.CRLF
            mockMessageEvent.getChannel() returns mockChannel
            val handler = new FingerHandler
            handler.messageReceived(mockChannelHandlerContext, mockMessageEvent)
            there was one(mockChannel).write(anyString())
        }
        
        "strip whitespace around usernames before lookup" in {
            val mockMessageEvent = mock[MessageEvent]
            mockMessageEvent.getMessage() returns "  mcroydon  " + Util.CRLF
            mockMessageEvent.getChannel() returns mockChannel
            val handler = new FingerHandler
            handler.messageReceived(mockChannelHandlerContext, mockMessageEvent)
            there was one(mockChannel).write(handler.user("mcroydon"))
        }
        
        
        "properly routes index requests" in {
            val mockMessageEvent = mock[MessageEvent]
            mockMessageEvent.getMessage() returns Util.CRLF
            mockMessageEvent.getChannel() returns mockChannel
            val handler = new FingerHandler
            handler.messageReceived(mockChannelHandlerContext, mockMessageEvent)
            there was one(mockChannel).write(handler.index())
        }
        
        "properly routes known user requests" in {
            val mockMessageEvent = mock[MessageEvent]
            mockMessageEvent.getMessage() returns "mcroydon" + Util.CRLF
            mockMessageEvent.getChannel() returns mockChannel
            val handler = new FingerHandler
            handler.messageReceived(mockChannelHandlerContext, mockMessageEvent)
            there was one(mockChannel).write(handler.user("mcroydon"))
        }
        
        "properly routes unknown user requests" in {
            val mockMessageEvent = mock[MessageEvent]
            mockMessageEvent.getMessage() returns "unknown" + Util.CRLF
            mockMessageEvent.getChannel() returns mockChannel
            val handler = new FingerHandler
            handler.messageReceived(mockChannelHandlerContext, mockMessageEvent)
            there was one(mockChannel).write(handler.user("unknown"))
        }
        
    }
}