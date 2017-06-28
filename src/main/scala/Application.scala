import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import slack.api.SlackApiClient

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object Application extends App{

  val actorSystem = ActorSystem("Slack")
  val conf = ConfigFactory.load()
  val token = conf.getString("accessToken")
  //created slack client
  val client = SlackApiClient(token)
  val allChannels = Await.result(client.listChannels()(actorSystem), 10 seconds)

  val channelInfo = allChannels.map{ channel=>
    val history = Await.result(client.getChannelHistory(channel.id)(actorSystem), 10 seconds)
    history.messages
  }

  channelInfo.foreach(a => println(a))
}
