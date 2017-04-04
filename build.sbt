// build.sbt

scalariformSettings

organization := "co.s4n"

name := "users-api-consumer"

scalaVersion := "2.12.1"

resolvers ++= Seq(
  "releases" at "http://oss.sonatype.org/content/repositories/releases"
)

val circeVersion = "0.7.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging"  %%  "scala-logging"                 % "3.5.0",
  "com.iheart"                  %%  "ficus"                         % "1.4.0",
  "ch.qos.logback"              %   "logback-classic"               % "1.1.7",
  "org.scalatest"               %   "scalatest_2.12"                % "3.0.0"     % "test",
  "de.heikoseeberger"           %% "akka-http-circe"                % "1.13.0",
  "com.typesafe.akka"           %%  "akka-http"                     % "10.0.4",
  "com.typesafe.akka"           %% "akka-http-testkit"              % "10.0.4",
  "com.outworkers"              %% "phantom-dsl"                    % "2.3.1",
  "org.cassandraunit"           % "cassandra-unit"                  % "3.1.1.0" % "it,test",
  "org.apache.kafka"            % "kafka-clients"                   % "0.10.2.0",
  "com.typesafe.akka"           %%  "akka-stream-kafka"             % "0.14"
)

coverageEnabled := false

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Xcheckinit"
)

publishMavenStyle := true

pomIncludeRepository := { _ => false }

publishArtifact in Test := false

publishTo := {
  val nexus = "http://somewhere/nexus/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("Nexus Snapshots" at nexus + "content/repositories/snapshots/")    
  else
    Some("Nexus Releases" at nexus + "content/repositories/releases")
}

credentials += Credentials("Sonatype Nexus Repository Manager", "somewhere", "user", "password")
