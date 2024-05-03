%====================================================================================
% test_deposit_sprint1 description   
%====================================================================================
request( loaddone, loaddone(TruckTicket) ).
dispatch( depositdone, depositdone(Ticket) ).
dispatch( depositfailed, depositfailed(Ticket) ).
request( engage, engage(OWNER,STEPTIME) ).
request( moverobot, moverobot(TARGET) ).
%====================================================================================
context(ctxcss_test_deposit, "localhost",  "TCP", "9999").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( test_deposit_coldstorageservice, ctxcss_test_deposit, "it.unibo.test_deposit_coldstorageservice.Test_deposit_coldstorageservice").
  qactor( test_deposit_transporttrolley, ctxcss_test_deposit, "it.unibo.test_deposit_transporttrolley.Test_deposit_transporttrolley").
