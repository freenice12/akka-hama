package remote.ASec

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * Created by freenice12 on 2017-01-11.
  */
object RemotingPingAActorSystem extends App {

  def remotingConfig(port: Int) = ConfigFactory.parseString(
    s"""
       akka {
         actor.provider = "akka.remote.RemoteActorRefProvider"
         remote {
           enabled-transports = ["akka.remote.netty.tcp"]
           netty.tcp {
             hostname = "127.0.0.1"
             port = $port
           }
         }
       }
     """
  )

  def remotingSystem(name: String, port: Int):ActorSystem = ActorSystem(name, remotingConfig(port))

  val system = remotingSystem("PingADimention", 23456)
  val runner = system.actorOf(Props[Runner], "runner")
  runner ! "start"
  Thread sleep(2000)
  system.terminate
}
