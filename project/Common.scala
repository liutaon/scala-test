import sbt._
import Keys._
import com.typesafe.sbteclipse.core.EclipsePlugin.EclipseKeys

object Common {
  def init(project: String) = Seq(
    name := project,
    organization := "liutaon",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.11.4",
    // logLevel := Level.Debug,
    incOptions := incOptions.value.withNameHashing(true),
    libraryDependencies ++= Seq(Deps.scalacheck),
    javacOptions ++= Seq("-encoding", "UTF-8"),
    scalacOptions ++= Seq("-feature", "-language:reflectiveCalls", "-deprecation", "-encoding", "UTF-8"),
    EclipseKeys.skipParents in ThisBuild := false,
    sources in (Compile, doc) := Seq.empty,
    publishArtifact in (Compile, packageDoc) := false
  )

  def recursiveListFiles(f: File): Seq[File] = {
    val files = Option(f.listFiles).toSeq.flatten
    files ++ files.filter(_.isDirectory).flatMap(recursiveListFiles)
  }
}

object Deps {
  val PlayVersion = "2.3.7"
  val play = "com.typesafe.play" %% "play" % PlayVersion
  val jdbc = "com.typesafe.play" %% "play-jdbc" % PlayVersion
  val cache = "com.typesafe.play" %% "play-cache" % PlayVersion
  val json = "com.typesafe.play" %% "play-json" % PlayVersion
  val ws = "com.typesafe.play" %% "play-ws" % PlayVersion
  val filters = "com.typesafe.play" %% "filters-helpers" % PlayVersion
  val slick = "com.typesafe.slick" %% "slick" % "2.1.0"
  val jodaconvert = "org.joda" % "joda-convert" % "1.7"
  val jodatime = "joda-time" % "joda-time" % "2.4"
  val jodamoney = "org.joda" % "joda-money" % "0.9.1"
  val mysql = "mysql" % "mysql-connector-java" % "5.1.32"
  val mailer = "com.typesafe.play.plugins" %% "play-plugins-mailer" % "2.3.0"
  val commonsio = "commons-io" % "commons-io" % "2.4"
  val redis =   "com.etaty.rediscala" %% "rediscala" % "1.4.2"
  // test
  val scalacheck = "org.scalacheck" %% "scalacheck" % "1.11.5" % "test"
}