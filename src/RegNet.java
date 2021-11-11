import java.util.ArrayList;
import java.util.Arrays;

public class RegNet
{
    //creates a regional network
    //G: the original graph
    //max: the budget
    public static Graph run(Graph G, int max) {
        G = kruskal(G);


        //Cut and expand the MST
        System.out.println("Current weight is: " + G.totalWeight());
        if (G.totalWeight() > max) {
            System.out.println("Weight too large!");
            //Keep removing edges until good
            G = cut(G, max);
        } if (G.totalWeight() < max) {
            System.out.println("Possible room for more edges");
            //Keep adding edges until good
            G = add(G, max);
        }

        return G;
    }

    //BUILDS MST
    private static Graph kruskal(Graph G) {
        UnionFind finder = new UnionFind(G.V());
        ArrayList<Edge> q = G.sortedEdges();
        System.out.println("Size: " + q.size());
        if (q.size() == 0) {
            return G;
        }

        Graph tree = new Graph(G.V());
        tree.setCodes(G.getCodes());

        while (tree.E() < tree.V() - 1) {
            int u = q.get(0).ui();
            int v = q.get(0).vi();
            System.out.println("U: " + u + ", V: " + v);
            if (finder.find(u) != finder.find(v)) {
                tree.addEdge(q.get(0));
                finder.union(u, v);
            }
            System.out.println("Tree edge size: " + tree.E());
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
            int u = edges.get(i).ui();
            int v = edges.get(i).vi();

            //If either adjacency list size is 1, then that is the only vertex to become stray
            if (G.adj(u).size() == 1 || G.adj(v).size() == 1) {
                G.removeEdge(edges.get(i));
                G = G.connGraph();

                if (G.totalWeight() <= max) {
                    break;
                }
            }
        }
        return G;
    }

    //Add edges because it is below max weight
    private static Graph add(Graph G, int max) {
        String[] vertices = G.getCodes();
        ArrayList<Edge> addQ = new ArrayList<>();


        //Build edge queue
        for (int i = 0; i < vertices.length; i++) {
            //Graph temp = G.subgraph(Arrays.copyOfRange(G.getCodes(), i, G.getCodes().length));
            G.BFS(addQ, i);
        }

        System.out.println("Testing Possible Edges");
        for (int i = 0; i < addQ.size(); i++) {
            System.out.println(addQ.get(i).u + " to " + addQ.get(i).v + " has " + addQ.get(i).w + " stops");
        }

        System.out.println("Done with that");
        return G;
    }
}