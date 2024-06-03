# Cold Storage Service: Sprint1 
## Introduzione
Sprint1 del progetto finale del corso di Ingegneria dei Sistemi Software M.
In questa fase ci concentreremo sui requisiti riguardanti il core della logica di business del servizio da realizzare analizzati nello [sprint0](../sprint0/Sprint0_v1.html) con l'obiettivo di implementare un primo prototipo funzionante dell'applicazione. 
## Requisiti[](../commons/html/TemaFinale23.html)
[TemaFinale23](../commons/html/TemaFinale23.html)
In particolare, ci concentreremo sull'analisi e progettazione delle entitÃ  coinvolte nella catena "richiesta di deposito - accettazione - azione di deposito". 
## Analisi dei requisiti
### System abstract
Lo Sprint1 coinvolge i seguenti contesti: 

- ColdStorageService
- DDR Robot
- ServiceAccesGUI


![ref1] 
Dato che siamo interessati ad analizzare solo il core-business del sistema, implementeremo la GUI come una semplice applicazione che simula l'interazione tra un camionista e il servizio.
Inoltre non prenderemo in considerazione l'esistenza del raspberry e non implementeremo le funzionalitÃ  di stop/resume del transporttrolley, le quali verranno trattate nell'ultimo sprint. 
### Formalizzazione dei requisiti
Di seguito sono riportati gli elementi individuati nello sprint0 che sono coinvolti in questa fase. 
#### **EntitÃ** 

|**EntitÃ** |**AttvitÃ** |**Tipo**|**Descrizione**|
| :-: | :-: | :-: | :-: |
|ColdStorageService|Proattivo & Reattivo|Attore|Gestisce le richieste di fridgeTruck e gestice le azioni del Transport Trolley. Inoltre invia aggiornamenti agli Alarm Devices .|
|basicRobot|Reattivo|Attore|EntitÃ  che riceve comandi dal TransportTrolley e guida il DDR robot.|
|transportTrolley|Proattivo e Reattivo|Attore|EntitÃ  principale del ColdStorage system, che si muove all'interno della Service Area ed esegue le azioni di trasporto.|
|ServiceAcessGUI-simulator|Reattivo|Attore|EntitÃ  che funge da simulatore di store request e di interazione tra camionista e servizio|
#### **Dati**

|Dati|Tipo|Descrizione|
| :- | :- | :- |
|truckLoad|variabile intera > 0|Indica il peso diverso di ogni food-load che il truckFridge vuole scaricare, espresso in KG.|
|MAXW|costante intera > 0|Indica la quantitÃ  massima,espressa in KG, che il container della ColdRoom puÃ² dedicare alla conservazione del cibo . |
|Ticket|variabile intera > 0|Viene generato dal servizio una volta che il driver della truckFridge ha inviato una **storerequest**.|
|TruckTicket|variabile intera > 0|Si tratta dell'intero associato al precedente ticket generato, che risulta essenziale per il driver quando dovrÃ  inserirlo al momento dell'arrivo presso la porta INDOOR. Questo numero Ã¨ importante per verificare se il ticket Ã¨ ancora valido o se Ã¨ scaduto.|
|TICKETTIME <br>(da inserire nel kotlin)|variabile intera > 0|Indica un tempo espresso in secondi, che il driver ha a disposizione, una volta che la ColdStorageService ha accettato la **storerequest** per recarsi alla porta INDOOR.|
|RD|costante intera > 0|Lunghezza di ogni lato del TransportTrolley.|
#### **Messaggi**

|Messaggio|Tipo|Mittente|Destinatario|Descrizione|
| :- | :- | :- | :- | :- |
|storerequest|Richiesta|Fridge truck driver|ColdStorageService|Chiede se il foodload di FW kg puÃ² essere scaricato nella ColdStorageService.|
|loadaccepted|Risposta|ColdStorageService|Fridge truck driver|C'Ã¨ abbastanza spazio per depositare il nuovo food-load.|
|loadrejected|Risposta|ColdStorageService|Fridge truck driver|Non c'Ã¨ abbastanza spazio per depositare il nuovo food-load.|
|verifyticket|Richiesta|ServiceAccessGUI|ColdStorageService|Quando il Fridge truck driver usa la ServiceAcessGUI, inserisce il numero del ticket per verificare se la durata del ticket sia valida o sia scaduta.|
|chargetaken|Risposta|ColdStorageService|ServiceAccessGUI|ColdStorageService ha validato il ticket, il TransportTrolley si avvicinerÃ  a INDOOR, preleverÃ  il food-load e il servizio invierÃ  il messsaggio sulla ServiceAccessGUI utile al Fridge truck driver per liberare la porta INDOOR. |
|invalidticket|Risposta|ColdStorageService|ServiceAccessGUI|ColdStorageService ha elaborato che il ticket non Ã¨ piÃ¹ valido/scaduto.|



