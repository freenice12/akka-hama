package tellAndAsk.ask

import akka.actor.Actor
import akka.event.Logging

/**
  * Created by freenice12 on 2017-01-11.
  */
class PongActor extends Actor {

  val log = Logging(context.system, this)

  override def receive = {
    case "ping" => {
      log.info("Got a ping -- will pong back")
      // sender is MasterActor - deal with "start", "pong" and "merong"
      sender ! "pong"
      context.stop(self)
    }
    case "merong" => {
      sender ! "merong"
      println("pong merong!")
      context.stop(self)
    }
  }

  override def postStop(): Unit = log.info("pongActor going down")

}
