/*
 * Copyright (C) 2018 The lgrcyanny, Inc. All Rights Reserved.
 * Authors: liangguorong@gmail.com
 * Description: grpc HelloServer
 */

package com.cyanny.learning.grpc

import java.util.concurrent.atomic.AtomicReference

import com.cyanny.learning.grpc.hellodemo.{PingPongGrpc, PingRequest, PongResponse}
import io.grpc.{Server, ServerBuilder}
import io.grpc.stub.StreamObserver

class HelloServer(port: Int) {
  import HelloServer._
  val helloServer = new AtomicReference[Server](null)

  def startup(): Unit = {
    /* The port on which the server should run */
    val server = ServerBuilder.forPort(port).addService(new HelloServerStubImp).build.start
    println("Server started, listening on " + port)
    Runtime.getRuntime.addShutdownHook(new Thread {
      override def run(): Unit =  {
        println("shutting down gRPC server since JVM is shutting down")
        HelloServer.this.shutdown()
        println("server shut down")
      }
    })
    helloServer.set(server)
  }

  def shutdown(): Unit = {
    if (helloServer.get() != null) {
      helloServer.get().shutdown()
      println("server shutdown")
    }
  }

  def blockUntilShutdown(): Unit = {
    if (helloServer.get() != null) {
      helloServer.get().awaitTermination()
    }
  }

}

object HelloServer {
  class HelloServerStubImp extends PingPongGrpc.PingPongImplBase {
    override def ping(request: PingRequest,
                      responseObserver: StreamObserver[PongResponse]): Unit = {
      val pongResponseBuilder = PongResponse.newBuilder()
      pongResponseBuilder.setMessage(s"Got message: ${request.getMessage}")
      responseObserver.onNext(pongResponseBuilder.build())
      responseObserver.onCompleted()
    }
  }

  def main(args: Array[String]): Unit = {
    val port = if (args.length >= 1) {
      args(0).toInt
    } else {
      8088
    }
    val server = new HelloServer(port)
    server.startup()
    server.blockUntilShutdown()
  }
}