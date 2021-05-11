ThisBuild / sonatypeProfileName := "com.bitcoinpaygate"

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