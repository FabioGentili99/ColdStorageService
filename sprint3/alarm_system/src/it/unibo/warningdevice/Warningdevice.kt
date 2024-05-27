/* Generated by AN DISI Unibo */ 
package it.unibo.warningdevice

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
class Warningdevice ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
				return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="athome", cond=doswitch() )
				}	 
				state("athome") { //this:State
					action { //it:State
						    
									try{
						      			val p  = Runtime.getRuntime().exec("python3 ledOFF.py")
						      			
						    			}catch( e : Exception){
						      				println(e.message)
						    			}
						CommUtils.outyellow("warningdevice - led OFF")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t11",targetState="athome",cond=whenDispatch("home"))
					transition(edgeName="t12",targetState="moving",cond=whenDispatch("moving"))
					transition(edgeName="t13",targetState="stopped",cond=whenDispatch("stopped"))
				}	 
				state("moving") { //this:State
					action { //it:State
						CommUtils.outyellow("warningdevice - start blinking")
						    
									try{
						      			val p  = Runtime.getRuntime().exec("python3 ledON.py")
						      			
						    			}catch( e : Exception){
						      				println(e.message)
						    			}
						 Thread.sleep(500);
									try{
						      			val p  = Runtime.getRuntime().exec("python3 ledOFF.py")
						      			
						    			}catch( e : Exception){
						      				println(e.message)
						    			}
									
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_moving", 
				 	 					  scope, context!!, "local_tout_warningdevice_moving", 500.toLong() )
					}	 	 
					 transition(edgeName="t24",targetState="moving",cond=whenTimeout("local_tout_warningdevice_moving"))   
					transition(edgeName="t25",targetState="moving",cond=whenDispatch("moving"))
					transition(edgeName="t26",targetState="athome",cond=whenDispatch("home"))
					transition(edgeName="t27",targetState="stopped",cond=whenDispatch("stopped"))
				}	 
				state("stopped") { //this:State
					action { //it:State
						    
									try{
						      			val p  = Runtime.getRuntime().exec("python3 ledON.py")
						      			
						    			}catch( e : Exception){
						      				println(e.message)
						    			}
						CommUtils.outyellow("warningdevice - led ON")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t38",targetState="stopped",cond=whenDispatch("stopped"))
					transition(edgeName="t39",targetState="athome",cond=whenDispatch("home"))
					transition(edgeName="t310",targetState="moving",cond=whenDispatch("moving"))
				}	 
			}
		}
} 