package org.wcong.test.algorithm.graph;

import java.util.List;

/**
 * Created by wcong on 2017/2/23.
 */
public class Graph {

    private int vertices;

    private int edge;

    private List<Vertices>[] adjacencyList;


    public int getVertices() {
        return vertices;
    }

    public void setVertices(int vertices) {
        this.vertices = vertices;
    }

    public int getEdge() {
        return edge;
    }

    public void setEdge(int edge) {
        this.edge = edge;
    }

    public List<Vertices>[] getAdjacencyList() {
        return adjacencyList;
    }

    public void setAdjacencyList(List<Vertices>[] adjacencyList) {
        this.adjacencyList = adjacencyList;
    }
}
