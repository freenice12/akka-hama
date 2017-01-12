package io.echo

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._

/**
  * Created by freenice12 on 2017-01-11.
  */
object EchoServer extends App {

  val config = ConfigFactory.parseString("akka.loglevel = DEBUG")
  implicit val system = ActorSystem("EchoServer", config)

  try run()
  finally system.terminate()

  def run(): Unit = {
    import akka.actor.ActorDSL._

    val watcher = inbox()
    watcher.watch(system.actorOf(Props(classOf[EchoManager], classOf[EchoHandler]), "echo"))
    watcher.watch(system.actorOf(Props(classOf[EchoManager], classOf[SimpleEchoHandler]), "simple"))
    watcher.receive(10.minutes)

  }

}
