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
import it.unibo.kactor.sysUtil.createActor   //Sept2023
class Transporttrolley ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
			var TruckTicket : Int = 0	 
				return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outyellow("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t08",targetState="waitingHome",cond=whenDispatch("robotready"))
				}	 
				state("waitingHome") { //this:State
					action { //it:State
						forward("setrobotstate", "setpos(0,0,down)" ,"robotpos" ) 
						CommUtils.outyellow("transport trolley waiting for requests, robot at home")
						forward("home", "home(_)" ,"warningdevice" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t09",targetState="stoppedHome",cond=whenDispatch("stop"))
					transition(edgeName="t010",targetState="waitingHome",cond=whenDispatch("resume"))
					transition(edgeName="t011",targetState="startdeposit",cond=whenRequest("loaddone"))
				}	 
				state("stoppedHome") { //this:State
					action { //it:State
						CommUtils.outyellow("transport trolley stopped, robot at home")
						forward("home", "home(_)" ,"warningdevice" ) 
						forward("stopplan", "stopplan(_)" ,"planexec" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t012",targetState="stoppedHome",cond=whenDispatch("stop"))
					transition(edgeName="t013",targetState="continueworking",cond=whenDispatch("resume"))
				}	 
				state("continueworking") { //this:State
					action { //it:State
						CommUtils.outyellow("transport trolley resumed, robot at home")
						forward("home", "home(_)" ,"warningdevice" ) 
						forward("continueplan", "continueplan(_)" ,"planexec" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waitingHome", cond=doswitch() )
				}	 
				state("startdeposit") { //this:State
					action { //it:State
						updateResourceRep( "test_deposit(handle_loaddone)" 
						)
						if( checkMsgContent( Term.createTerm("loaddone(TruckTicket)"), Term.createTerm("loaddone(Ticket)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 TruckTicket = payloadArg(0).toInt()
						}
						CommUtils.outyellow("loaddone received, start moving to indoor")
						forward("moving", "moving(_)" ,"warningdevice" ) 
						request("moverobot", "moverobot(0,4)" ,"robotpos" )  
						updateResourceRep("test_deposit(move_to_indoor)" 
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t014",targetState="stoppedWhileMovingIndoor",cond=whenDispatch("stop"))
					transition(edgeName="t015",targetState="handleError",cond=whenReply("robotDead"))
					transition(edgeName="t016",targetState="handlecharge",cond=whenReply("moverobotdone"))
				}	 
				state("handlecharge") { //this:State
					action { //it:State
						forward("stopped", "stopped(_)" ,"warningdevice" ) 
						CommUtils.outyellow("il robot è arrivato all'indoor, inizio scarico della merce dal camion...")
						delay(3000) 
						updateResourceRep("test_deposit(pickup_completed)" 
						)
						answer("loaddone", "goaway", "goaway(_)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="movetoport", cond=doswitch() )
				}	 
				state("stoppedWhileMovingIndoor") { //this:State
					action { //it:State
						forward("stopplan", "stopplan(_)" ,"planexec" ) 
						CommUtils.outyellow("transport trolley stopped while moving to the indoor")
						forward("stopped", "stopped(_)" ,"warningdevice" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t017",targetState="resumeIndoor",cond=whenDispatch("resume"))
				}	 
				state("resumeIndoor") { //this:State
					action { //it:State
						forward("continueplan", "continueplan(_)" ,"planexec" ) 
						CommUtils.outyellow("transport trolley resumed")
						forward("moving", "moving(_)" ,"warningdevice" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t018",targetState="stoppedWhileMovingIndoor",cond=whenDispatch("stop"))
					transition(edgeName="t019",targetState="handleError",cond=whenReply("robotDead"))
					transition(edgeName="t020",targetState="handlecharge",cond=whenReply("moverobotdone"))
				}	 
				state("movetoport") { //this:State
					action { //it:State
						request("moverobot", "moverobot(4,3)" ,"robotpos" )  
						updateResourceRep("test_deposit(move_to_port)" 
						)
						CommUtils.outyellow("transport trolley moving to the cold room")
						forward("moving", "moving(_)" ,"warningdevice" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t021",targetState="stoppedWhileMovingPort",cond=whenDispatch("stop"))
					transition(edgeName="t022",targetState="handleError",cond=whenReply("robotDead"))
					transition(edgeName="t023",targetState="handledeposit",cond=whenReply("moverobotdone"))
				}	 
				state("stoppedWhileMovingPort") { //this:State
					action { //it:State
						forward("stopplan", "stopplan(_)" ,"planexec" ) 
						updateResourceRep("test_deposit(stopped)" 
						)
						CommUtils.outyellow("transport trolley stopped while moving to the cold room")
						forward("stopped", "stopped(_)" ,"warningdevice" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t024",targetState="resumePort",cond=whenDispatch("resume"))
				}	 
				state("resumePort") { //this:State
					action { //it:State
						forward("continueplan", "continueplan(_)" ,"planexec" ) 
						updateResourceRep("test_deposit(resumed)" 
						)
						CommUtils.outyellow("transport trolley resumed")
						forward("moving", "moving(_)" ,"warningdevice" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t025",targetState="stoppedWhileMovingPort",cond=whenDispatch("stop"))
					transition(edgeName="t026",targetState="handleError",cond=whenReply("robotDead"))
					transition(edgeName="t027",targetState="handledeposit",cond=whenReply("moverobotdone"))
				}	 
				state("handledeposit") { //this:State
					action { //it:State
						forward("stopped", "stopped(_)" ,"warningdevice" ) 
						CommUtils.outyellow("il robot è arrivato davanti alla cold room, inizio scarico della merce...")
						delay(3000) 
						updateResourceRep("test_deposit(deposit_done)" 
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="depositdone", cond=doswitch() )
				}	 
				state("depositdone") { //this:State
					action { //it:State
						CommUtils.outyellow("deposit done")
						forward("depositdone", "depositdone($TruckTicket)" ,"coldroommanager" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_depositdone", 
				 	 					  scope, context!!, "local_tout_transporttrolley_depositdone", 2000.toLong() )
					}	 	 
					 transition(edgeName="t028",targetState="returnhome",cond=whenTimeout("local_tout_transporttrolley_depositdone"))   
					transition(edgeName="t029",targetState="startdeposit",cond=whenRequest("loaddone"))
				}	 
				state("returnhome") { //this:State
					action { //it:State
						CommUtils.outyellow("no new requests, returning home...")
						request("moverobot", "moverobot(0,0)" ,"robotpos" )  
						updateResourceRep("test_deposit(returning_home)" 
						)
						forward("moving", "moving(_)" ,"warningdevice" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t030",targetState="stoppedWhileMovingHome",cond=whenDispatch("stop"))
					transition(edgeName="t031",targetState="handleError",cond=whenReply("robotDead"))
					transition(edgeName="t032",targetState="setDirectionHome",cond=whenReply("moverobotdone"))
				}	 
				state("stoppedWhileMovingHome") { //this:State
					action { //it:State
						forward("stopplan", "stopplan(_)" ,"planexec" ) 
						CommUtils.outyellow("transport trolley stopped while returning home")
						forward("stopped", "stopped(_)" ,"warningdevice" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t033",targetState="resumeHome",cond=whenDispatch("resume"))
				}	 
				state("resumeHome") { //this:State
					action { //it:State
						forward("continueplan", "continueplan(_)" ,"planexec" ) 
						CommUtils.outyellow("transport trolley resumed")
						forward("moving", "moving(_)" ,"warningdevice" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t034",targetState="stoppedWhileMovingHome",cond=whenDispatch("stop"))
					transition(edgeName="t035",targetState="handleError",cond=whenReply("robotDead"))
					transition(edgeName="t036",targetState="setDirectionHome",cond=whenReply("moverobotdone"))
				}	 
				state("setDirectionHome") { //this:State
					action { //it:State
						forward("setdirection", "dir(down)" ,"robotpos" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waitingHome", cond=doswitch() )
				}	 
				state("handleError") { //this:State
					action { //it:State
						CommUtils.outred("problems with the robot, stopping the service...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
} 
