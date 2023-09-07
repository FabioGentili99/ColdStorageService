from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
with Diagram('coldstorageserviceArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxcoldstorageservice', graph_attr=nodeattr):
          coldstorageservice=Custom('coldstorageservice','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxraspi', graph_attr=nodeattr):
          alarmdevice=Custom('alarmdevice','./qakicons/symActorSmall.png')
          warningdevice=Custom('warningdevice','./qakicons/symActorSmall.png')
     with Cluster('ctxaccessgui', graph_attr=nodeattr):
          testgui=Custom('testgui','./qakicons/symActorSmall.png')
     testgui >> Edge(color='magenta', style='solid', xlabel='storerequest', fontcolor='magenta') >> coldstorageservice
     testgui >> Edge(color='magenta', style='solid', xlabel='verifyticket', fontcolor='magenta') >> coldstorageservice
     coldstorageservice >> Edge(color='blue', style='solid', xlabel='doJob', fontcolor='blue') >> transporttrolley
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='engage', fontcolor='magenta') >> basicrobot
     transporttrolley >> Edge(color='blue', style='solid', xlabel='updateled', fontcolor='blue') >> warningdevice
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='moverobot', fontcolor='magenta') >> basicrobot
     transporttrolley >> Edge(color='blue', style='solid', xlabel='stop', fontcolor='blue') >> basicrobot
     alarmdevice >> Edge(color='blue', style='solid', xlabel='stop', fontcolor='blue') >> transporttrolley
     alarmdevice >> Edge(color='blue', style='solid', xlabel='resume', fontcolor='blue') >> transporttrolley
diag
