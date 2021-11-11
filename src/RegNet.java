import java.util.ArrayList;
import java.util.Arrays;

public class RegNet
{
    //creates a regional network
    //G: the original graph
    //max: the budget
    public static Graph run(Graph G, int max) {
        System.out.println("MAX WEIGHT: " + max);
        //System.out.println(G.toString());
        Graph original = G;
        G = kruskal(G);

       // System.out.println(G.toString());


        //Cut and expand the MST
        System.out.println("Current weight is: " + G.totalWeight());
        if (G.totalWeight() > max) {
            System.out.println("Weight too large!");
            //Keep removing edges until good
            G = cut(G, max);
            System.out.println("Weight after cut: " + G.totalWeight());
            System.out.println("Size is: " + G.getCodes().length);
        } if (G.totalWeight() < max) {
            System.out.println("Is there possible room for more edges?");
            //Keep adding edges until good
            G = add(original, G, max);
        }

        return G;
    }

    //BUILDS MST
    private static Graph kruskal(Graph G) {
        UnionFind finder = new UnionFind(G.V());
        ArrayList<Edge> q = G.sortedEdges();
        //System.out.println("Size: " + q.size());
        if (q.size() == 0) {
            return G;
        }

        Graph tree = new Graph(G.V());
        tree.setCodes(G.getCodes());

        while (tree.E() < tree.V() - 1) {
            int u = q.get(0).ui();
            int v = q.get(0).vi();
            //System.out.println("U: " + u + ", V: " + v);
            if (finder.find(u) != finder.find(v)) {
                tree.addEdge(q.get(0));
                finder.union(u, v);
            }
            //System.out.println("Tree edge size: " + tree.E());
            q.remove(0);
        }
        return tree;
    }

    //Remove edges because it is above the max weight
    private static Graph cut(Graph G, int max) {
        ArrayList<Edge> edges = G.sortedEdges();
        if (edges.size() == 0) {
            return G;
        }

        for (int i = edges.size() - 1; i >= 0; i--) {
            //Check to see if removing edge would disconnect graph
            String u = edges.get(i).u;
            String v = edges.get(i).v;

            //If either adjacency list size is 1, then that is the only vertex to become stray
            if (G.deg(u) == 1 || G.deg(v) == 1) {
                G.removeEdge(edges.get(i));
                G = G.connGraph();

                if (G.totalWeight() <= max) {
                    break;
                }
                edges = G.sortedEdges();
                i = edges.size();
            }
        }
        return G;
    }

    //Add edges because it is below max weight
    private static Graph add(Graph original, Graph G, int max) {
        String[] vertices = G.getCodes();
        ArrayList<Edge> addQ = new ArrayList<>();


        //Build edge queue
        for (int i = 0; i < vertices.length; i++) {
            G.BFS(addQ, i);
        }


        //Sort List
        addQ.sort(new EdgeSort());
//
//        System.out.println("Testing Possible Edges");
//        for (int i = 0; i < addQ.size(); i++) {
//            System.out.println(addQ.get(i).u + " to " + addQ.get(i).v + " has " + addQ.get(i).w + " stops");
//        }

        int maxStop = addQ.get(addQ.size() - 1).w;
        while (G.totalWeight() < max && maxStop > 0) {
            ArrayList<Edge> dPriority = new ArrayList<>();
            for (int i = addQ.size() - 1; i >= 0; i--) {
                if (addQ.get(i).w == maxStop) {
                    int u = G.index(addQ.get(i).u);
                    int v = G.index(addQ.get(i).v);
                    dPriority.add(new Edge(addQ.get(i).u, addQ.get(i).v, original.getEdgeWeight(u, v)));
                } else {
                    dPriority.sort(new EdgeSort());
                    for (int j = 0; j < dPriority.size() - 1; j++) {
                        if (G.totalWeight() + dPriority.get(j).w <= max) {
                            G.addEdge(dPriority.get(j));
                        }
                    }
                    i++;
                    maxStop--;
                    dPriority = new ArrayList<>();
                }
            }
        }
        return G;
    }
}