### Componenti della Core-Business
#### **ServiceAccessGUI-Simulator**
Componente che simula l'interazione tra i camionisti e il ColdStorageService inviando storerequest e verifyticket. 
#### **ColdStorageService**
Componente che implementa la logica di business della nostra applicazione. 
##### **Requisiti aggiuntivi**
Il fridgeTruck deve andarsene il prima possibile e il tempo per il ritiro dei rifiuti Ã¨ sempre limitato e prevedibile, quindi: 

- Se la ColdStorageService risponde con la loadacceptedallora il fridgeTruck-driver deve attendere il completamento del ritiro, e mandato via subito dopo.
- Se la ColdStorageService risponde con un loadrejected allora il fridgeTruck-driver deve essere mandato via immediatamente.
##### **Service Area**
Date l'immagine e la descrizione della service area da parte del committente, si possono cosÃ¬ sintetizzare gli elementi principali: 

- la stanza Ã¨ di forma rettangolare
- la zona HOME Ã¨ posta nell'angolo nord-ovest. Il robot si trova qui all'avvio dell'applicazione
- l'INDOOR Ã¨ un'area sul lato opposto a HOME in cui il transport trolley deve recarsi ogni volta che un truck load Ã¨ pronto a essere depositato
- la cold room Ã¨ la cella frigorifera dove si depositano i carichi. E' accessibile tramite una porta sul lato rivolto verso l'INDOOR
- Ã¨ presente un ostacolo in mezzo alla stanza

Grazie al software unibo.mapperQak23 fornito dal committente, Ã¨ possibile ottenere una rappresentazione a griglia della service area, in cui ogni cella Ã¨ localizzata tramite un sistema di coordinate (colonna,riga).
Ogni cella Ã¨ un quadrato di lato pari a RD e HOME si trova in posizione (0,0).
La rappresentazione ottenuta Ã¨ la seguente:

![ref2] 

La posizione HOME Ã¨ rappresentata dalla cella (0,0), INDOOR occupa le celle (0,4) e (1,4), PORT occupa le celle (4,3) e (5,3). 

Legenda: 

- r: posizione corrente del robot
- 1: celle libere
- X: celle occupate da un ostacolo/cold room

#### **TransportTrolley & BasicRobot**
Il TransportTrolley e il DDR Robot sono gli elementi del sistema che effettuano le operazioni di stoccaggio: 

- pickup
- transport
- stock

In particolare, il transportTrolley interagisce con la ColdStorageService e invia comandi al DDR Robot, che esegue le varie operazioni di stoccaggio. 
Il Robot che utilizzeremo per il progetto sarÃ  virtuale ed eseguirÃ  quindi all'interno di un ambiente virtuale. 
## Analisi del Problema
### Core-Business Software
#### **ServiceAccesGUI-Simulator**
Il simulatore sarÃ  realizzato come un QAK actor in grado di generare storerequest consecutive, intervallate da richieste di tipo verifyticket.
In questo modo potremo testare il funzionamento della business logic della nostra applicazione senza dover sviluppare una GUI stand alone apposita.
L'analisi e il progetto di quest'ultima verranno affrontati nello sprint3. 
#### **ColdStorageService**
Questo Ã¨ il componente centrale del sistema e dovrÃ  occuparsi di varie operazioni (interfacciamento con GUI e raspberry, generazione e gestione dei ticket, controllo del transporttrolley...).
Per questo motivo risulta vantaggioso scomporlo in piÃ¹ sotto-componenti, in primis per rispettare il principio di singola responsabilitÃ  e, in secondo luogo, per rendere l'applicazione piÃ¹ modulare e migliorarne reusability e maintainability in visione di futuri aggiornamenti.
Ogni parte sarÃ  implementata come un attore QAK poichÃ¨ dovrÃ  essere in grado sia di ricevere che inviare messaggi.
Introduciamo i due componenti della ColdStorageService TicketManager e Coldroommanager, il loro funzionamento dettagliato sarÃ  esplicitato meglio in fase di progettazione. 

