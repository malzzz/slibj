name := "slibj"

organization := "co.quine"

version := "0.1.0"

scalaVersion := "2.11.8"

isSnapshot := true

publishTo := Some("Quine snapshots" at "s3://snapshots.repo.quine.co")

resolvers ++= Seq[Resolver](
  "Typesafe repository snapshots"    at "http://repo.typesafe.com/typesafe/snapshots/",
  "Typesafe repository releases"     at "http://repo.typesafe.com/typesafe/releases/"
)

lazy val versions = new {
  val fs = "0.9.0-M3"
  val scalajhttp = "2.3.0"
}

libraryDependencies ++= Seq(
  "org.scalaj" %% "scalaj-http" % versions.scalajhttp,
  "co.fs2"  %% "fs2-core" % versions.fs,
  "co.fs2"  %% "fs2-io"   % versions.fs
)