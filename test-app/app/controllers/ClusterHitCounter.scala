package controllers

import play._
import play.mvc._

import se.scalablesolutions.akka.actor._
import se.scalablesolutions.akka.stm._
import se.scalablesolutions.akka.stm.Transaction._
import se.scalablesolutions.akka.remote._


object ClusterHitCounter extends Controller {
    
    val ref = TransactionalRef[Int]
    
    def index = {
        val clusterHitCounterActor = RemoteClient.actorFor("cluster-hit-counter", "localhost", 9999)
        clusterHitCounterActor ! Increment
        <h1>{(clusterHitCounterActor !! GetCount).getOrElse(0)}</h1>
    }
    
}