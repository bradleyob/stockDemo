name := "stockDemo"
 
version := "1.0" 
      
lazy val `stockdemo` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice)

libraryDependencies ++= Seq(
  "com.yahoofinance-api" % "YahooFinanceAPI" % "3.15.0",
  "org.webjars" % "jquery" % "3.3.1",
  "org.webjars.bower" % "skeleton-css" % "2.0.4"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

