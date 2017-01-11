package remote.BFir

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * Created by freenice12 on 2017-01-11.
  */
object RemotingPongBActorSystem extends App {

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

  def remotingSystem(name: String, port: Int) = ActorSystem(name, remotingConfig(port))

  val system = remotingSystem("PongBDimension", 23455)
  val pongBActor = system.actorOf(Props[PongBActor], "pongBActor")
  Thread.sleep(30000)
  system.terminate()

}