Una prima suddivisione puÃ² comprendere i seguenti attori: 

- ColdStorageService

si occupa di interfacciarsi con gli altri contesti e di inoltrare eventuali richieste ai giusti attori. E' quindi l'access point dell'applicazione 

- Transporttrolley

rappresenta l'astrazione logica del DDR robot, si occupa di inoltrare i comandi di movimento al basic robot e funge quindi da unitÃ  di controllo di alto livello 

- Ticketmanager

si occupa della generazione, memorizzazione e verifa dei ticket legati a ogni storerequest andata a buon fine 

- Coldroommanager

si occupa di gestire i contatori della cold room ed, eventualmente, svuotarla periodicamente 
#### **Service area**
Nonostante la zona INDOOR e la porta della cold room occupino due celle ciascuna nel nostro modello di service area, abbiamo deciso di scegliere soltanto una delle due come destinazione dei movimenti del DDR robot, in modo da poterle cablare direttamente nel codice.
Questo per tre motivi principali: 

- semplicitÃ  e velocitÃ  di implementazione
- non prevediamo che la forma della stanza possa cambiare in futuro
- il basic robot Ã¨ giÃ  dotato di un componente in grado di calcolare il miglior path dato un punto di partenza e un goal. Tenere conto del fatto che il goal potrebbe essere composto da due posizioni invece di una non inficia considerevolmente l'efficienza dei movimenti del robot

In alternativa si potrebbe estendere la libreria unibo.planner23-1.0 in modo da rappresentare le celle corrispondenti all'INDOOR e alla porta con degli specifici nomi logici invece delle semplici coordinate (x,y).
Valuteremo se implementare questa soluzione in uno sprint futuro. 
#### **DDR robot**
Il DDR robot Ã¨ l'end point finale dell'applicazione: Ã¨ il componente fisico che effettuerÃ  i movimenti e le azioni di deposito all'interno della service area.
Lo si puÃ² pensare come un robot fisico reale o come, nel caso di questo progetto, un robot virtuale.
Infatti il committente ci ha fornito, oltre al software giÃ  citato, anche un'implementazione di tale robot virtuale, compatibile con il modello Qak a scambio di messaggi .
Il progetto unibo.basicrobot23 rappresenta lo strato software da interporre tra la nostra applicazione e l'implementazione del robot ed Ã¨ esso stesso basato sul modello qak, quindi facilmente integrabile al nostro sistema.
Il transporttrolley si occuperÃ  di comunicare al basic robot i movimenti da effettuare.
Una caratteristica importante del basic robot Ã¨ la sua completa indipendenza dalla realizzazione sottostante del DDR robot, per questo motivo in futuro sarÃ  possibile sostituire il virtual robot con uno fisico senza cambiare nulla del codice della nostra applicazione. 
### Interazioni
#### **Formato dei Messaggi**
Per il formato dei messaggi abbiamo pensato di utilizzare il linguaggio Prolog poichÃ¨ esso Ã¨ anche il formato dei messaggi del linguaggio a meta-modello **QAK**. 
#### **Lista Messaggi della Core Business**

|Messaggio|Tipo|Mittente|Destinatario|Descrizione|
| :- | :- | :- | :- | :- |
|storeRequest|Richiesta|ServiceAcessGUI|ColdStorageService|Chiede se il foodload kg puÃ² essere scaricato nella ColdStorageService|
|loadaccepted|Risposta|ColdStorageService|ServiceAccesGUI|Informa il fridge truck driver(ServiceAcessGUI) che c'Ã¨ abbastanza spazio per il suo foodload.|
|loadrejected|Risposta|ColdStorageService|ServiceAcessGUI|Non Ã¨ possibile depositare il nuovo foodload poichÃ¨ potrebbe non esserci disponibilitÃ  nella ColdRoom.|
|deposit|Richiesta|ColdStorageService|TransportTrolley|Chiede al TransportTrolley di eseguire un'azione di deposito (Indoor->scarico->cold room->deposito)|
|verifyticket|Richiesta|ServiceAcessGUI|ColdStorageService|una volta arrivato all'indoor, il driver inserisce il suo ticket tramite la gui per verificare che sia valido e poter procedere con il deposito|
|pickupCompleted|Risposta|TransportTrolley|ColdStorageService|Informa il ColdStorageServiceche il foodload Ã¨ stato prelevato dalla porta Indoor.|
|depositCompleted|Dispatch|TransportTrolley|ColdStorageService|Informa il ColdStorageService che il foodload Ã¨ stato scaricato nella coldroom|
|chargetaken/invalidTicket|Risposta|ColdStorageService|ServiceAcessGUI|Possibili messaggi di risposta (da parte del service) che il driver riceve a seguito di una **verifyticket**|
|goaway|Risposta|ColdStorageService|ServiceAcessGUI|Messaggio destinato al driver per indicargli di liberare la porta INDOOR e andar via. Viene inviato alla fine di un operazione di pickup avvenuta con successo. |

