package become

import akka.actor.Actor
import akka.event.Logging

/**
  * simple become example
  *
  * Created by freenice12 on 2017-01-11.
  */
class CountdownActor extends Actor {

  val log = Logging(context.system, this)

  var n = 10

  // anti design
//  override def receive = if (n > 0) {case "count" => ... }

  def counting: Actor.Receive = {
    case "count" => {
      n -= 1
      log.info(s"n = $n")
      if (n == 0) context.become(done)
    }
  }

  def done = PartialFunction.empty
  def receive = counting

}
