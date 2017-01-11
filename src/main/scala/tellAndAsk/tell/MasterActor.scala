package tellAndAsk.tell

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
    case "start" => pingActor ! pongActor
  }

  override def postStop(): Unit = log.info("masterActor going down")

}