Abbiamo introdotto i due messaggi "depositcomplted e pickupcompleted" e il terzo messaggio "deposit" 
#### **ServiceAcessGUI ↔ ColdStorageService**
Le richieste del cliente non specificano se vi sono regole di prioritÃ  per quanto riguarda le richieste dei vari **fridge-truck**, pertanto si presuppone che verranno serviti in ordine di arrivo. Validazione della storeRequest **Problema**: cosa succede se l'utente inserisce un carattere al posto di un numero o se inserisce un qualsiasi altro parametro in formato errato (anche aggiunto in futuro) nella ServiceAcessGUI? Oppure cosa succede se alla ColdStorageService arriva una **storeRequest** non valida? 

**Soluzione**: 

- convalida latoclient: se l'utente inserisce un parametro in formato errato, la ServiceAcessGUI ritorna un errore e la richiesta non verrÃ  inviata. 
  convalida latoserver: se la ColdStorageService riceve una storeRequest non valida, allora risponde con un loadrejected. 


**Motivazione soluzione:** 

- Questa soluzione Ã¨ la piÃ¹ semplice da implementare e la piÃ¹ efficiente, poichÃ¨ le storeRequest non valide vengono semplicemente ignorate e sprecando meno tempo e risorse computazionali. 
- Allo stato attuale del progetto, il livello di sicurezza del sistema non impedisce a un malintenzionato di inviare storeRequest non valide ed una convalida latoserver dovrebbe rendere piÃ¹ sicuro il sistema. 

Viene riportato qui un esempio delle possibili interazioni tra i vari attori del sistema a seguito di una storeRequest: 

![storeRequestdrawio][ref1] 

Congedare il fridgeTruck **Problema**: Quando e che tipo di messaggio bisogna inviare per congedare il fridge-truck ? Bisogna introdurre nuovi messaggi? Cosa accade se il fridge-truck driver dalla ServiceAcessGUI invia una storeRequest al TicketManager/ColdStorageService mentre vi Ã¨ in corso una **Deposit-Action** ? 

**Soluzione**: 

- La loadaccepted viene inviata quando l'azione di prelievo da parte del TransportTrolley Ã¨ terminata (invio del messaggio pickupCompleted ). 
  La riposta viene inviata solo quando il fridgeTruck puÃ² essere congedato (per fare spazio e liberare la porta INDOOR e quindi proseguire con le successive richieste da parte di altri fridgeTruck-driver).
- L' introduzione del nuovo attore TicketManager che si occuperÃ  validare o meno i ticket (in base alla loro scadenza **TICKETTIME**) e di eventualmente eliminare un ticket, mentre al TransportTrolley lasciamo gestire/eseguire la Deposit-Action.
- Se il fridge-truck driver, dalla ServiceAcessGUI, invia una storeRequest mentre vi Ã¨ in corso una Deposit-Action, questa potrÃ  essere tranquillamente gestita dal coldroom manager dato che Ã¨ il transport trolley a occuparsi del deposito.
#### **DDR Robot ↔ TransportTrolley**
L'interazione tra le due componenti avviene tramite l'utilizzo dei messaggi messi a dispozione dall'interfaccia di BasicRobot. 
#### **ColdStorageService ↔ TicketManager** 
Convalida ticket **Problema**: Cosa accade se l'utente inserisce un numero di ticket non valido o con TICKETTIME scaduto? Cosa fare per evitare di far attendere per molto tempo il driver che Ã¨ in attesa di una risposta di validazione o meno del ticket? 

