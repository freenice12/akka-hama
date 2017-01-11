package tellAndAsk.ask

import akka.actor.Props
import tellAndAsk.MySystem.mySystem

/**
  * Created by freenice12 on 2017-01-11.
  */
object CommunicatingAsk extends App {

  val master = mySystem.actorOf(Props[MasterActor], "master")

  master ! "start"
  Thread.sleep(3000)
  mySystem.terminate()

}
