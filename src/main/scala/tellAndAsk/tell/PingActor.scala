package tellAndAsk.tell

import akka.actor.{Actor, ActorRef}
import akka.event.Logging

/**
  * Created by freenice12 on 2017-01-11.
  */
class PingActor extends Actor {

  val log = Logging(context.system, this)

  override def receive = {
    case pongActorRef: ActorRef => pongActorRef ! "ping"
    case "pong" => {
      log.info("I've got pong back!")
      context.stop(self)
    }
  }

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = log.info("pingActor going down")

}