**Soluzione**: Dopo una discussione con il committente, vedi [DiscussioniTemaFinale23](../commons/html/DiscussioniTF23.html), ipotizziamo che nessun driver cerchi di imbrogliare (rubando ticket,etc.). 
La ServiceAccesGUI-Simulator invia un verifyticket al ColdStorageService, che lo inoltra al **TicketManager** ed Ã¨ quest'ultimo che verifca effettivamente se il ticket sia valido o meno. 
Il ticket potrebbe risultare non valido per due ragioni: 

- Il fridgeTruck-driver ha inserito un codice ticket errato portandolo sul reinserimento del ticket.
- Il TICKETTIME Ã¨ scaduto e il driver viene mandato via.


Per evitare di far attendere per molto tempo il driver, dopo aver discusso col cliente ( vedi [DiscussioniTemaFinale23](../commons/html/DiscussioniTF23.html)), si puÃ² risolvere il problema impostando una interazione a DUE-FASI tra driver e service (compito che puÃ² svolgere la serviceAcessGui): 

- FASE1: il driver invia il ticket e attenda una risposta (immediata) come ad esempio ticketaccepted/ticketrejected (sarebbero le nostre chargetaken/invalidticket) 
- FASE2: il driver invia la richiesta loaddone e attenda una seconda risposta (chargeTaken o fallimento per cause legate al servizio) (chargeTaken sarebbe la nostra goaway)

![ref3] 
#### **Nota**
1. Per semplicitÃ  in questa fase non abbiamo analizzato nel dettaglio le interazioni a seguito di una risposta da parte del service di invalidticket, ovvero la diversa gestione dei casi in cui l'invalid ticket Ã¨ causato da un inserimento errato del ticket o dal ticket scaduto(**TICKETTIME** scaduto). 
### Architettura Logica
Grazie alla funzione **delegate** del linguaggio QAK, Ã¨ possibile far gestire i messaggi inviati dalla gui al cold storage service direttamente agli opportuni attori, senza dover generare nuovi messaggi.
Inoltre, in questo modo, i vari attori possono rispondere alle richieste senza dover passare nuovamente per il cold storage service.
CiÃ² permette di risparmiare sulla quantitÃ  di messaggi da inviare, oltre che consetire la scrittura di codice piÃ¹ pulito e leggibile.
I messaggi che ci serviranno per implementare i comportamenti mostrati nei diagrammi di sequenza precedenti sono: 

|Messaggio|Tipo|Mittente|Destinatario|Descrizione|
| :- | :- | :- | :- | :- |
|storerequest|request|serviceaccessGUI-simulator|coldstorageservice|richiesta di deposito di un certo numero di kg di cibo|
|loadaccepted/loadrejected|reply|coldroommanager|serviceaccessGUI-simulator|accept/reject del carico richiesto dal driver|
|verifyticket|request|serviceaccessGUI-simulator|coldstorageservice|richiesta di verifica del ticket inserito dal driver una volta arrivato all'indoor|
|chargetaken/invalidticket|reply|ticketmanager|serviceaccessGUI-simulator|accept/reject del ticket inserito dal driver|
|loaddone|request|serviceaccessGUI-simulator|coldstorageservice|richiesta inviata dal driver per sapere quando potrÃ  liberare l'indoor |
|goaway/depositfailed|reply|transporttrolley|serviceaccessGUI-simulator|completamento/fallimento dell'azione di scarico dal camion, in entrambi i casi il driver dovrÃ  liberare l'indoor|
|generateticket|request|coldroommanager|ticketmanager|richiesta di generazione di un nuovo ticket per completare la risposta positiva a una storerequest|
|ticket|reply|ticketmanager|coldroommanager|invio del ticket generato richiesto dal cold room manager|
|depositdone/depositfailed|dispatch|transporttrolley|coldroommanager|messaggio di successo/fallimento dell'azione di deposito per l'eventuale aggiornamento dello stato della cella|
|expiredticket|dispatch|ticketmanager|coldroommanager|notifica al cold room manager il fatto che il tickt inserito Ã¨ scaduto in modo da poter aggiornare lo stato della cella|


l'architettura logica risultante Ã¨ la seguente:
![ref4] 
[file qak architettura logica](./sprint1_analisiproblema/src/sprint1_analisiproblema.qak)
## Progettazione e sviluppo
Il progetto contiene tutto il software sviluppato dal nostro team per la Core-Business dell'applicazione finale. 
### PATTERN FACADE
Decidiamo di utilizzare e introdurre il modello del **PATTERN FACADE**. 
Nell'immagine dell'architettura logica vi Ã¨ l'attore intermedio denominato "coldstorageservice" che funge da controller per il pattern FACADE . 

