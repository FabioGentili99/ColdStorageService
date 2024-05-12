%====================================================================================
% coldstorageservice description   
%====================================================================================
request( getweight, getweight(NO_PARAM) ).
reply( currentweight, currentweight(CurrentWeight,FreeSpace) ).  %%for getweight
request( storerequest, storerequest(TruckLoad) ).
reply( loadaccepted, loadaccepted(Ticket,FreeSpace,CurrentWeight) ).  %%for storerequest
reply( loadrejected, loadrejected(_) ).  %%for storerequest
request( verifyticket, verifyticket(TruckTicket) ).
reply( chargetaken, chargetaken(_) ).  %%for verifyticket
reply( invalidticket, invalidticket(REASON) ).  %%for verifyticket
request( loaddone, loaddone(TruckTicket) ).
reply( goaway, goaway(_) ).  %%for loaddone
reply( depositfailed, depositfailed(_) ).  %%for loaddone
request( generateticket, generateticket(TruckLoad) ).
dispatch( depositdone, depositdone(Ticket) ).
dispatch( depositfailed, depositfailed(Ticket) ).
dispatch( expiredticket, expiredticket(Ticket) ).
request( engage, engage(OWNER,STEPTIME) ).
request( moverobot, moverobot(TARGET) ).
%====================================================================================
context(ctxcoldstorageservice, "localhost",  "TCP", "9990").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( coldstorageservice, ctxcoldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( coldroommanager, ctxcoldstorageservice, "it.unibo.coldroommanager.Coldroommanager").
  qactor( ticketmanager, ctxcoldstorageservice, "it.unibo.ticketmanager.Ticketmanager").
  qactor( transporttrolley, ctxcoldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
