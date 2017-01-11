package tellAndAsk.gracefulDown

import akka.actor.Status.{Failure, Success}
import akka.actor.{Actor, Props, Terminated}
import akka.event.Logging
import tellAndAsk.MySystem._
import tellAndAsk.ask.PongActor
import akka.pattern.{ask, pipe}
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by freenice12 on 2017-01-11.
  */
class GracefulPingActor extends Actor {

  val pongActor = context.actorOf(Props[PongActor], "pongActor")
  // watching pongActor
  // after postStop(pongActor) ->
  // GracefulPingActor has got Terminated(`pongActor`) -> stop
  context.watch(pongActor)

  override def receive = {
    case GracefulPingActor.CustomShutdown => context.stop(pongActor)
    case Terminated(`pongActor`) => context.stop(self)
  }

}

object GracefulPingActor {
  object CustomShutdown
}


//object CommunicatingGracefulStop extends App {
//  val grace = mySystem.actorOf(Props[GracefulPingActor], "grace")
//  val stopped = gracefulStop(grace, 3 seconds, GracefulPingActor.CustomShutdown)
//  stopped onComplete {
//    case Success(x) =>
//      println("graceful shutdown successful")
//      mySystem.terminate()
//    case Failure(t) =>
//      println("grace not stopped!")
//      mySystem.terminate()
//  }
//}