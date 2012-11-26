package de.bht.algo.dijkstra;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import javax.swing.JOptionPane;

import sun.org.mozilla.javascript.ast.ForInLoop;

/**
 * Implementierung des Dijkstra-Algorithmus für Graphen mit Knoten und Kanten
 * 
 * @author Hanna Prinz
 * @author Hala Basali
 * @author Jan Zimmermann
 */
public class Dijkstra {

  private final Graph<Vertex, Edge<Vertex>> graph;
  private final Vertex startVertex;

  private final Vertex[] pred;
  private final ArrayList< Vertex > knoten;

  private final PriorityQueue<Vertex> queue;
  private Vertex endVertex;

  /**
   * Konstruktor für den Dijkstra-Algorithmus
   * 
   * @param graph
   *          der Graph auf den wir den Dijkstra-Algorithmus anwenden möchten
   * @param startpoint
   *          die Id des Knotens von dem wir starten möchten
   */
  public Dijkstra(Graph<Vertex, Edge<Vertex>> graph, int startpoint) {
    this.graph = graph;
    this.startVertex = graph.getVertex(startpoint);

    int numVertices = graph.getVertices().size();
    
    

    // Array initialisieren
    pred = new Vertex[numVertices];
    knoten = new ArrayList< Vertex >();

    // Queue initialsieren
    Comparator<Vertex> comparator = new DijkstraVertexComparator();
    queue = new PriorityQueue<Vertex>(numVertices, comparator);
  }

  /**
   * startet den Dijkstra-Algorithmus
   * 
   * @param endpoint
   *          "alle" wenn die Entfernung zu allen Punkten gesucht ist, sonst die
   *          Id des Endknotens als String
   * @return ein String mit der Benutzerausgabe
   */
  public String startDijkstra(String endpoint) {

    // initialsieren der Standardwerte
    if (!init()) {
      return "";
    }

    // AUSGABE
    StringBuilder returnValue = new StringBuilder();
    returnValue.append("Wir beginnen bei Knoten " + startVertex.getId() + "\n");

    // ist ein endVertex gegeben? Wenn nicht endVertex auf null setzen
    if (!endpoint.equals("alle")) {
      this.endVertex = graph.getVertex(Integer.parseInt(endpoint));
      returnValue.append("Ziel ist Knoten " + endVertex.getId() + "\n");
    } else {
      this.endVertex = null;
      returnValue.append("Ziel ist die kürzeste Verbindung zu allen Knoten. \n");
    }

    StringBuilder ergebnisReihenfolge = new StringBuilder();
    StringBuilder knotenreihe = new StringBuilder();

    while (!queue.isEmpty()) {
      // nimm den Knoten mit der kleinsten Entfernung aus der Queue
      // (Comparable-Implementierung von Vertex siehe Methode
      // graph.Vertex.compareTo(Vertex))
      Vertex currVertex = queue.poll();
      
      // F�r Auswertung speichern
      if ( currVertex != startVertex) {
    	  System.out.println(currVertex + "---");
    	  knoten.add( currVertex );
      }
      
      // wenn der Knoten unerreichbar ist
      if (currVertex.getDist() == Integer.MAX_VALUE) {
        continue;
      }

      // iteriere durch alle Nachbarknoten des aktuellen Knotens...
      for (Vertex neighbor : graph.getNeighbours(currVertex)) {
        // ...die noch nicht abgearbeitet sind
        if (queue.contains(neighbor)) {
          // relaxiere die Kante zwischen den beiden Knoten
          String relaxReturn = relax(currVertex, neighbor);
          returnValue.append(relaxReturn + "\n");
        }
      }

      // wenn es kein EndVertex gibt, gib alle Distanzen aus
      if (endVertex == null) {
        ergebnisReihenfolge.append(" Distanz " + startVertex.getId() + " \u2192 " + currVertex.getId() + ": "
            + currVertex.getDist() + " über Knoten " + knotenreihe.toString() + "\n");
      }

      // wenn es einen EndVertex gibt und dieser der aktuelle Vertex ist, kann
      // hier abgebrochen werden
      if (endVertex != null && currVertex.getId() == endVertex.getId()) {
        ergebnisReihenfolge.append(" Distanz " + startVertex.getId() + "\u2192" + currVertex.getId() + ": "
            + currVertex.getDist() + " über Knoten " + knotenreihe.toString() + "\n");
        break;
      }

      knotenreihe.append(currVertex.getId() + ", ");
    }

    // TODO funktioniert der Algorithmus richtig?
    
    for ( Vertex v : knoten ) {
    	System.out.println("K " + pathfinder( v ));
	}


    // letzen "-->" abscheiden und den String zurück geben
    returnValue.append(ergebnisReihenfolge.substring(0, ergebnisReihenfolge.length() - 1));
    return returnValue.append("\n").toString();
  }

