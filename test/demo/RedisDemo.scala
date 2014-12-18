package demo

import redis.RedisClient
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {
  implicit val akkaSystem = akka.actor.ActorSystem()

  val redis = RedisClient()

  def testKey() {
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

    f.map { case (a, b) => println(a.map(_.utf8String) + "  " + b.map(_.utf8String)) }
  }

  def testKey2() {
    redis.setnx("access.count",1)
    redis.incr("access.count")
    redis.get("access.count").map { i => println("access.count=" + i.map(_.utf8String.toInt)) }
  }
  
  // 测试 Hash
  def testHash() {
    redis.hset("demo.hash", "name", "tom")
    redis.hset("demo.hash", "age", "28")
    redis.hgetall("demo.hash").map { case hash => println("demo.hash=" + hash)}
  }

  def testSet() {
    redis.srem("demo.set", "a", "b", "c")
    redis.sadd("demo.set", "abc", "china", "jack")
    redis.sismember("demo.set", "china").map { b => println("demo.set.ismember=" + b) }
    redis.smembers("demo.set").map { s => println("demo.set=" + s.map(_.utf8String)) }
  }
  
  def testSetInter() {
    redis.sadd("demo.set1", "1", "2", "3", "4")
    redis.sadd("demo.set2", "a", "b", "c", "d", "1", "2","3")
    redis.sinter("demo.set1", "demo.set2").map { d => println("demo.set.inter=" + d.map(_.utf8String)) }
    redis.sdiff("demo.set1", "demo.set2").map { d => println("demo.set.diff=" + d.map(_.utf8String)) }
    redis.sdiff("demo.set2", "demo.set1").map { d => println("demo.set.diff=" + d.map(_.utf8String)) }
  }
  
  def testList() {
    redis.lpush("demo.list", "abc", "123", "china", "jack")
    redis.lpop("demo.list").map { l => println("demo.list.pop=" + l.map(_.utf8String)) }
    redis.rpush("demo.list", "right1", "right2")
    redis.lpush("demo.list", "left1", "left2")
    redis.lrange("demo.list", 0, 3).map { l => println("demo.list.range=" + l.map(_.utf8String)) }
  }
  
  def testSortedSet() {
    redis.zadd("demo.sorted.set", (10, "abc"))
    redis.zadd("demo.sorted.set", (9, "abcDEF"))
    redis.zadd("demo.sorted.set", (12, "09123"))
    redis.zadd("demo.sorted.set", (1, "09123"))
    redis.zincrby("demo.sorted.set", 10, "09123")
    redis.zrange("demo.sorted.set", 0, 10).map { s => println("demo.sorted.set=" + s.map(_.utf8String)) }
  }
  
  // 测试 Key
  testKey()
  testKey2()
  testHash()
  testSet()
  testSetInter()
  testList()
  testSortedSet()
  Thread.sleep(5000)
  akkaSystem.shutdown()
}
