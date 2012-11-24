package de.bht.algo.dijkstra;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.PriorityQueue;

public class Dijkstra {

  private final Graph<Vertex, Edge<Vertex>> graph;
  private Vertex startVertex;

  private final String[] col;
  private final int[] dist;
  private final Vertex[] pred;

  private final String WEISS = "weiss";
  private final String GRAU = "grau";
  private final String SCHWARZ = "schwarz";

  private final PriorityQueue<Vertex> queue;

  public Dijkstra(Graph<Vertex, Edge<Vertex>> graph, int startpoint) {
    this.graph = graph;
    this.startVertex = graph.getVertex(startpoint);
    int numVertices = graph.getVertices().size();
    // Arrays initialisieren
    col = new String[numVertices];
    dist = new int[numVertices];
    pred = new Vertex[numVertices];
    queue = new PriorityQueue<>();
  }

  public Graph<Vertex, Edge<Vertex>> getGraph() {
    return graph;
  }

  public void startDijkstra() {
    init();
    while (!queue.isEmpty()) {
      Vertex currVertex = queue.poll();
      System.out.println("Neuer CurrentVertex ist " + currVertex.getId());

      for (Vertex neighbor : graph.getNeighbours(currVertex)) {
        if (queue.contains(neighbor)) {
          relax(currVertex, neighbor);
        }
      }
    }

    // TODO funktioniert das richtig?
    // TODO ausgeben, welche Reihenfolge der Graph jetzt hat
  }

  private void init() {
    for (Vertex v : graph.getVertices()) {
      col[v.getId()] = WEISS;
      dist[v.getId()] = Integer.MAX_VALUE;
      pred[v.getId()] = null;
      queue.add(v);
    }
    dist[startVertex.getId()] = 0;
  }

  private void relax(Vertex currVertex, Vertex neighbor) {
    int weight = (int) graph.getEdgeMap().get(currVertex.getId(), neighbor.getId());

    System.out.println("Weight von " + currVertex.getId() + " und " + neighbor.getId() + " ist " + weight);

    int alternativeDist = currVertex.getDist() + weight;

    if (alternativeDist < neighbor.getDist()) {
      neighbor.setDist(alternativeDist);
      pred[neighbor.getId()] = currVertex;
    }
  }

  public void setStartVertex(int startpoint) {
    this.startVertex = graph.getVertex(startpoint);
  }

}
