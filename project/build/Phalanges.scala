import sbt._

class Phalanges(info: ProjectInfo) extends DefaultProject(info)
{
    val nettyRepo = "repository.jboss.org" at "http://repository.jboss.org/nexus/content/groups/public/"
    val netty = "org.jboss.netty" % "netty" % "3.2.4.Final"
    val twitterRepo = "maven.twttr.com" at "http://maven.twttr.com/"
    val configgy = "net.lag" % "configgy" % "2.0.2"
    val ostrich = "com.twitter" % "ostrich" % "3.0.4"
    override def mainClass = Some("com.postneo.phalanges.FingerServer")
}
