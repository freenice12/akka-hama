package hello

import akka.actor.{ ActorRef, ActorSystem, Props, Actor }
import akka.event.Logging


class Hello(val hello: String) extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case `hello` => log.info(s"Received a $hello!")
    case msg   => {
      log.info(s"Unexpected message '$msg'")
      context.stop(self)
    }
  }
}


object ActorsCreate extends App {

  val mySystem = ActorSystem("mysystem")

  val hiActor : ActorRef = mySystem.actorOf(Props(new Hello("hi")), name = "greeter")

  hiActor ! "hi"
  Thread.sleep(1000)

  hiActor ! 3
  Thread.sleep(1000)

  mySystem.terminate()
}