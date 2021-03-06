Common.init("scala-test")

libraryDependencies ++= Seq(
  Deps.reflect, Deps.jdbc, Deps.slick, Deps.mysql, Deps.jodamoney, Deps.jodatime, Deps.redis, Deps.poi,
  Deps.qrgen, Deps.barcode
)

lazy val akka = project.in(file("libs/akka"))
lazy val colossus = project.in(file("libs/colossus"))
lazy val finagle = project.in(file("libs/finagle"))
lazy val json4s = project.in(file("libs/json4s"))

lazy val root = (project in file(".")).enablePlugins(PlayScala)
  .dependsOn(colossus, finagle, json4s)
  .aggregate(colossus, finagle, json4s)
