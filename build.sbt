name := "xchange-spray-json"

organization := "bitcoinpaygate"

version := "0.2.0"

scalaVersion := "2.13.1"

scapegoatVersion in ThisBuild := "1.4.1"

libraryDependencies ++= {
  val xchangeVersion = "4.3.21"
  val akkaHttpVersion = "10.1.11"

  Seq(
    "org.knowm.xchange" % "xchange-core"          % xchangeVersion  % "provided",
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion % "provided",
    "javax.ws.rs"       % "javax.ws.rs-api"       % "2.1"           % "provided" artifacts Artifact("javax.ws.rs-api", "jar", "jar") // this is a workaround for jax-rs/api#571 (XChange 4.3.X dependency)
  )
}

fork := true

addCommandAlias("testAll", ";test")
addCommandAlias("formatAll", ";scalafmt;test:scalafmt;scalafmtSbt")
addCommandAlias("compileAll", ";compile;test:compile")

bintrayOrganization := Some("bitcoinpaygate")

bintrayRepository := "bitcoinpaygate-maven"

licenses += ("Apache-2.0", url("https://opensource.org/licenses/Apache-2.0"))

val doNotPublishSettings = Seq(publish := {})

val publishSettings =
  if (version.toString.endsWith("-SNAPSHOT"))
    Seq(
      publishTo := Some("Artifactory Realm" at "http://oss.jfrog.org/artifactory/oss-snapshot-local"),
      bintrayReleaseOnPublish := false,
      credentials := List(Path.userHome / ".bintray" / ".artifactory").filter(_.exists).map(Credentials(_))
    )
  else
    Seq(
      pomExtra := <scm>
        <url>https://github.com/bitcoinpaygate/xchange-spray-json</url>
        <connection>https://github.com/bitcoinpaygate/xchange-spray-json</connection>
      </scm>,
      publishArtifact in Test := false,
      homepage := Some(url("https://github.com/bitcoinpaygate/xchange-spray-json")),
      publishMavenStyle := false
    )
