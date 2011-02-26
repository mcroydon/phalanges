package com.postneo
package phalanges

import net.lag.configgy.ConfigMap

import org.specs.Specification
import org.specs.mock.Mockito
import org.mockito.Matchers._

import org.jboss.netty.channel.{Channel, ChannelHandlerContext, ChannelFuture, MessageEvent}

object FingerHandlerSpec extends Specification with Mockito {
    val mockChannelFuture = mock[ChannelFuture]
    val mockChannel = mock[Channel]
    mockChannel.write(anyString()) returns mockChannelFuture
    val mockChannelHandlerContext = mock[ChannelHandlerContext]
    val mockConfigMap = mock[ConfigMap]
    mockConfigMap.getString("users_json", "config/user_map.json") returns "config/user_map.json"
    
    def mockMessageEventFactory(str: String) = {
        val mockMessageEvent = mock[MessageEvent]
        mockMessageEvent.getMessage() returns str
        mockMessageEvent.getChannel() returns mockChannel
        mockMessageEvent
    }
    
    "FingerHandler" should {
        "write to a channel" in {
            val mockMessageEvent = mockMessageEventFactory(Util.CRLF)
            val handler = new FingerHandler(mockConfigMap)
            handler.messageReceived(mockChannelHandlerContext, mockMessageEvent)
            there was one(mockChannel).write(anyString())
        }
        
        "strip whitespace around usernames before lookup" in {
            val mockMessageEvent = mockMessageEventFactory("  mcroydon  " + Util.CRLF)
            val handler = new FingerHandler(mockConfigMap)
            handler.messageReceived(mockChannelHandlerContext, mockMessageEvent)
            there was one(mockChannel).write(handler.user("mcroydon"))
        }
        
        "properly routes index requests" in {
            val mockMessageEvent = mockMessageEventFactory(Util.CRLF)
            val handler = new FingerHandler(mockConfigMap)
            handler.messageReceived(mockChannelHandlerContext, mockMessageEvent)
            there was one(mockChannel).write(handler.index())
        }
        
        "properly routes known user requests" in {
            val mockMessageEvent = mockMessageEventFactory("mcroydon" + Util.CRLF)
            val handler = new FingerHandler(mockConfigMap)
            handler.messageReceived(mockChannelHandlerContext, mockMessageEvent)
            there was one(mockChannel).write(handler.user("mcroydon"))
        }
        
        "properly routes unknown user requests" in {
            val mockMessageEvent = mockMessageEventFactory("unknown" + Util.CRLF)
            val handler = new FingerHandler(mockConfigMap)
            handler.messageReceived(mockChannelHandlerContext, mockMessageEvent)
            there was one(mockChannel).write(handler.user("unknown"))
        }
        
    }
}
