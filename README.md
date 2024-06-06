☰ Menu

[×](javascript:void\(0\)) <a name="currentsprint"></a>[—— ReadMe ——]() [Sprint0](sprint0/Sprint0_v1.html) [Sprint1](sprint1/Sprint1_v1.html) [Sprint2](sprint2/Sprint2_v2.html) [Sprint3](sprint3/Sprint3_v0.html) 


# **ColdStorage Service**
Sistema software DISTRIBUITO per la gestione automatizzata di un servizio di storage di cibo in una cella frigorifera tramite l'uso di un ***robot dotato di carrello elevatore*** . Il progetto è stato sviluppato per il corso di [Ingegneria dei Sistemi Software M](https://www.unibo.it/en/teaching/course-unit-catalogue/course-unit/2023/468003) dell' università di Bologna, Usando [SCRUM agile framework](https://www.scrum.org/resources/what-is-scrum). 
Vedi la versione del [README di tipo .md ](README.md)
### **Components**

|**Nome**|**Descrizione**|**Demo**|
| :-: | :-: | :-: |
|**Virtual Robot**|Web app che simula l'esecuzione di un Robot . Il robot è posizionato in un **virtual environment** (una stanza rettangolare in un ambientre virtuale) e può **ricevere comandi per eseguire alcune azioni**, come muoversi avanti/indietro per un certo periodo di tempo e ruotare. ||
|**ColdStorage Core**|Componente principale del sistema. Esso **gestisce il ColdStorage**. Riceve storerequest (richiesta di deposito) dagli utenti, le elabora e delega un *Transport Trolley* per prelevare e depositare il foodload, nel caso ci sia abbastanza spazio nel container della ColdRoom. ||
|**ColdStorage RaspPi**|Componente che può essere eseguito su un Raspberry Pi. Implementa un dispositivo di **alarm/warning**: quando un sensore sonar ad ultrasuoni rileva una distanza minore di un valore soglia, invia un messaggio al sistema, che stopperà/riprenderà l'attività del Transport Trolley; ColdStorage RaspPi mostra anche informazioni riguardo il stato attuale del sistema tramite un led. ||
|**Service Access GUI**|Web app per consentire al client di interagire con il sistema e **inviare richeste di servizio**. Permette di inviare una richiesta, verificare la convalida del ticket precedentemente ricevuto verificare il peso depositato in un determinato momento (current weight). ||
### **Roadmap**
- ` `FORMCHECKBOX **Sprint0** - Analisi dei requisiti e struttura base del sistema: 
  - Latest Release (html format): [Sprint0_v1.html](../ColdStorageService/sprint0/Sprint0_v1.html)
  - Sprint Review: 19/09/2023
- ` `FORMCHECKBOX **Sprint1** - Core Business: 
  - Latest Release (html format): [Sprint1_v1.html](../ColdStorageService/sprint1/Sprint1_v1.html)
  - Sprint Review: 10/11/2023
- ` `FORMCHECKBOX **Sprint2** - Service Access GUI: 
  - Latest Release (html format): [Sprint2_v2.html](../ColdStorageService/sprint2/Sprint2_v2.html)
  - Sprint Review: 07/05/2024
- ` `FORMCHECKBOX **Sprint3** - RaspberryPi: 
  - Latest Release (html format): [Sprint3_v0.html](../ColdStorageService/sprint3/Sprint3_v0.html)
  - Sprint Review: 04/06/2024
  - Final Exam: XX/XX/2024
## **Team**

|[FabioGentili99](https://github.com/FabioGentili99)|[Andreaise](https://github.com/Andreaise)|
| :-: | :-: |
|**Fabio Gentili**|**Andrea Isernio**|

