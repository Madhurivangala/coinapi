lazy val appName = "CryptoTest"
lazy val appVersion = "1.0"
lazy val scalaVersionNumber = "2.11"
lazy val scalaVersionDot = s"$scalaVersionNumber.8"
name := appName
version := appVersion
scalaVersion := scalaVersionDot

assemblyJarName := s"${appName}_$scalaVersionNumber-$appVersion.jar"

packageOptions in assembly ~= { pos =>
  pos.filterNot { po =>
    po.isInstanceOf[Package.MainClass]
  }
}

// Repos to look at for finding dependencies.
resolvers += "Typesafe backup repo" at "http://repo.typesafe.com/typesafe/repo/"
resolvers += "Maven repo1" at "http://repo1.maven.org/maven2/"
resolvers += "jitpack" at "https://jitpack.io"

// Change the spark packages to "provided" before deploying to edge node.
libraryDependencies += ("org.apache.spark" %% "spark-core" % "2.3.0.2.6.5.0-292" % "compile")
libraryDependencies += ("org.apache.spark" %% "spark-sql" % "2.3.0.2.6.5.0-292" % "compile")
libraryDependencies += ("org.apache.spark" %% "spark-hive" % "2.3.0.2.6.5.0-292" % "compile")
libraryDependencies += ("org.wvlet.airframe" %% "airframe-log" % "19.4.2" % "compile")
libraryDependencies += ("org.scalactic" %% "scalactic" % "3.0.5" % "provided")
libraryDependencies += ("org.scalatest" %% "scalatest" % "3.0.4" % "compile" )
libraryDependencies += ("org.scalaj" %% "scalaj-http" % "2.3.0")
libraryDependencies += "io.really" %% "jwt-scala" % "1.2.2"
libraryDependencies += "com.typesafe.play" % "play-json_2.11" % "2.4.6" % "compile"
libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.12.3"  % "compile"


libraryDependencies += "net.sourceforge.jtds" % "jtds" % "1.3.1" % "compile"
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.13"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.4.0-M2" % "compile"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}