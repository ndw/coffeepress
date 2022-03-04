lazy val extIxmlVersion = "2.50.11"

name         := "invisible-xml"
organization := "com.xmlcalabash"
homepage     := Some(url("https://xmlcalabash.com/"))
version      := extIxmlVersion
scalaVersion := "2.13.5"
//maintainer   := "ndw@nwalsh.com" // for packaging

resolvers += "Restlet" at "https://maven.restlet.com"

libraryDependencies += "org.nineml" % "coffeegrinder" % "0.6.0"
libraryDependencies += "org.nineml" % "coffeefilter" % "0.7.0"
libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.32"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.7"
libraryDependencies += "com.xmlcalabash" % "xml-calabash_2.13" % "2.99.11"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test"
