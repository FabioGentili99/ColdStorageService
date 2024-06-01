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
dispatch( cmd, cmd(MOVE) ).
dispatch( end, end(ARG) ).
request( step, step(TIME) ).
reply( stepdone, stepdone(V) ).  %%for step
reply( stepfailed, stepfailed(DURATION,CAUSE) ).  %%for step
request( doplan, doplan(PATH,STEPTIME) ).
reply( doplandone, doplandone(ARG) ).  %%for doplan
reply( doplanfailed, doplanfailed(ARG) ).  %%for doplan
dispatch( nextmove, nextmove(M) ).
dispatch( nomoremove, nomoremove(M) ).
dispatch( robotready, robotready(BOOL) ).
dispatch( setdirection, dir(D) ).
request( moverobot, moverobot(TARGETX,TARGETY) ).
reply( moverobotdone, moverobotok(ARG) ).  %%for moverobot
reply( moverobotfailed, moverobotfailed(PLANDONE,PLANTODO) ).  %%for moverobot
request( getrobotstate, getrobotstate(ARG) ).
reply( robotstate, robotstate(POS,DIR) ).  %%for getrobotstate
dispatch( stopplan, stopplan(_) ).
dispatch( continueplan, continueplan(_) ).
dispatch( setrobotstate, setpos(X,Y,D) ).
dispatch( stop, stop(_) ).
dispatch( resume, resume(_) ).
dispatch( home, home(_) ).
dispatch( moving, moving(_) ).
dispatch( stopped, stopped(_) ).
%====================================================================================
context(ctxcoldstorageservice, "localhost",  "TCP", "9990").
context(ctxraspberrypi, "192.168.250.244",  "TCP", "9999").
 qactor( warningdevice, ctxraspberrypi, "external").
  qactor( alarmdevice, ctxraspberrypi, "external").
  qactor( coldstorageservice, ctxcoldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( coldroommanager, ctxcoldstorageservice, "it.unibo.coldroommanager.Coldroommanager").
  qactor( ticketmanager, ctxcoldstorageservice, "it.unibo.ticketmanager.Ticketmanager").
  qactor( transporttrolley, ctxcoldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( planexec, ctxcoldstorageservice, "it.unibo.planexec.Planexec").
  qactor( robotpos, ctxcoldstorageservice, "it.unibo.robotpos.Robotpos").
  qactor( basicrobot, ctxcoldstorageservice, "it.unibo.basicrobot.Basicrobot").
