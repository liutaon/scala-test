package liutaon.json4s

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

object Json4sApp extends App {

  case class Winner(id: Long, numbers: List[Int])
  case class Lotto(id: Long, winningNumbers: List[Int], winners: List[Winner], drawDate: Option[java.util.Date])

  val winners = List(Winner(23, List(2, 45, 34, 23, 3, 5)), Winner(54, List(52, 3, 12, 11, 18, 22)))
  val lotto = Lotto(5, List(2, 45, 34, 23, 7, 5, 3), winners, None)

  val json =
    ("lotto" ->
      ("lotto-id" -> lotto.id) ~
      ("winning-numbers" -> lotto.winningNumbers) ~
      ("draw-date" -> lotto.drawDate.map(_.toString)) ~
      ("winners" ->
        lotto.winners.map { w =>
          (("winner-id" -> w.id) ~
           ("numbers" -> w.numbers))}))

  println(compact(render(json)))
  println(pretty(render(json)))

  val lotto1 = parse("""{
    "lotto":{
       "lotto-id":5,
       "winning-numbers":[2,45,34,23,7,5,3]
       "winners":[{
         "winner-id":23,
         "numbers":[2,45,34,23,3,5]
       }]
     }
   }""")
  val lotto2 = parse("""{
    "lotto":{
       "winners":[{
         "winner-id":54,
         "numbers":[52,3,12,11,18,22]
       }]
     }
   }""")
  val mergedLotto = lotto1 merge lotto2
  println(pretty(render(mergedLotto)))
}
