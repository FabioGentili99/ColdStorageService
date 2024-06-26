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
          planexec=Custom('planexec','./qakicons/symActorSmall.png')
          robotpos=Custom('robotpos','./qakicons/symActorSmall.png')
          basicrobot=Custom('basicrobot','./qakicons/symActorSmall.png')
     with Cluster('ctxraspberrypi', graph_attr=nodeattr):
          warningdevice=Custom('warningdevice(ext)','./qakicons/externalQActor.png')
          alarmdevice=Custom('alarmdevice(ext)','./qakicons/externalQActor.png')
     transporttrolley >> Edge(color='magenta', style='solid', decorate='true', label='<moverobot<font color="darkgreen"> moverobotdone moverobotfailed</font> &nbsp; >',  fontcolor='magenta') >> robotpos
     robotpos >> Edge(color='magenta', style='solid', decorate='true', label='<doplan<font color="darkgreen"> doplandone doplanfailed</font> &nbsp; >',  fontcolor='magenta') >> planexec
     planexec >> Edge(color='magenta', style='solid', decorate='true', label='<step<font color="darkgreen"> stepdone stepfailed</font> &nbsp; >',  fontcolor='magenta') >> basicrobot
     coldroommanager >> Edge(color='magenta', style='solid', decorate='true', label='<generateticket &nbsp; >',  fontcolor='magenta') >> ticketmanager
     basicrobot >> Edge(color='blue', style='solid',  label='<robotready &nbsp; >',  fontcolor='blue') >> transporttrolley
     transporttrolley >> Edge(color='blue', style='solid',  label='<home &nbsp; moving &nbsp; stopped &nbsp; >',  fontcolor='blue') >> warningdevice
     planexec >> Edge(color='blue', style='solid',  label='<nextmove &nbsp; nomoremove &nbsp; >',  fontcolor='blue') >> planexec
     transporttrolley >> Edge(color='blue', style='solid',  label='<depositdone &nbsp; >',  fontcolor='blue') >> coldroommanager
     transporttrolley >> Edge(color='blue', style='solid',  label='<stopplan &nbsp; continueplan &nbsp; >',  fontcolor='blue') >> planexec
     transporttrolley >> Edge(color='blue', style='solid',  label='<setrobotstate &nbsp; setdirection &nbsp; >',  fontcolor='blue') >> robotpos
     ticketmanager >> Edge(color='blue', style='solid',  label='<expiredticket &nbsp; >',  fontcolor='blue') >> coldroommanager
diag
