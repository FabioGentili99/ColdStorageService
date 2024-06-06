

<div align="center">
  <img align="center" width="25%" src="/commons/html/_images/robottino.png"/>
  
  <h1>ColdStorage Service</h1>

Sistema software DISTRIBUITO per la gestione automatizzata di un servizio di storage di cibo in una cella frigorifera tramite l'uso di un _**robot dotato di carrello elevatore  **_. Il progetto è stato sviluppato per il corso di [Ingegneria dei Sistemi Software M](https://www.unibo.it/en/teaching/course-unit-catalogue/course-unit/2023/468003) dell' università di Bologna, Usando [SCRUM agile framework](https://www.scrum.org/resources/what-is-scrum).
<br/>Vedi la versione del <a href="README.html" target="_blank" rel="noopener">README.html </a>
</div>

### Components
<table>
  <tr align="center">
    <th width="15%">Nome</th>
    <th width="35%">Descrizione</th>
    <th width="50%">Demo</th>
  </tr>
  <tr align="center">
    <td><b>Virtual&nbsp;Robot</b></td>
    <td>
      Web app che simula l'esecuzione di un Robot . Il robot è posizionato in un <b>virtual environment</b> (una stanza rettangolare in un ambientre virtuale) e può <b>ricevere comandi per eseguire alcune azioni</b>, come muoversi avanti/indietro per un certo periodo di tempo e ruotare.
    </td>
    <td><img src="/commons/html/_images/ColdStorageServiceRoomAnnoted.PNG"/></td>
  </tr>
  <tr align="center">
    <td><b>ColdStorage&nbsp;Core</b></td>
    <td>
     Componente principale del sistema. Esso <b>gestisce il ColdStorage</b>. Riceve storerequest (richiesta di deposito) dagli utenti, le elabora e delega un <i>Transport Trolley</i> per prelevare e depositare il foodload, nel caso ci sia abbastanza spazio nel container della ColdRoom.
    </td>
    <td><img src="/sprint1/sprint1_progetto/coldstorageservicearch.png"/></td>
  </tr>
  <tr align="center">
    <td><b>ColdStorage&nbsp;RaspPi</b></td>
    <td>
			Componente che può essere eseguito su un Raspberry Pi. Implementa un dispositivo di <b>alarm/warning</b>: quando un sensore sonar ad ultrasuoni rileva una distanza minore di un valore soglia, invia un messaggio al sistema, che stopperà/riprenderà l'attività del Transport Trolley; ColdStorage RaspPi mostra anche informazioni riguardo il stato attuale del sistema tramite un led.
	</td>
    <td><img src="/commons/html/_images/img_raspRitagliata.jpg"/></td>
  </tr>
  <tr align="center">
    <td><b>Service&nbsp;Access&nbsp;GUI</b></td>
    <td>
			Web app per consentire al client di interagire con il sistema e <b>inviare richeste di servizio</b>. Permette di inviare una richiesta, verificare la convalida del ticket precedentemente ricevuto verificare il peso depositato in un determinato momento (current weight).
		</td>
    <td><img src="/commons/html/_images/SAG_storeRequest_ritagliata.png"/></td>
  </tr>
  
</table>

### Roadmap
- [x] **Sprint0** - Analisi dei requisiti e struttura base del sistema: 
  - Latest Release (html format): [Cold Storage Service - Sprint0_v1.html](sprint0/Sprint0_v1.html)
  - Sprint Review: 19/09/2023
- [x] **Sprint1** -  Core Business: 
  - Latest Release (html format): [Sprint1_v1.html](sprint1/Sprint1_v1.html)
  - Sprint Review: 10/11/2023
- [x] **Sprint2** - Service Access GUI:
  - Latest Release (html format): [Sprint2_v2.html](sprint2/Sprint2_v2.html)
  - Sprint Review: 07/05/2024
- [x] **Sprint3** - Raspberry Pi: 
  - Latest Release (html format): [Sprint3_v0.html](sprint3/Sprint3_v0.html)
  - Sprint Review: 04/06/2024
  - Final Exam: XX/XX/2024

## Team
<table>
  <!--<tr align="center"><td colspan="3"><b>Team BCR</b></td></tr>-->
  <tr align="center">
    <td><a href="https://github.com/FabioGentili99">FabioGentili99</a></td>
    <td><a href="https://github.com/Andreaise">Andreaise</a></td>
  </tr>
  <tr align="center">
    <td><b>Fabio Gentili</b></td>
    <td><b>Andrea Isernio</b></td>
  </tr>
</table>
