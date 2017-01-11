package killStrategy

import akka.actor.SupervisorStrategy.{Escalate, Restart}
import akka.actor.{Actor, ActorKilledException, AllForOneStrategy, Kill, OneForOneStrategy, Props}
import akka.event.Logging
import tellAndAsk.MySystem.mySystem

/**
  * Created by freenice12 on 2017-01-11.
  */
class Naughty extends Actor {
  val log = Logging(context.system, this)
  def receive = {
    case s: String => log.info(s)
    case msg: Any => throw new RuntimeException
//    case mas => sender ! Kill
  }
  override def postRestart(t: Throwable) = log.info("naughty restarted")
}


class Supervisor extends Actor {
  val child = context.actorOf(Props[Naughty], "victim")
  val child2 = context.actorOf(Props[Naughty], "victim2")
  def receive = PartialFunction.empty
  override val supervisorStrategy =
    OneForOneStrategy() {
      case ake: ActorKilledException => Restart
      case _ => Escalate
    }
}

object SupervisionKill extends App {
  val supervisor = mySystem.actorOf(Props[Supervisor], "supervisor")
  mySystem.actorSelection("/user/supervisor/*") ! Kill
  mySystem.actorSelection("/user/supervisor/*") ! "sorry about that"
  Thread.sleep(1000)
  mySystem.actorSelection("/user/supervisor/*") ! "kaboom".toList
  Thread.sleep(1000)
  mySystem.stop(supervisor)
  Thread.sleep(1000)
  mySystem.terminate()
}