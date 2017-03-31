package org.wcong.test.algorithm.basic;

import java.util.List;
import java.util.Map;

/**
 * represent a graph
 * adjacent list
 * Created by wcong on 2017/3/29.
 */
public class Graph {


    public static class Edge {

        public Vertex from;

        public Vertex to;

        public int distance;

    }

    public static class Vertex {
        public int value;

        public int distance;

        public Vertex last;

        @Override
        public boolean equals(Object o) {
            if (o == null || !(o instanceof Vertex)) {
                return false;
            }
            return value == ((Vertex) o).value;
        }
    }

    public List<Edge> edges;

    public Map<Vertex, List<Vertex>> adjacentMap;

    public void addEdge(Edge edge) {
        edges.add(edge);
        adjacentMap.get(edge.from).add(edge.to);
    }
}
