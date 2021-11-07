import java.util.ArrayList;

public class RegNet
{
    //creates a regional network
    //G: the original graph
    //max: the budget
    public static Graph run(Graph G, int max) {
        ArrayList<Edge> q = G.sortedEdges();

        return G;
    }


    private Graph kruskal(Graph G) {
        UnionFind finder = new UnionFind(G.V());
        DistQueue q = new DistQueue(G.V());

        for (int i = 0; i < G.E(); i++) {
            q.insert()
        }

        return G;
    }


}