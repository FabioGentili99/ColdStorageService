%====================================================================================
% alarm_system description   
%====================================================================================
dispatch( home, home(_) ).
dispatch( moving, moving(_) ).
dispatch( stopped, stopped(_) ).
dispatch( stop, stop(_) ).
dispatch( resume, resume(_) ).
%====================================================================================
context(ctxraspberrypi, "localhost",  "TCP", "9999").
context(ctxcoldstorageservice, "192.168.232.182",  "TCP", "9990").
 qactor( coldstorageservice, ctxcoldstorageservice, "external").
  qactor( alarmdevice, ctxraspberrypi, "it.unibo.alarmdevice.Alarmdevice").
  qactor( warningdevice, ctxraspberrypi, "it.unibo.warningdevice.Warningdevice").
