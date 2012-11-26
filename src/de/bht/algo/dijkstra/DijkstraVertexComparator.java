package de.bht.algo.dijkstra;

import graph.Vertex;

import java.util.Comparator;

/**
 * Diese Klasse vergleicht zwei Knoten nach ihrer Distanz
 * 
 * @author Hanna
 * 
 */
public class DijkstraVertexComparator implements Comparator<Vertex> {

  @Override
  public int compare(Vertex one, Vertex o) {
    if (one.getDist() < o.getDist()) {
      return -1;
    } else if (one.getDist() == o.getDist()) {
      return 0;
    } else {
      return 1;
    }
  }

}
