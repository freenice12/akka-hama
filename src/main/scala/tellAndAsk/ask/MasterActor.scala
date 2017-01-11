package tellAndAsk.ask

import akka.actor.{Actor, Props}
import akka.event.Logging
import tellAndAsk.MySystem.mySystem

/**
  * Created by freenice12 on 2017-01-11.
  */
class MasterActor extends Actor {

  val log = Logging(context.system, this)
  val pingActor = mySystem.actorOf(Props[PingActor], "pingActor")
  val pongActor = mySystem.actorOf(Props[PongActor], "pongActor")

  override def receive = {
    // MasterActor: be sender - to pingActor tell pongActor(ActorRef)
    case "start" => pingActor ! pongActor
    case "pong" => {
      log.info("got a pong back!")
      context.stop(self)
    }
    case "merong" => {
      log.info("got a merong back!")
      context.stop(self)
    }
  }

  override def postStop(): Unit = log.info("masterActor going down")

}