**MOTIVAZIONE SCELTA:** Il pattern FACADE Ã¨ un design pattern strutturale che fornisce un'interfaccia semplificata a un sottosistema complesso.
Lo scopo Ã¨ rendere piÃ¹ facile l'utilizzo di un sistema complesso fornendo un'interfaccia unica e di alto livello che renda la complessitÃ  sottostante piÃ¹ gestibile. 

VANTAGGI: 

- Nasconde le complessitÃ  di un sistema piÃ¹ ampio e fornisce un'interfaccia piÃ¹ semplice al client. CiÃ² rende il sistema piÃ¹ facile da usare, capire e testare.
- Minimizza le dipendenze tra il client e il sottosistema, riducendo il coupling. I client devono essere consapevoli solo del *controller FACADE*, non della complessitÃ  sottostante.
- Viene spesso utilizzato quando un sistema Ã¨ molto complesso o difficile da capire perchÃ© il sistema ha molte classi interdipendenti o il suo codice sorgente non Ã¨ disponibile.
### ServiceAccesGUI-Simulator
E' un simulatore della ServiceAccesGUI,modellato come un attore QAK per interagire con il service e viene utilizzato per: 

1. simulare l'arrivo del fridgeTruck-driver: invia al ColdStorageService una storerequest e attende una risposta dal ColdRoomManager che verifica se la richiesta puÃ² essere presa in carica o meno in quel determinanto momento. RiceverÃ  dal service un loadaccepted o una loadrejected nel caso in cui in quel momento Ã¨ impossibile depositare altro cibo, riportando cosÃ¬ la SAG(simulator) nello stato iniziale. 
1. simulare l'interazione con il ColdStorageService: una volta che il servizio ha preso in carico la richiesta invia alla SAG(simulator) un loadaccepted e un intero corrispondente al numero del ticket. A questo punto il ServiceAccesGUI-Simulator inserisce il ticket e attende che il service verifichi che il ticket inserito sia corretto e ancora valido. Nel caso in cui il ticket sia ancora valido, il service risponderÃ  con un chargetaken; il SAG-simulator invierÃ  un loaddone (con parametro il numero del ticket) e attenderÃ  che l'azione di deposito da parte del service sia completata o fallita. Il service invierÃ :

-o un goaway nel caso di azione di pickup avvenuta successo, quindi che il robot ha scaricato la merce dal camion. Il service continuerÃ  con l'azione di deposito, per completare la richiesta fatta dal driver che Ã¨ appena andato via
-o un depositfailed nel caso contrario. 

In entrambi i casi verrÃ  visualizzato a video il messaggio di liberare la porta INDOOR per procedere con nuove richieste. 
#### **NOTA:** 

E' un simulatore per ora poichÃ¨ non deve andare a complicare la realizzazione della Core-Business.
La ServiceAcessGUI la andremo a realizzare nello sprint successivo, ovvero nello sprint2. [(Link allo sprint2)](../sprint2/) 
### ColdStorageService
E' il cuore del progetto. E' l' elemento principale della core-business ma anche l'elemento principale del sistema distribuito nel complesso. 
In questa fase pensiamo alla gestione corretta nell' invio di invalidticket che potrebbe essere inviato o a causa dell'errato inserimento del numero del ticket o causa di un ticket scaduto.
![ref5] 
#### **ColdStorageService**
Riceve le richieste provenienti dalla ServiceAccesGUI (simulator per ora) e gestisce le interazioni con gli altri componenti (come ticketmanager, coldroomManager,transportTrolley). 
#### **TransportTrolley**
Riceve la richiesta di deposito, inoltrata dalla ColdStorageService, e la inoltra al basicRobot. Una volta che il basicrobot puÃ² essere ingaggiato per eseguire le azioni di prelievo e di deposito si mette in attesa che arrivi una loaddone da parte del SAG(-simulator) per poi inviare la richiesta al basicrobot di spostarsi nella cella corrispondente alla porta INDOOR (0,4). 

Per semplicitÃ  abbiamo pensato di cablare come cella fissa della porta INDOOR, la cella situata in posizione (0,4) escludendo l'utilizzo della seconda cella adiacente in posizione(0,5) e di cablare la cella fissa della porta della ColdRoom quella presente in posizione (4,3) escludendo la seconda cella disponibile in (5,3) per eseguire il deposito nella ColdRoom.

