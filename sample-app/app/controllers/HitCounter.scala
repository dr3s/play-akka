package controllers

import play._
import play.mvc._

import se.scalablesolutions.akka.actor._
import se.scalablesolutions.akka.stm._
import se.scalablesolutions.akka.stm.Transaction._
import se.scalablesolutions.akka.stm.Transaction.Local._



object HitCounter extends Controller {
    
    val ref = TransactionalRef[Int]
    var dumbCount = 0
    
    def index = {
        dumbCount = dumbCount + 1
        val count = atomic{
            val i = ref.get.getOrElse(0) + 1
            ref.swap(i)
            i
        }
        <div>
            <h1>{count}</h1>
            <h1>{dumbCount}</h1>
        </div>
    }
    
}