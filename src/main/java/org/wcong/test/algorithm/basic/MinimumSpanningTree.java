package org.wcong.test.algorithm.basic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * test for graph
 * Created by wcong on 2017/3/29.
 */
public class MinimumSpanningTree {


    public List<Graph.Edge> kruskal(Graph graph) {
        List<Graph.Edge> mini = new ArrayList<>();
        Set<Graph.Vertex> vertexSet = new HashSet<>();
        Queue<Graph.Edge> priorityQueue = new PriorityQueue<>(graph.edges.size(), new Comparator<Graph.Edge>() {
            @Override
            public int compare(Graph.Edge o1, Graph.Edge o2) {
                return o2.distance - o1.distance;
            }
        });
        priorityQueue.addAll(graph.edges);
        while (!priorityQueue.isEmpty()) {
            Graph.Edge edge = priorityQueue.poll();
            if (!(vertexSet.contains(edge.from) && vertexSet.contains(edge.to))) {
                mini.add(edge);
                vertexSet.add(edge.from);
                vertexSet.add(edge.to);
            }
        }
        return mini;
    }

    public List<Graph.Edge> prim(Graph graph) {
        PriorityQueue<Graph.Vertex> priorityQueue = new PriorityQueue<>(graph.adjacentMap.keySet().size(), new Comparator<Graph.Vertex>() {
            @Override
            public int compare(Graph.Vertex o1, Graph.Vertex o2) {
                return o2.distance - o1.distance;
            }
        });
        for (Graph.Vertex vertex : graph.adjacentMap.keySet()) {
            vertex.distance = Integer.MAX_VALUE;
            priorityQueue.add(vertex);
        }
        Graph.Vertex first = priorityQueue.poll();
        first.distance = 0;
        priorityQueue.add(first);
        while (!priorityQueue.isEmpty()) {
            Graph.Vertex min = priorityQueue.poll();

        }
        return null;
    }
}
