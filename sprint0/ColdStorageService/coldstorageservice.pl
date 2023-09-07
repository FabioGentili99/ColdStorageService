%====================================================================================
% coldstorageservice description   
%====================================================================================
context(ctxcoldstorageservice, "localhost",  "TCP", "9990").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxraspi, "localhost",  "TCP", "9992").
context(ctxaccessgui, "127.0.0.1",  "TCP", "9993").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( testgui, ctxaccessgui, "it.unibo.testgui.Testgui").
  qactor( coldstorageservice, ctxcoldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( transporttrolley, ctxcoldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( alarmdevice, ctxraspi, "it.unibo.alarmdevice.Alarmdevice").
  qactor( warningdevice, ctxraspi, "it.unibo.warningdevice.Warningdevice").
