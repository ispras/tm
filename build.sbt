name := "tm"

version := "1.0"

scalaVersion := "2.11.1"

resolvers += "JCenter" at "http://jcenter.bintray.com/"

libraryDependencies += "org.clapper" %% "grizzled-slf4j" % "1.0.2"

libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.7"

libraryDependencies += "log4j" % "log4j" % "1.2.14"

resolvers +="Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies += "net.sf.trove4j" % "trove4j" % "3.0.3"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test"

libraryDependencies += "org.mockito" % "mockito-core" % "1.8.5"

libraryDependencies += "com.esotericsoftware.kryo" % "kryo" % "2.10"
