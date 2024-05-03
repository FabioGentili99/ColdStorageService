/* Generated by AN DISI Unibo */ 
package it.unibo.test_deposit_transporttrolley

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
class Test_deposit_transporttrolley ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				var TruckTicket : Int = 0	
				return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outgreen("TRANSPORT TROLLEY STARTING")
						request("engage", "engage(transporttrolley,200)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="waiting",cond=whenReply("engagedone"))
					transition(edgeName="t01",targetState="errorengage",cond=whenReply("engagerefused"))
				}	 
				state("errorengage") { //this:State
					action { //it:State
						CommUtils.outred("engage del basicrobot fallito")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
				state("waiting") { //this:State
					action { //it:State
						CommUtils.outgreen("waiting for requests...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t02",targetState="movetoindoor",cond=whenRequest("loaddone"))
				}	 
				state("movetoindoor") { //this:State
					action { //it:State
						updateResourceRep(	"test_deposit_sprint1(handle_loaddone)"	 
						)
						if( checkMsgContent( Term.createTerm("loaddone(TruckTicket)"), Term.createTerm("loaddone(T)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 TruckTicket = payloadArg(0).toInt()  
						}
						CommUtils.outgreen("il driver con ticket $TruckTicket è pronto all'indoor, avvio dell'operazione di scarico dal camion...")
						request("moverobot", "moverobot(0,4)" ,"basicrobot" )  
						updateResourceRep(	"test_deposit_sprint1(move_to_indoor)"	 
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t03",targetState="handlecharge",cond=whenReply("moverobotdone"))
					transition(edgeName="t04",targetState="errordeposit",cond=whenReply("moverobotfailed"))
				}	 
				state("errordeposit") { //this:State
					action { //it:State
						updateResourceRep(	"test_deposit_sprint1(deposit_failed)"	 
						)
						CommUtils.outred("operazione di deposito fallita, si procede con le prossime richieste...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="gohome", cond=doswitch() )
				}	 
				state("handlecharge") { //this:State
					action { //it:State
						CommUtils.outgreen("il robot è arrivato all'indoor, inizio scarico della merce dal camion...")
						delay(3000) 
						answer("loaddone", "goaway", "goaway(_)"   )  
						updateResourceRep(	"test_deposit_sprint1(pickup_completed)"	 
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="movetoport", cond=doswitch() )
				}	 
				state("movetoport") { //this:State
					action { //it:State
						CommUtils.outgreen("il camion è stato scaricato, il robot si dirige verso la porta della cold room...")
						request("moverobot", "moverobot(4,3)" ,"basicrobot" )  
						updateResourceRep(	"test_deposit_sprint1(move_to_port)"	 
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t05",targetState="handledeposit",cond=whenReply("moverobotdone"))
					transition(edgeName="t06",targetState="errordeposit",cond=whenReply("moverobotfailed"))
				}	 
				state("handledeposit") { //this:State
					action { //it:State
						CommUtils.outgreen("il robot è arrivato davanti alla cold room, inizia la fase di deposito...")
						delay(3000) 
						CommUtils.outgreen("deposito effettuato con successo")
						updateResourceRep(	"test_deposit_sprint1(deposit_done)"	 
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="gohome", cond=doswitch() )
				}	 
				state("gohome") { //this:State
					action { //it:State
						CommUtils.outgreen("ritorno del robot alla posizione home in corso...")
						request("moverobot", "moverobot(0,0)" ,"basicrobot" )  
						updateResourceRep(	"test_deposit_sprint1(returning_home)"	 
						)
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
