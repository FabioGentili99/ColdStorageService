%====================================================================================
% coldstorageservice description   
%====================================================================================
context(ctxcoldstorageservice, "localhost",  "TCP", "9990").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxaccessgui, "127.0.0.1",  "TCP", "9993").
 qactor( basicrobot, ctxbasicrobot, "external").
