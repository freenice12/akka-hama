package lifecycle

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging

/**
  * Created by freenice12 on 2017-01-11.
  */
class LifecycleActor extends Actor {

  val log = Logging(context.system, this)
  var child: ActorRef = _

  override def receive = {
    case num: Double => log.info(s"got a double - $num")
    case num: Int => log.info(s"got an integer - $num")
    case list: List[_] => log.info(s"list - ${list.head}, ...")
    case txt: String => child ! txt
  }

  override def preStart(): Unit = {
    log.info("about to start")
    child = context.actorOf(Props[StringPrinter], "kiddo")
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info(s"about to restart because of $reason, during message $message")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    log.info(s"just restarted due to $reason")
    super.postRestart(reason)
  }

  override def postStop(): Unit = log.info("just stopped")

}

class StringPrinter extends Actor {
  val log = Logging(context.system, this)

  override def receive = {
    case msg => log.info(s"child got message: $msg")
  }

  override def preStart(): Unit = log.info("child about to start.")
  override def postStop(): Unit = log.info("child just stopped.")

}


object ActorsLifecycle extends App {
  val ourSystem = ActorSystem("ourSystem")
  val lifecycleActor = ourSystem.actorOf(Props[LifecycleActor], "lifecycleActor")

  lifecycleActor ! math.Pi
  Thread.sleep(1000)
  lifecycleActor ! 7
  Thread.sleep(1000)
  lifecycleActor ! "hi there!"
  Thread.sleep(1000)
  lifecycleActor ! List(1,2,3)
  Thread.sleep(1000)

  // The actor caught exception
  // then: preRestart -> postStop(after children stopped)
  //       -> postRestart -> preStart
  lifecycleActor ! Nil
  Thread.sleep(1000)

  lifecycleActor ! "sorry about that"
  Thread.sleep(1000)

  // Actor stopped after children stopped
  ourSystem.stop(lifecycleActor)
  Thread.sleep(1000)

  ourSystem.terminate()
}

