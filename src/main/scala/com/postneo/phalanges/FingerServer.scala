package com.postneo
/**
 * An experimental implementation of the original finger protocol (RFC 742) with Netty and Scala.
 *
 * @author Matt Croydon
 * @version 0.3.0-SNAPSHOT
 */
package phalanges

import com.yammer.metrics.Metrics

import java.net.InetSocketAddress
import java.util.concurrent.{CountDownLatch, Executors, TimeUnit}

import net.lag.configgy.{Configgy, ConfigMap}
import net.lag.logging.Logger

import org.jboss.netty.bootstrap.ServerBootstrap
import org.jboss.netty.channel.{ChannelPipeline, ChannelPipelineFactory, Channels}
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory
import org.jboss.netty.handler.codec.string.{StringDecoder, StringEncoder}
import org.jboss.netty.util.CharsetUtil

/**
 * A Finger daemon.
 */
object FingerServer {

    private val log = Logger.get(getClass.getName)
    private val deathSwitch = new CountDownLatch(1)
    
    Configgy.configure("config/phalanges.conf")
    val config = Configgy.config
    
    def main(args: Array[String]) = {
        log.info("Starting phalanges.")
        start(config)
    }
    
    def start(config: ConfigMap) {
        val enable_console = config.getBool("metrics.enable_console", false)
        
        if (enable_console) {
            Metrics.enableConsoleReporting(config.getInt("metrics.console_interval", 30), TimeUnit.SECONDS)
        }
        
        val executor = Executors.newCachedThreadPool()
        val factory = new NioServerSocketChannelFactory(executor, executor)
        val bootstrap = new ServerBootstrap(factory)
        val handler = new FingerHandler(config)
        val pipeline = bootstrap.getPipeline()
        // Encode end decode messages to ASCII
        pipeline.addLast("encoder", new StringEncoder(CharsetUtil.US_ASCII))
        pipeline.addLast("decoder", new StringDecoder(CharsetUtil.US_ASCII))
        pipeline.addLast("handler", handler)
        bootstrap.bind(new InetSocketAddress(config("host").toString, config("port").toInt))
    }
    
    def start() {
        start(config)
    }
    
    def shutdown() {
        log.info("Shutting down phalanges.")
        // TODO: Gracefully shut down Netty?
        System.exit(0)
    }
}
