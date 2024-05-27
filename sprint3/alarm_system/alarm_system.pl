%====================================================================================
% alarm_system description   
%====================================================================================
dispatch( home, home(_) ).
dispatch( moving, moving(_) ).
dispatch( stopped, stopped(_) ).
dispatch( stop, stop(_) ).
dispatch( resume, resume(_) ).
%====================================================================================
context(ctxalarmsystem, "localhost",  "TCP", "8300").
context(ctxcoldstorageservice, "192.168.232.182",  "TCP", "8040").
 qactor( coldstorageservice, ctxcoldstorageservice, "external").
  qactor( alarmdevice, ctxalarmsystem, "it.unibo.alarmdevice.Alarmdevice").
  qactor( warningdevice, ctxalarmsystem, "it.unibo.warningdevice.Warningdevice").
