package tellAndAsk.router

import akka.actor.Actor
import akka.event.Logging

/**
  * Created by freenice12 on 2017-01-11.
  */
class StringPrinter extends Actor {

  val log = Logging(context.system, this)

  override def receive = {
    case msg => log.info(s"child got message: $msg")
  }

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = log.info(s"child about to start.")

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = log.info(s"child just stopped.")

}
