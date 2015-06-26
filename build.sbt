name             := "SimPack"

version          := "0.1.0" // was: "0.91"

organization     := "de.sciss"  // for publishing

description      := "SimPack is a library to measure the similarity between concepts in ontologies or ontologies as a whole."

homepage         := Some(url(s"https://github.com/Sciss/${name.value}"))

licenses         := Seq("LGPL v2.1+" -> url("http://www.gnu.org/licenses/lgpl-2.1.txt"))

scalaVersion     := "2.11.6"

crossPaths       := false  // this is just a Java project right now!

autoScalaLibrary := false

javacOptions in (Compile, compile) ++= Seq("-g", "-source", "1.6", "-target", "1.6")

libraryDependencies ++= Seq(
  "org.apache.logging.log4j" % "log4j-api" % "2.3",
  "junit" % "junit" % "4.12" % "test"
)

// ---- publishing ----

publishMavenStyle := true

publishTo :=
  Some(if (isSnapshot.value)
    "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  else
    "Sonatype Releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
  )

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := { val n = name.value
<scm>
  <url>git@github.com:Sciss/{n}.git</url>
  <connection>scm:git:git@github.com:Sciss/{n}.git</connection>
</scm>
<developers>
  <developer>
    <id>bernstein@ifi.uzh.ch</id>
    <name>Abraham Bernstein</name>
    <url>http://www.ifi.uzh.ch/ddis/people/bernstein.html</url>
  </developer>
  <developer>
    <id>kiefer@ifi.uzh.ch</id>
    <name>Christoph Kiefer</name>
    <url>https://files.ifi.uzh.ch/ddis/oldweb/ddis/people/alumni/kiefer/</url>
  </developer>
  <developer>
    <id>sciss</id>
    <name>Hanns Holger Rutz</name>
    <url>http://www.sciss.de</url>
  </developer>
</developers>
}
