package controllers

import play._
import play.mvc._

import se.scalablesolutions.akka.actor._
import se.scalablesolutions.akka.stm._
import se.scalablesolutions.akka.stm.Transaction._



object HitCounter extends Controller {
    
    val ref = TransactionalRef[Int]
    
    def index = {
        MyActor ! Increment
        val reply: Option[Any] = MyActor !! GetCount
        val count: Int = reply match {
            case Some(i: Int) => i
            case _ => 0
        }
        <h1>{count}</h1>
    }
    
}

case object Increment{}
case object GetCount{}

object MyActor extends Actor{
    start
    
    val ref = TransactionalRef[Int]
    
    def receive = {
        case Increment =>
            atomic{
                val i: Int = ref.get.getOrElse(0) + 1
                ref.swap(i)
            }
        case GetCount => reply(atomic{ ref.get.getOrElse(0) })
    }
}
