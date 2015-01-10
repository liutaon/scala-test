import play.Play.autoImport._
import PlayKeys._
import play.twirl.sbt.Import.TwirlKeys

Common.init("scala-test")

libraryDependencies ++= Seq(
  Deps.jdbc, Deps.slick, Deps.mysql, Deps.jodamoney, Deps.jodatime, Deps.redis, Deps.poi
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
