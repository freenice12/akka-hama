package remote.ASec

import akka.actor.{Actor, ActorRef}
import akka.pattern.{ask, pipe}
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Created by freenice12 on 2017-01-11.
  */
class PingAActor extends Actor {

  override def receive = {
    case pongBRef: ActorRef => {
      implicit val timeout = Timeout(2 seconds)
      val future = pongBRef ? "ping"
      pipe(future) to sender
    }
  }

}
