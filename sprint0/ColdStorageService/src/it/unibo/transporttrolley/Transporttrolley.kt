/* Generated by AN DISI Unibo */ 
package it.unibo.transporttrolley

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Transporttrolley ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outyellow("TRANSPORT TROLLEY START, engage basicrobot")
						request("engage", "engage(transporttrolley)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("idle") { //this:State
					action { //it:State
						request("moverobot", "moverobot(0,0)" ,"basicrobot" )  
						CommUtils.outblack("waiting for requests...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("job") { //this:State
					action { //it:State
						CommUtils.outyellow("transporttrolley si dirige all'indoor")
						request("moverobot", "moverobot(7,0)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_job", 
				 	 					  scope, context!!, "local_tout_transporttrolley_job", 8000.toLong() )
					}	 	 
					 transition(edgeName="t06",targetState="coldroom",cond=whenTimeout("local_tout_transporttrolley_job"))   
				}	 
				state("coldroom") { //this:State
					action { //it:State
						CommUtils.outyellow("transporttrolley si dirige alla cold room")
						request("moverobot", "moverobot(4,4)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_coldroom", 
				 	 					  scope, context!!, "local_tout_transporttrolley_coldroom", 8000.toLong() )
					}	 	 
					 transition(edgeName="t07",targetState="idle",cond=whenTimeout("local_tout_transporttrolley_coldroom"))   
				}	 
				state("halt1") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("halt2") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
}
