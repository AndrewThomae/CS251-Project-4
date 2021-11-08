import java.util.ArrayList;

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
            while (G.totalWeight() > max) {
                G = cut(G);
            }
        } if (G.totalWeight() < max) {
            System.out.println("Possible room for more edges");
            //Keep adding edges until good
            while (G.totalWeight() < max) {
                G = add(G);
            }
        }

        return G;
    }

    //BUILDS MST
    private static Graph kruskal(Graph G) {
        UnionFind finder = new UnionFind(G.V());
        ArrayList<Edge> q = G.sortedEdges();

        Graph tree = new Graph(G.V());

        while (tree.E() < tree.V() - 1) {
            int u = q.get(0).ui();
            int v = q.get(0).vi();
            if (finder.find(u) != finder.find(v)) {
                tree.addEdge(u, v, q.get(0).w);
                finder.union(u, v);
            }
            q.remove(0);
        }
        return tree;
    }

    //Remove edges because it is above the max weight
    private static Graph cut(Graph G) {

        return G;
    }

    //Add edges because it is below max weight
    private static Graph add(Graph G) {

        return G;
    }
}