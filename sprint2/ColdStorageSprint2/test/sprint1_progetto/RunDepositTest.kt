package sprint1_progetto

import it.unibo.kactor.QakContext
import kotlinx.coroutines.runBlocking

class RunDepositTest {
    fun main() = runBlocking {
        QakContext.createContexts("localhost",this,"test_deposit_sprint1.pl","sysRules.pl","ctxcss_test_deposit")
    }
}