ThisBuild / sonatypeProfileName := "com.bitcoinpaygate"

ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"

ThisBuild / publishMavenStyle := true

ThisBuild / licenses += ("Apache-2.0", url("https://opensource.org/licenses/Apache-2.0"))

ThisBuild / homepage := Some(url("https://github.com/bitcoinpaygate/xchange-spray-json/"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/bitcoinpaygate/xchange-spray-json/"),
    "scm:git@github.com:bitcoinpaygate/xchange-spray-json.git",
  )
)

ThisBuild / developers := List(
  Developer(id = "pjendrusik", name = "Paweł Jendrusik", email = "", url = url("https://github.com/pjendrusik"))
)

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "content/repositories/releases")
}
ThisBuild / publishMavenStyle := true
