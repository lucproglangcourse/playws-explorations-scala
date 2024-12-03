scalaVersion := "3.3.3"

Compile / scalacOptions ++= Seq(
  "-deprecation"
, "-feature"
, "-unchecked"
, "-Wvalue-discard"
, "-Yexplicit-nulls"
, "-Ysafe-init"
, "-language:strictEquality"
// "-Xfatal-warnings"
)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.2.9",
  "org.slf4j"         % "slf4j-simple"            % "2.0.16"
)
