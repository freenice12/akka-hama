package remote.ASec

import akka.actor.{Actor, ActorIdentity, Identify, Props}
import akka.event.Logging

/**
  * Created by freenice12 on 2017-01-11.
  */
class Runner extends Actor {

  val log = Logging(context.system, this)
  val pingAActor = context.actorOf(Props[PingAActor], "pingAActor")

  override def receive = {
    case "start" => {
      val path = context.actorSelection("akka.tcp://PongBDimension@127.0.0.1:23455/user/pongBActor")
      path ! Identify(0)
    }
    case ActorIdentity(0, Some(ref)) => pingAActor ! ref
    case ActorIdentity(0, None) => {
      log.info("Something's wrong -- no pongBActor anywhere!")
      context.stop(self)
    }
    case "pong" => {
      log.info("got a pong from another dimension.")
      context.stop(self)
    }
  }

}