Se il **basicrobot** non Ã¨ impegnato con altre richieste, si muove verso la porta INDOOR e inizia a scaricare la merce dal camion e rispondendo alla richiesta di loaddone(inviata dalla SAG-simulator) con un goaway; il transportTrolley chiederÃ  nuovamente al basicrobot di muoversi, questa volta nella posizione associata alla cella della ColdRoom e se il basicRobot ha eseguito anche questa azione con successo, invierÃ  al transportTrolley un moverobotdone per dire che il basicrobot Ã¨ arrivato davanti alla coldroom e che sta completando la fase di deposito. 
Solo in seguito a un'azione di deposito completata con successo, il transportTrolley inoltra il messaggio di depositdone al ColdRoomManager in modo tale da aggiornare il peso corrente **CurrentWeight** della ColdRoom. 
#### **ColdRoomManager**
Abbiamo introdotto le variabili contatore **CurrentWeight** e **FreeSpace** per una gestione migliore della disponibilitÃ  corrente della ColdRoom. 

|Nome|Tipo|Descrizione|
| :- | :- | :- |
|CurrentWeight|variabile intera > 0|Indica il peso realmente disponibile per le varie richieste di deposito, inizializzato con il valore **0** e si aggiorna solo nel momento in cui la richiesta di deposito viene eseguita con successo.|
|FreeSpace|variabile intera > 0|Ã¨ un indicatore fittizio di CurrentWeight ,inizializzato con il valore **100** che simula la disponibilitÃ  reale nella ColdRoom.<br>Si aggiorna solo dopo aver verificato che il truckLoad della richiesta sia inferiore a **FreeSpace** stesso, quindi viene usato nelle prime fasi di validazione della richiesta.|

Per gestire le varie richieste di deposito, abbiamo utilizzato una lista del tipo **hashMapOf<Int,Int>** dove il primo intero Ã¨ associato al **TruckTicket** ovvero l'id del ticket e il secondo intero al **truckLoad** ovvero il foodload espresso in KG. 
Riceve la storeRequest inoltrata dal ColdStorageService e valuta se la richiesta di deposito possa essere accettata o meno: 

-se non c'Ã¨ abbastanza spazio,rifiuta la richiesta rispondendo con un loadrejected .
-se c'Ã¨ abbastanza spazio, aggiorna il contatore fittizio **FreeSpace** sottraendo ad esso il foodload della storeRequest corrente, invia una richiesta al ticketmanager di generare un nuovo ticket,attende che gli venga inviato e lo inoltra alla SAG(simulator); inserisce la richiesta(passandogli il TruckTicket e il truckLoad) nella lista Requests e risponde alla storeRequest della SAG-simulator con una loadaccepted (e il Truckticket ad esso associato);solo quando il transportTrolley invia un depositdone il ColdRoomManager aggiorna il reale contatore **CurrentWeight** sommandogli il foodload corrente e rimuove la richiesta dalla lista .
#### **Ticket Manager**
Ogni ticket Ã¨ composto da due interi: id del ticket e TICKETTIME e viene mantenuto in una **hashMapOf<Int,Long>**. 
Il Ticket Manager riceve due tipi di richieste (inoltrate dal coldstorageservice) : 

1. di generazione di un nuovo ticket generateticket. 
   Il ticket sarÃ  composto da due interi: il primo intero viene assocciato all'id del ticket e viene utilizzata la classe Random per generare un intero compreso tra 1 e 10000; il secondo intero viene usato per associare al ticket un **TICKETTIME** calcolato dalla somma del tempo attuale, piÃ¹ 10 secondi, piÃ¹ 0,5 secondi per ogni kg del truckLoad. 
1. di verificare che il ticket inserito sia valido, quindi verifyTicket e risponderÃ  direttamente alla SAG (simulator in questo sprint) con invalidTicket o chargeTaken. 
### Architettura Logica
Di seguito viene riportata l'immagine dell'architettura logica corrispondente al progetto:
![ref3] 
LINK al modello qak del progetto: [sprint1_progetto.qakx](./sprint1_progetto/src/sprint1_progetto.qakx) 
## Test plan
Il qak di riferimento per l'escuzione dei seguenti test Ã¨ [test_coldstorageservice_sprint1.qak](./sprint1_progetto/src/test_coldstorageservice_sprint1.qak)
La classe java che implementa gli unit test Ã¨ [sprint1_test.java](./sprint1_progetto/test/sprint1_progetto/sprint1_test.java)

