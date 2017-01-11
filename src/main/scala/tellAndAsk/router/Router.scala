package tellAndAsk.router

import akka.actor.{Actor, Props}
import akka.event.Logging

/**
  * Created by freenice12 on 2017-01-11.
  */
class Router extends Actor {

  val log = Logging(context.system, this)
  var i = 0
  val children = for (_ <- 0 until 4) yield context.actorOf(Props[StringPrinter])

  override def receive = {
    case "stop" => context.stop(self)
    case msg => {
      children(i) forward msg
      i = (i + 1) % 4
    }
  }

}
