package Aois.Romanoff;

import lombok.Data;

import java.util.*;

@Data
public class Graph {
    HashMap<Integer, Node> nodes;
    List<Integer> neightbors;
    String graphCode;
    HashMap<String,Integer> covered;
    public void setCovered(HashMap<String,Integer> covered){
        for(var node:nodes.values()){
            if(covered.containsKey(node.getGreyCode())){
                this.covered.put(node.getGreyCode(),node.getData());
            }
        }
    }
    public void setCovered(Set<Integer> covered){
        for(var node:nodes.values()){
            if(covered.contains(node.getData())){
                this.covered.put(node.getGreyCode(),node.getData());
            }
        }
    }
    public HashMap<String,Integer> setGetCovered(Set<Integer> covered){
        HashMap<String,Integer> coveredMap=new HashMap<>();
        for(var node:nodes.values()){
            if(covered.contains(node.getData())){
                this.covered.put(node.getGreyCode(),node.getData());
                coveredMap.put(node.getGreyCode(),node.getData());
            }
        }
        return coveredMap;
    }
    public void setCoveredStrings(Set<String> covered){
        if(covered.isEmpty()){
            this.covered=new HashMap<>();
        }
        for(var node:nodes.values()){
            if(!covered.contains(node.getGreyCode())){
                this.covered.remove(node.getGreyCode(),node.getData());
            }
        }
    }
    public List<String> codesRectangle(Set<Integer> rectangle){
        List<String> code=new ArrayList<>();
        for(var node:nodes.values()){
            if(rectangle.contains(node.getData())){
                code.add(node.getGreyCode());
            }
        }
        return code;
    }
    public Graph() {
        nodes = new HashMap<>();
        neightbors = new ArrayList<>();
        covered=new HashMap<>();
    }

    public void adjastNodes(Set<String> validNodes) {
        for (var node : nodes.values()) {
            if (!validNodes.contains(node.getGreyCode())) {
                int pos=node.getData();
                node = new Node(-10, new String(""));
                nodes.put(pos, node);
            } else {
                int[] directions = node.getDirections();
                for (int j = 0; j < directions.length; j++) {
                    if (nodes.get(directions[j]) != null) {
                        if (!validNodes.contains(nodes.get(directions[j]).getGreyCode())) {
                            directions[j] = -10;
                        }
                    }
                }
                node.setDirections(directions);
                nodes.put(node.getData(), node);
            }
        }
    }

    public Graph(Graph graph) {
        this.nodes = new HashMap<>();
        for(var key:graph.nodes.keySet()){
            this.nodes.put(key,new Node( graph.nodes.get(key)));
        }
        this.neightbors = new ArrayList<>(graph.neightbors);
        this.graphCode = new String(graph.graphCode);
        if(graph.covered!=null)
        this.covered = new HashMap<>(graph.covered);
        else
            this.covered = new HashMap<>();
    }

    public Set<String> greyCodes() {
        Set<String> result = new HashSet<>();
        for (var node : nodes.values()) {
            result.add(node.getGreyCode());
        }
        return result;
    }

    public static List<List<Integer>> findAllCycles(List<Graph> graphs) {
        List<List<Integer>> allCycles = new ArrayList<>();
        Set<String> cycleKeys = new HashSet<>();
        boolean[] visited = new boolean[graphs.size()];

        for (int i = 0; i < graphs.size(); i++) {
            List<Integer> path = new ArrayList<>();
            path.add(i);
            visited[i] = true;
            dfsCycle(graphs, i, i, -1, visited, path, allCycles, cycleKeys);
            visited[i] = false;
        }

        return allCycles.stream()
                .filter(cycle -> cycle.size() >= 3)
                .toList();
    }

    private static void dfsCycle(List<Graph> graphs, int start, int current, int parent,
                                 boolean[] visited, List<Integer> path,
                                 List<List<Integer>> allCycles, Set<String> cycleKeys) {
        for (int neighbor : graphs.get(current).neightbors) {
            if (neighbor == start && path.size() >= 3) {
                List<Integer> cycle = new ArrayList<>(path);
                cycle.add(neighbor);

                String cycleKey = getCycleKey(cycle);
                if (!cycleKeys.contains(cycleKey)) {
                    cycleKeys.add(cycleKey);
                    allCycles.add(cycle);
                }
            }
            else if (!visited[neighbor]) {
                visited[neighbor] = true;
                path.add(neighbor);
                dfsCycle(graphs, start, neighbor, current, visited, path, allCycles, cycleKeys);
                visited[neighbor] = false;
                path.remove(path.size() - 1);
            }
        }
    }

    private static String getCycleKey(List<Integer> cycle) {
        Set<String> edges = new TreeSet<>();
        int n = cycle.size();
        for (int i = 0; i < n - 1; i++) {
            int u = cycle.get(i);
            int v = cycle.get(i + 1);
            String edge = Math.min(u, v) + "-" + Math.max(u, v);
            edges.add(edge);
        }
        return String.join(",", edges);
    }

    public void createGraph(int columns, int rows) {
        HashMap<Integer, Node> graph = new HashMap<>(nodes);
        int[] dx = { -1, 0, 1, -1, 1, -1, 0, 1 };
        int[] dy = { -1, -1, -1, 0, 0, 1, 1, 1 };
        String[] directions = {
                "leftUp", "up", "rightUp",
                "left", "right",
                "leftDown", "down", "rightDown"
        };

        for (int pos : graph.keySet()) {
            Node currentNode = graph.get(pos);
            int row = pos / 10;
            int col = pos % 10;

            for (int i = 0; i < 8; i++) {
                int newRow = (row + dy[i] + rows) % rows;
                int newCol = (col + dx[i] + columns) % columns;
                if (newRow < 0) {
                    newRow = 3;
                } else if (newRow > 3) {
                    newRow = 0;
                }
                if (newCol < 0) {
                    newCol = 3;
                } else if (newCol > 3) {
                    newCol = 0;
                }
                int neighborPos = newRow * 10 + newCol;

                if (graph.containsKey(neighborPos)) {
                    int neighborData = graph.get(neighborPos).getData();

                    switch (directions[i]) {
                        case "left": currentNode.setLeft(neighborData); break;
                        case "right": currentNode.setRight(neighborData); break;
                        case "up": currentNode.setUp(neighborData); break;
                        case "down": currentNode.setDown(neighborData); break;
                        case "leftUp": currentNode.setLeftUp(neighborData); break;
                        case "rightUp": currentNode.setRightUp(neighborData); break;
                        case "leftDown": currentNode.setLeftDown(neighborData); break;
                        case "rightDown": currentNode.setRightDown(neighborData); break;
                    }
                }
            }
        }

        nodes = graph;
    }
}