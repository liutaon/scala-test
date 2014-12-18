package demo

import redis.RedisClient
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {
  implicit val akkaSystem = akka.actor.ActorSystem()

  val redis = RedisClient()

  val futurePong = redis.ping()
  println("Ping sent!")
  futurePong.map(pong => {
    println(s"Redis replied with a $pong")
  })
  // key
  redis.set("person.count", "12345")
  redis.set("person.total", "123.33")
  val f = for {
    count <- redis.get("person.count")
    total <- redis.get("person.total")
  } yield {
    println("count=" + count.map(_.utf8String))
    println("total=" + total.map(_.utf8String))
    (count, total)
  }
  
  f.map { case (Some(a), Some(b)) => println(a.utf8String + "  " + b.utf8String) }
  Await.result(futurePong, 5.seconds)

  akkaSystem.shutdown()
}
