package become

import akka.actor.Props
import tellAndAsk.MySystem._

/**
  * Created by freenice12 on 2017-01-11.
  */
object ActorsBecome extends App {
  val dict = mySystem.actorOf(Props[DictionaryActor], "dictionary")
  dict ! DictionaryActor.IsWord("program")
  Thread.sleep(1000)
  dict ! DictionaryActor.Init("words.txt")
  Thread.sleep(1000)
  dict ! DictionaryActor.IsWord("program")
  Thread.sleep(1000)
  dict ! DictionaryActor.IsWord("balaban")
  Thread.sleep(1000)
  dict ! DictionaryActor.IsWord("balaban123")
  Thread.sleep(1000)
  dict ! DictionaryActor.End
  Thread.sleep(1000)
  dict ! DictionaryActor.IsWord("termination")
  Thread.sleep(1000)
  mySystem.shutdown()
}

