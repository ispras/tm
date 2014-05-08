import sbtassembly.Plugin._
import AssemblyKeys._

assemblySettings

name := "tm"

version := "1.0"

scalaVersion := "2.10.3"

javaHome := Some(file(System.getenv("JAVA_HOME")))

unmanagedSourceDirectories in Compile := List(file("src/"))

unmanagedJars in Compile ++= List(file("lib/"))

target in Compile := file("target/")
