package remote.BFir

import akka.actor.Actor
import akka.event.Logging

/**
  * Created by freenice12 on 2017-01-11.
  */
class PongBActor extends Actor {

  val log = Logging(context.system, this)

  override def receive = {
    case "ping" => {
      log.info("Got a ping -- pong will back!")
      sender ! "pong"
      context.stop(self)
    }
  }

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = log.info("pongBActor going down")

}
