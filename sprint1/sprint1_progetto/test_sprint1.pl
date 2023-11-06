%====================================================================================
% test_sprint1 description   
%====================================================================================
request( storerequest, storerequest(TruckLoad) ).
request( verifyticket, verifyticket(TruckTicket) ).
request( loaddone, loaddone(TruckTicket) ).
request( generateticket, generateticket(TruckLoad) ).
%====================================================================================
context(ctxtestsprint1, "localhost",  "TCP", "9999").
 qactor( test_coldstorageservice_sprint1, ctxtestsprint1, "it.unibo.test_coldstorageservice_sprint1.Test_coldstorageservice_sprint1").
  qactor( test_coldroommanager, ctxtestsprint1, "it.unibo.test_coldroommanager.Test_coldroommanager").
  qactor( test_ticketmanager, ctxtestsprint1, "it.unibo.test_ticketmanager.Test_ticketmanager").
