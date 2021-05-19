name := "xchange-spray-json"

ThisBuild / organization := "com.bitcoinpaygate"

ThisBuild / version := "0.2.3"

ThisBuild / scalaVersion := "2.13.1"

scapegoatVersion in ThisBuild := "1.4.1"

sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
sonatypeCredentialHost := "s01.oss.sonatype.org"

libraryDependencies ++= {
  val xchangeVersion = "4.3.21"
  val akkaHttpVersion = "10.1.11"
  val akkaVersion = "2.6.1"

  Seq(
    "org.knowm.xchange" % "xchange-core"          % xchangeVersion  % "provided",
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion % "provided",
    "com.typesafe.akka" %% "akka-stream"          % akkaVersion     % "provided",
    "javax.ws.rs"       % "javax.ws.rs-api"       % "2.1"           % "provided" artifacts Artifact("javax.ws.rs-api", "jar", "jar") // this is a workaround for jax-rs/api#571 (XChange 4.3.X dependency)
  )
}

fork := true

addCommandAlias("testAll", ";test")
addCommandAlias("formatAll", ";scalafmt;test:scalafmt;scalafmtSbt")
addCommandAlias("compileAll", ";compile;test:compile")

val doNotPublishSettings = Seq(publish := {})
