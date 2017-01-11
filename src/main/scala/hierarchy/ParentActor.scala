package hierarchy

import akka.actor.{Actor, Props}
import akka.event.Logging

/**
  * Created by freenice12 on 2017-01-10.
  */
class ParentActor extends Actor {

  val log = Logging(context.system, this)

  override def receive = {
    case "create" =>
      context.actorOf(Props[ChildActor])
      log.info(s"created a new child - children = ${context.children}")

    case "hi" =>
      log.info("Kids, say hi!")
      for (c <- context.children) c ! "hi"

    case "stop" =>
      log.info("parent stopping")
      context.stop(self)
  }

}
