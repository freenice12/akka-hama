package tellAndAsk.ask

import akka.actor.{Actor, ActorRef}
import akka.event.Logging
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration._

/**
  * Created by freenice12 on 2017-01-11.
  */
class PingActor extends Actor {

  val log = Logging(context.system, this)

  override def receive = {
    case pongActorRef: ActorRef => {
      implicit val timeout = Timeout(2 seconds)
      // pongActorRef can deal with "ping" and "merong"
      val future = pongActorRef ? "merong"
      println(future)
      pipe(future) to sender
    }

  }

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = log.info("pingActor going down")

}
