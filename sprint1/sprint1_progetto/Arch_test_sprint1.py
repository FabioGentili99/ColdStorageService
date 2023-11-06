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
with Diagram('test_sprint1Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxtestsprint1', graph_attr=nodeattr):
          test_coldstorageservice_sprint1=Custom('test_coldstorageservice_sprint1','./qakicons/symActorSmall.png')
          test_coldroommanager=Custom('test_coldroommanager','./qakicons/symActorSmall.png')
          test_ticketmanager=Custom('test_ticketmanager','./qakicons/symActorSmall.png')
     test_coldroommanager >> Edge(color='magenta', style='solid', decorate='true', label='<generateticket &nbsp; >',  fontcolor='magenta') >> test_ticketmanager
diag
