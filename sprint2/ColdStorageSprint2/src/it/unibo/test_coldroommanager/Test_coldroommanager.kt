/* Generated by AN DISI Unibo */ 
package it.unibo.test_coldroommanager

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import it.unibo.kactor.sysUtil.createActor   //Sept2023
class Test_coldroommanager ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				val MAXW : Int = 100
				var CurrentWeight : Int = 0
				var FreeSpace : Int = 100
				var TruckLoad : Int = 0
				var TruckTicket : Int = 0
				var Requests = hashMapOf<Int,Int>()
				var Response : Boolean = false
				return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outgreen("COLD ROOM STARTING,capienza: $MAXW, free space: $FreeSpace")
						delay(1000) 
							
									Requests.put(1235, 10)
									Requests.put(1236, 10)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
				state("waiting") { //this:State
					action { //it:State
						CommUtils.outgreen("waiting for requests...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="handlestorerequest",cond=whenRequest("storerequest"))
				}	 
				state("handlestorerequest") { //this:State
					action { //it:State
						CommUtils.outgreen("richiesta di deposito in fase di valutazione...")
						if( checkMsgContent( Term.createTerm("storerequest(TruckLoad)"), Term.createTerm("storerequest(KG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 TruckLoad = payloadArg(0).toInt()  
						}
						if(  TruckLoad<= FreeSpace  
						 ){ 
											FreeSpace -= TruckLoad 
						CommUtils.outgreen("richiesta di deposito accettata, carico: $TruckLoad kg, spazio libero nel frigo: $FreeSpace kg. In attesa di generare un ticket univoco...")
						 Response = true  
						request("generateticket", "generateticket($TruckLoad)" ,"test_ticketmanager" )  
						}
						else
						 {CommUtils.outgreen("richiesta di deposito rifiutata, non c'e spazio sufficiente")
						  Response = false  
						 answer("storerequest", "loadrejected", "loadrejected(_)"   )  
						 }
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting", cond=doswitchGuarded({ !Response  
					}) )
					transition( edgeName="goto",targetState="waitingticket", cond=doswitchGuarded({! ( !Response  
					) }) )
				}	 
				state("waitingticket") { //this:State
					action { //it:State
						CommUtils.outgreen("in attesa della generazione del ticket...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t01",targetState="requestaccepted",cond=whenReply("ticket"))
				}	 
				state("requestaccepted") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("ticket(Ticket)"), Term.createTerm("ticket(Ticket)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												TruckTicket = payloadArg(0).toInt()
												Requests.put(TruckTicket, TruckLoad)
								CommUtils.outgreen("generato il ticket $TruckTicket per la richiesta di deposito di $TruckLoad kg")
								answer("storerequest", "loadaccepted", "loadaccepted($TruckTicket)"   )  
						}
						CommUtils.outgreen("è stato generato il ticket $TruckTicket per il driver")
						answer("storerequest", "loadaccepted", "loadaccepted($TruckTicket)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
			}
		}
} 
