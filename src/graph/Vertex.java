package graph;

/**
 * Eine Klasse, die Knoten eines Graphen repr�sentiert
 * 
 * @author ripphausen
 * @version 1.0
 * @param <T>
 */
public class Vertex implements Comparable<Vertex> {

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Vertex other = (Vertex) obj;
    if (id != other.id) {
      return false;
    }
    return true;
  }

  private final int id;
  /**
   * Added for Dijkstra
   */
  private int dist;

  public Vertex(int id) {
    this.id = id;

    // Added for Dijkstra
    this.dist = Integer.MAX_VALUE;
  }

  public int getId() {
    return id;
  }

  /**
   * Added for Dijkstra
   */
  public int getDist() {
    return dist;
  }

  /**
   * Added for Dijkstra
   */
  public void setDist(int dist) {
    this.dist = dist;
  }

  @Override
  public String toString() {
    return new Integer(id).toString();
  }

  @Override
  /**
   * Added for Dijkstra
   */
  public int compareTo(Vertex o) {
    if (dist < o.getDist()) {
      return -1;
    } else if (o.getDist() == dist) {
      return 0;
    } else {
      return 1;
    }
  }
}