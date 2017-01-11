package tellAndAsk.tell

import akka.actor.Props
import tellAndAsk.MySystem.mySystem

/**
  * Created by freenice12 on 2017-01-11.
  */
object CommunicatingTell extends App {

  val master = mySystem.actorOf(Props[MasterActor], "master")

  master ! "start"
  Thread.sleep(3000)
  mySystem.terminate()

}
