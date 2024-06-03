import it.unibo.kactor.QakContext
import kotlinx.coroutines.runBlocking

class RunDepositTest {
    fun main() = runBlocking {
        QakContext.createContexts("localhost",this,"coldstorageservice.pl","sysRules.pl","ctxcoldstorageservice")
    }
}