|Test|Descrizione|Risultato|
| :- | :- | :- |
|testLoadAccepted|verifica che, in caso di una storerequest da parte dell'accessgui e se c'Ã¨ abbastanza spazio nella cold room, l'applicazione risponda con un messaggio di loadaccepted|[output testloadaccepted](./resources/imgs/test_loadaccepted.png)|
|testLoadRejected|verifica che, in caso di una storerequest da parte dell'accessgui e se non c'Ã¨ abbastanza spazio nella cold room, l'applicazione risponda con un messagio di loadrejected|[output testloadrejected](./resources/imgs/test_loadrejected.png)|
|testValidTicket|verifica che, in seguito a una verifyticket da parte dell'accessgui, l'applicazione risponda con un messaggio di chargetaken se il ticket inserito esiste e non Ã¨ scaduto|[output testvalidticket](./resources/imgs/test_validticket.png)|
|testWrongTicket|verifica che, in seguito a una verifyticket da parte dell'accessgui, l'applicazione risponda con un messaggio di invalidticket con parametro wrong\_ticket se il ticket inserito non esiste|[output testwrongticket](./resources/imgs/test_wrongticket.png)|
|testExpiredTicket|verifica che, in seguito a una verifyticket da parte dell'accessgui, l'applicazione risponda con un messaggio di invalidticket con parametro expired\_ticket se il ticket inserito esiste ma Ã¨ scaduto|[output testexpiredticket](./resources/imgs/test_expiredticket.png)|



Per testare il movimento del virtual robot durante l'esecuzione di una deposit action Ã¨ stato predisposto uno specifico qak:[test_deposit_sprint1.qak](./sprint1_progetto/src/test_deposit_sprint1.qak)
Il test si basa sul fatto che gli attori qak sono anche risorse CoAP, quindi Ã¨ stato implementato un CoapObserver per tenere traccia degli aggiornamenti di stato notificati dal transporttrolley tramite la funzione updateResource.
In questo modo Ã¨ possibile verificare che la lista delle azioni compiute dal robot Ã¨ uguale a una serie di azioni che ci aspettiamo vengano svolte (spostamento verso indoor, scarico, movimento verso port, deposito, ritorno in home).
La classe kotlin che implementa il test Ã¨: [sprint1_test_deposit.kt](./sprint1_progetto/test/sprint1_progetto/sprint1_test_deposit.kt) 

|Test|Descrizione|Risultato|
| :- | :- | :- |
|test deposit|simulazione di invio di una richiesta di loaddone per verificare che il transporttrolley risponda con una goaway e che il robot esegua una specifica serie di azioni|[output testdeposit](./resources/imgs/test_deposit.png)|
## Conclusioni
### Deployment
### WorkPlan
#### **Sprint2 - ServiceAccesGUI**
Lo sprint2 prevede la realizzazione dell' interfaccia grafica ServiceAccessGUI utile per: 

- vedere lo stato corrente (**CurrentWeight**) della ColdRoom, ovvero il peso in KG depositato in quel momento. 
- inviare una richiesta di deposito (**storeRequest**) alla ColdStorageService 
- inserimento numero del ticket (precedentemente inviato dal service) 
- visualizzazione del messaggio di liberare l'INDOOR
#### **Sprint3 - RaspberryPi**


E' stato deciso che la quantitÃ  di tempo che sarÃ  dedicata al completamento dello Sprint2 sarÃ  di circa **15 ore umane** . 
[Sprint2](../sprint2/Sprint2_v1.html) link per lo sprint2 (ServiceAccesGUI). 

By andrea.isernio@studio.unibo.it & fabio.gentili3@studio.unibo.it 

GIT repo: [https://github.com/FabioGentili99/ColdStorageService ](https://github.com/FabioGentili99/ColdStorageService)

[ref1]: Aspose.Words.40fa9545-7bd6-4de8-851e-36687fc040fb.001.png
[ref2]: Aspose.Words.40fa9545-7bd6-4de8-851e-36687fc040fb.002.png
[ref3]: Aspose.Words.40fa9545-7bd6-4de8-851e-36687fc040fb.003.png
[ref4]: Aspose.Words.40fa9545-7bd6-4de8-851e-36687fc040fb.004.png
[ref5]: Aspose.Words.40fa9545-7bd6-4de8-851e-36687fc040fb.005.png
