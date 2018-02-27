import com.typesafe.sbt.SbtScalariform.ScalariformKeys

import scala.language.postfixOps
import scalariform.formatter.preferences._

name := "xchange-spray-json"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= {
  val xchangeVersion = "4.3.3"
  val akkaHttpVersion = "10.0.11"

  Seq(
    "org.knowm.xchange" % "xchange-core" % xchangeVersion % "provided",
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion % "provided",
    "javax.ws.rs" % "javax.ws.rs-api" % "2.1" % "provided" artifacts Artifact("javax.ws.rs-api", "jar", "jar") // this is a workaround for jax-rs/api#571 (XChange 4.3.X dependency)

  )
}

fork := true

scalacOptions --= Seq("-Xfatal-warnings")

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentConstructorArguments, true)
  .setPreference(SpacesAroundMultiImports, false)
  .setPreference(CompactControlReadability, false)

addCommandAlias("formatAll", ";scalariformFormat;test:scalariformFormat")
