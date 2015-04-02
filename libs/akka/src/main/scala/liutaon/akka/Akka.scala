package liutaon.akka

import akka.actor._

case class Greeting(who: String)

class GreetingActor extends Actor with ActorLogging {
  def receive = {
    case Greeting(who) => log.info("Hello " + who)
  }
}

class ByeActor extends Actor with ActorLogging with Stash {

  def receive = {
    case "Init" =>
      log.info("Send Identify")
      context.actorSelection("/user/greeter") ! Identify("shop1")
    case ActorIdentity("shop1", Some(ref)) =>
      log.info("Find " + ref)
      context.watch(ref)
      context.become(handle(ref))
      unstashAll()
    case ActorIdentity("shop1", None) ⇒
      log.info("Stop when none")
      context.stop(self)
    case a =>
      log.info("Stash xx {}", a)
      stash()
  }

  def handle(ref: ActorRef): Receive = {
    case Greeting(who) =>
      log.info("Buy " + who)
      context.stop(ref)
      // ref ! PoisonPill
    case Terminated(r) if r == ref =>
      log.info("Terminated {}", r)
      context.stop(self)
  }
}

object AkkaApp extends App {
  val system = ActorSystem("MySystem")
  val greeter = system.actorOf(Props[GreetingActor], name = "greeter")
  val bye = system.actorOf(Props[ByeActor], name = "bye")
  greeter ! Greeting("Charlie Parker")
  bye ! "Init"
  bye ! Greeting("Tom")
  Thread.sleep(1000)
  system.shutdown
}
