package io.echo

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, Props, SupervisorStrategy}
import akka.io.{IO, Tcp}

/**
  * Created by freenice12 on 2017-01-11.
  */
class EchoManager(handlerClass: Class[_]) extends Actor with ActorLogging {

  import akka.io.Tcp._
  import context.system

  // there is not recovery for broken connections
  override def supervisorStrategy: SupervisorStrategy = SupervisorStrategy.stoppingStrategy

  // bind to the listen port
  // the port will automatically be closed once this actor dies
  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 0))

  @scala.throws[Exception](classOf[Exception])
  override def postRestart(reason: Throwable): Unit = context stop self

  // do not restart
  override def receive = {
    case Bound(localAddress) => log.info("listening on port {}", localAddress.getPort)
    case CommandFailed(Bind(_, local, _, _, _)) => {
      log.warning(s"cannot bind to [$local]")
      context stop self
    }
      // #echo-manager
    case Connected(remote, local) => {
      log.info(s"received connection from $remote")
      val handler = context.actorOf(Props(handlerClass, sender(), remote))
      sender() ! Register(handler, keepOpenOnPeerClosed = true)
    }
      // #echo-manager
  }

}
