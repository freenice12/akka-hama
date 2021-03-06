package distDown

import akka.actor._
import akka.event.Logging
import akka.actor.SupervisorStrategy._
import org.apache.commons.io.FileUtils
import scala.io.Source
import scala.collection._
import scala.concurrent.duration._
import tellAndAsk.MySystem.mySystem

/**
  * Created by freenice12 on 2017-01-11.
  */

class Downloader extends Actor {
  def receive = {
    case DownloadManager.Download(url, dest) =>
      val content = Source.fromURL(url)
      FileUtils.write(new java.io.File(dest), content.mkString, "UTF-8")
      sender ! DownloadManager.Finished(dest)
  }
}


class DownloadManager(val downloadSlots: Int) extends Actor {
  val log = Logging(context.system, this)
  val downloaders = mutable.Queue[ActorRef]()
  val pendingWork = mutable.Queue[DownloadManager.Download]()
  val workItems = mutable.Map[ActorRef, DownloadManager.Download]()

  override def preStart(): Unit = {
    for (i <- 0 until downloadSlots)
      downloaders.enqueue(context.actorOf(Props[Downloader], s"dl$i"))
  }

  private def checkMoreDownloads(): Unit = {
    if (pendingWork.nonEmpty && downloaders.nonEmpty) {
      val dl = downloaders.dequeue()
      val workItem = pendingWork.dequeue()
      log.info(s"$workItem starting, ${downloaders.size} download slots left")
      dl ! workItem
      workItems(dl) = workItem
    }
  }

  def receive = {
    case msg @ DownloadManager.Download(url, dest) =>
      pendingWork.enqueue(msg)
      checkMoreDownloads()
    case DownloadManager.Finished(dest) =>
      workItems.remove(sender)
      downloaders.enqueue(sender)
      log.info(s"Down to '$dest' finished, ${downloaders.size} down slots left")
      checkMoreDownloads()
  }

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 6, withinTimeRange = 30 seconds) {
      case fnf: java.io.FileNotFoundException =>
        log.info(s"Resource could not be found: $fnf")
        workItems.remove(sender)
        downloaders.enqueue(sender)
        Resume
      case _ =>
        Escalate
    }
}


object DownloadManager {
  case class Download(url: String, dest: String)
  case class Finished(dest: String)
}


object SupervisionDownloader extends App {
  import DownloadManager._
  val manager = mySystem.actorOf(Props(classOf[DownloadManager], 4), "manager")
  manager ! Download("http://www.w3.org/Addressing/URL/url-spec.txt", "url-spec.txt")
  manager ! Download("http://www.w3.org/Addressing/URL/url-spec.txt", "url-spec.txt")
  manager ! Download("http://www.w3.org/Addressing/URL/url-spec.txt", "url-spec.txt")
  manager ! Download("http://www.w3.org/Addressing/URL/url-spec.txt", "url-spec.txt")
  manager ! Download("http://www.w3.org/Addressing/URL/url-spec.txt", "url-spec.txt")
  manager ! Download("http://www.w3.org/Addressing/URL/url-spec.txt", "url-spec.txt")
  manager ! Download("http://www.w3.org/Addressing/URL/url-spec.txt", "url-spec.txt")

  Thread sleep 1000
//  manager ! Download("https://github.com/scala/scala/blob/master/README.md", "README.md")
  Thread.sleep(5000)
  mySystem.stop(manager)
  Thread.sleep(5000)
  mySystem terminate
}