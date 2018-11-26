/*
 * Copyright (C) 2018 The lgrcyanny, Inc. All Rights Reserved.
 * Authors: liangguorong@gmail.com
 * Description: grpc HelloClient
 */
package com.cyanny.learning.grpc

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

import com.cyanny.learning.grpc.hellodemo.PingPongGrpc.PingPongBlockingStub
import com.cyanny.learning.grpc.hellodemo.{PingPongGrpc, PingRequest}
import io.grpc.{ManagedChannel, ManagedChannelBuilder}

class HelloClient(host: String, port: Int) {
  val clientStub = new AtomicReference[PingPongBlockingStub](null)
  val channel = new AtomicReference[ManagedChannel](null)
  createBlockingStub()

  def createBlockingStub() = {
    val channel = ManagedChannelBuilder
      .forAddress(host, port)
      .usePlaintext // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
      // needing certificates.
      .build
    val clientStub = PingPongGrpc.newBlockingStub(channel)
    this.clientStub.set(clientStub)
    this.channel.set(channel)
  }

  def ping(message: String): Unit = {
    val request = PingRequest.newBuilder()
    request.setMessage(message)
    val tic = System.currentTimeMillis()
    val response = clientStub.get().ping(request.build())
    val toc = System.currentTimeMillis()
    println(s"Got response: ${response}, time taken: ${toc - tic}ms")
  }

  def shutdown(): Unit = {
    if (channel.get() != null) {
      channel.get().shutdown().awaitTermination(5, TimeUnit.MILLISECONDS)
    }
  }

}

object HelloClient {

  def main(args: Array[String]): Unit = {
    val host = if (args.length >= 1) args(0) else "localhost"
    val port = if (args.length >= 2) args(1).toInt else 8088
    val client = new HelloClient(host, port)
    val n = 10000
    val tic = System.currentTimeMillis()
    (1 to n).foreach { i =>
      client.ping(s"ping: $i")
    }
    val toc = System.currentTimeMillis()
    println(s"done time taken: ${toc - tic}ms")
    client.shutdown()
  }

}
