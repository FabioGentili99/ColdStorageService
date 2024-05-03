### conda install diagrams
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
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxcoldstorageservice', graph_attr=nodeattr):
          coldstorageservice=Custom('coldstorageservice','./qakicons/symActorSmall.png')
          coldroommanager=Custom('coldroommanager','./qakicons/symActorSmall.png')
          ticketmanager=Custom('ticketmanager','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     transporttrolley >> Edge(color='magenta', style='solid', decorate='true', label='<engage &nbsp; moverobot &nbsp; >',  fontcolor='magenta') >> basicrobot
     coldroommanager >> Edge(color='magenta', style='solid', decorate='true', label='<generateticket &nbsp; >',  fontcolor='magenta') >> ticketmanager
     transporttrolley >> Edge(color='blue', style='solid',  label='<depositfailed &nbsp; depositdone &nbsp; >',  fontcolor='blue') >> coldroommanager
     ticketmanager >> Edge(color='blue', style='solid',  label='<expiredticket &nbsp; >',  fontcolor='blue') >> coldroommanager
diag
