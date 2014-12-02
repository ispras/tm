import SonatypeKeys._

sonatypeSettings

name := "tm"

version := "1.0"

scalaVersion := "2.11.4"

resolvers += "JCenter" at "http://jcenter.bintray.com/"

libraryDependencies += "org.clapper" %% "grizzled-slf4j" % "1.0.2"

libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.7"

libraryDependencies += "log4j" % "log4j" % "1.2.14"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies += "net.sf.trove4j" % "trove4j" % "3.0.3"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.0" % "test"

libraryDependencies += "org.mockito" % "mockito-core" % "1.8.5"

libraryDependencies += "com.esotericsoftware.kryo" % "kryo" % "2.10"


organization := "ru.ispras.modis"

profileName := "ru.ispras"

mainClass in assembly := Some("ru.ispras.modis.tm.wtf.ModelTrainer")

pomExtra := {
    <url>https://github.com/ispras/tm</url>
        <licenses>
            <license>
                <name>Apache 2</name>
                <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
            </license>
        </licenses>
        <scm>
            <connection>scm:git:github.com/ispras/tm.git</connection>
            <developerConnection>scm:git:git@github.com:ispras/tm.git</developerConnection>
            <url>github.com/ispras/tm.git</url>
        </scm>
        <developers>
            <developer>
                <id>acopich</id>
                <name>Valeriy Avanesov</name>
                <url>http://ispras.ru/en/</url>
                <email>acopich@gmail.com</email>
            </developer>
            <developer>
                <id>IlyaKozlov</id>
                <name>Ilya Kozlov</name>
                <url>http://ispras.ru/en/</url>
                <email>kozlov-ilya@ispras.ru</email>
            </developer>
        </developers>
}