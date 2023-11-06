%====================================================================================
% coldstorageservice description   
%====================================================================================
request( storerequest, storerequest(TruckLoad) ).
request( verifyticket, verifyticket(TruckTicket) ).
request( loaddone, loaddone(TruckTicket) ).
request( generateticket, generateticket(TruckLoad) ).
dispatch( depositdone, depositdone(Ticket) ).
dispatch( depositfailed, depositfailed(Ticket) ).
dispatch( expiredticket, expiredticket(Ticket) ).
request( engage, engage(OWNER,STEPTIME) ).
request( moverobot, moverobot(TARGET) ).
%====================================================================================
context(ctxcoldstorageservice, "localhost",  "TCP", "9990").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxaccessgui, "10.201.7.226",  "TCP", "9993").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( serviceaccessgui_simulator, ctxaccessgui, "it.unibo.serviceaccessgui_simulator.Serviceaccessgui_simulator").
  qactor( coldstorageservice, ctxcoldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( coldroommanager, ctxcoldstorageservice, "it.unibo.coldroommanager.Coldroommanager").
  qactor( ticketmanager, ctxcoldstorageservice, "it.unibo.ticketmanager.Ticketmanager").
  qactor( transporttrolley, ctxcoldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