  /**
   * initialisieren der Variablen (alle Entfernungen auf maximal setzen, ...)
   * 
   * @return false wenn der graph negative Kantengewichte hat, true wenn nicht
   */
  private boolean init() {
    for (Object i : graph.getEdgeMap().values()) {
      Integer integer = (Integer) i;

      // wenn es nagative Werte gibt, gib eine Warnung aus
      if (Math.abs(integer) != integer) {
        JOptionPane.showMessageDialog(null,
            "Dijkstra kann auf diesen Graph nicht angewendet werden. Der Graph enthält negative Kantengewichte.",
            "Fehler", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }

    startVertex.setDist(0);
    queue.add(startVertex);

    for (Vertex v : graph.getVertices()) {
      if (v.getId() != startVertex.getId()) {

        v.setDist(Integer.MAX_VALUE);
        pred[v.getId()] = null;

        queue.add(v);
      }
    }

    startVertex.setDist(0);
    return true;
  }

  /**
   * Prüft ob eine Verbesserung der Entfernung zum Nachbarknoten
   * <code>neighbor</code> möglich ist
   * 
   * @param currVertex
   *          der aktuelle Knoten deren Kanten wir betrachten
   * @param neighbor
   *          der Nachbarknoten von <code>currVertex</code> den wir prüfen
   *          möchten
   * @return ein String der in der Ausgabe erscheinen soll
   */
  private String relax(Vertex currVertex, Vertex neighbor) {
    StringBuilder returnValue = new StringBuilder("");

    // Kantengewicht der Kante zwischen dem aktuellen Knoten und seinem Nachbarn
    Object o = graph.getEdgeMap().get(currVertex.getId(), neighbor.getId());
    if (o == null) {
      o = graph.getEdgeMap().get(neighbor.getId(), currVertex.getId());
    }
    int weight = (int) o;

    // gesamte alternative Distanz zum Nachbarknoten
    int alternativeDist = currVertex.getDist() + weight;

    // wenn alternative Distanz kürzer als die aktuelle ist (Verbesserung)
    if (alternativeDist < neighbor.getDist()) {

      // textarea-Ausgabe
      String oldDistance = "" + neighbor.getDist();
      if (neighbor.getDist() == Integer.MAX_VALUE) {
        oldDistance = "\u221E";
      }
      returnValue.append("Kante zwischen " + currVertex.getId() + " und " + neighbor.getId() + " wurde verbessert ("
          + oldDistance + " \u21D2 " + alternativeDist + ")");

      // neue Distanz setzen
      neighbor.setDist(alternativeDist);
      // neuen Vorgänger setzen
      pred[neighbor.getId()] = currVertex;
    }

    return returnValue.toString();
  }
  
  private String pathfinder(Vertex v) {
	  String path = "";
	  // Vorg�nger des Knotens
	  Vertex tmp = v;
	  while (!( tmp == startVertex ) ) {
		  tmp = findPred( tmp );
		  path.concat( tmp.getId() + "-->" );
	  }
	  return path;
  }

private Vertex findPred( Vertex v ) {
	Vertex vorgaenger;
	vorgaenger = pred[v.getId()];
	/*
	 *  hier der Fehler: Vorg�nger ist NULL
	 *  weil letzter Wert nicht �berpr�ft wird
	 */
	System.out.println("V " + v);
	System.out.println("VOR " + vorgaenger);
	return vorgaenger;
}
}
