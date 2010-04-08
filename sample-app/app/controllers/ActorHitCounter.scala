package controllers

import play._
import play.mvc._

import se.scalablesolutions.akka.actor._

/**
 * This shows some rudimentary Actors stuff.
 * Again this is a hit counter, but here we're using an actor instead of STM.
**/
object ActorHitCounter extends Controller {
    
    //setting up an actor to keep track of hits - don't forget to start it!
    val hitCounter = new HitCounterActor
    hitCounter.start
    
    
    def index = {
        
        //increment the hit counter using fire-and-forget semantics
        hitCounter ! Increment
        
        //fetch the count using send-and-receive-eventually semantics
        var count = (hitCounter !! (GetCount, 1000)).getOrElse(0)
        
        //fetch the count again using send-and-receive-future semantics
        val f = hitCounter !!! GetCount
        f.await
        count = f.result match {
            case Some(i: Int) => i
            case _ => 0
        }
        
        //display the result
       <h1>{count}</h1>
    }
    
}

/**
 * This is the actor that acts as our hit counter.
 * It receives two messages defined just below.  Increment means increment the count, and GetCount means reply with the count
**/
case object Increment{}
case object GetCount{}
class HitCounterActor extends Actor{
    
    //make your vars private!
    private var counter = 0
    
    def receive = {
        case Increment => counter = counter + 1
        case GetCount => reply(counter)
    }
    
}