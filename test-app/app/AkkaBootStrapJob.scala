import play._
import play.jobs._
import play.test._

import controllers.{Increment,GetCount}
import se.scalablesolutions.akka.actor._
 
@OnApplicationStart
class Bootstrap extends Job {
 
    override def doJob(): Unit = {
        import se.scalablesolutions.akka.remote.RemoteNode
        

        RemoteNode.start(Some(this.getClass.getClassLoader))
        RemoteNode.register("cluster-hit-counter", new ClusterHitCounterActor)
    }
 
}

class ClusterHitCounterActor extends Actor{
    private var count = 0
    start
    
    def receive = {
        case Increment => count = count + 1
        case GetCount => reply(count)
    }
    
}

