package hierarchy

import akka.actor.Actor
import akka.event.Logging

/**
  * Created by freenice12 on 2017-01-10.
  */
class ChildActor extends Actor {

  val log = Logging(context.system, this)

  override def receive = {

    case "hi" =>
      val parent = context.parent
      log.info(s"my parent $parent made me say hi!")

  }

  override def postStop(): Unit = {
    log.info("child stopped!")
  }

}
