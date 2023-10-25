/* Generated by AN DISI Unibo */ 
package it.unibo.serviceaccessgui_simulator

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
class Serviceaccessgui_simulator ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				var TruckTicket : Int= 0
				//val TruckLoad : Int = 50
				return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outblue("GUI START, sending request to cold storage service")
						delay(3000) 
						request("storerequest", "storerequest(50)" ,"coldstorageservice" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="enterticket",cond=whenReply("loadaccepted"))
					transition(edgeName="t01",targetState="handlereject",cond=whenReply("loadrejected"))
				}	 
				state("handlereject") { //this:State
					action { //it:State
						CommUtils.outblue("frigo pieno, impossibile depositare altro cibo")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
				state("enterticket") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("loadaccepted(Ticket)"), Term.createTerm("loadaccepted(Ticket)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 TruckTicket = payloadArg(0).toInt()  
						}
						delay(2000) 
						CommUtils.outblue("inserimento codice biglietto per verifica")
						request("verifyticket", "verifyticket($TruckTicket)" ,"coldstorageservice" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t02",targetState="startdeposit",cond=whenReply("chargetaken"))
					transition(edgeName="t03",targetState="error",cond=whenReply("invalidticket"))
				}	 
				state("error") { //this:State
					action { //it:State
						CommUtils.outblue("il ticket inserito non è valido o è scaduto")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
				state("startdeposit") { //this:State
					action { //it:State
						CommUtils.outblue("il ticket inseriro è valido, inizio azione di deposito...")
						delay(2000) 
						request("loaddone", "loaddone($TruckTicket)" ,"coldstorageservice" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t04",targetState="depositcompleted",cond=whenReply("goaway"))
					transition(edgeName="t05",targetState="failure",cond=whenReply("depositfailed"))
				}	 
				state("failure") { //this:State
					action { //it:State
						CommUtils.outblue("il deposito è fallito, liberare l'INDOOR e procedere con una nuova richiesta")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
				state("depositcompleted") { //this:State
					action { //it:State
						CommUtils.outblue("il transporttrolley ha scaricato la merce dal camion, liberare l'INDOOR")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
			}
		}
} 
