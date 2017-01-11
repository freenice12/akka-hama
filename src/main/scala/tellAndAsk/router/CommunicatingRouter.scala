package tellAndAsk.router

import akka.actor.Props
import tellAndAsk.MySystem._

/**
  * Created by freenice12 on 2017-01-11.
  */
object CommunicatingRouter extends App {
  val router = mySystem.actorOf(Props[Router], "router")

  router ! "Hi."
  router ! "I'm talking to you!"
  Thread.sleep(1000)
  router ! "stop"
  Thread.sleep(1000)
  mySystem.terminate

}
