import java.util.ArrayList;

public class GlobalNet
{
    //creates a global network 
    //O : the original graph
    //regions: the regional graphs
    public static Graph run(Graph O, Graph[] regions) {
        //Construct the graph


        //Connect subgraphs
        for (int i = 0; i < regions.length - 1; i++) {
            for (int j = i + 1; j < regions.length - 1; j++) {
                //biggie[0] is distance array
                //biggie[1] is previous position array
                int[][] biggie = regDijkstra(O, regions[i], O.index(regions[i].getCode(0)));
                int bestPort = Integer.MAX_VALUE;
                for (int k = 0; k < regions[j].V() - 1; k++) {
                    if (biggie[0][k] < bestPort) {
                        bestPort = k;
                    }
                }

                //add edges
                while (true) {
                    break;
                    //O.addEdge(regions[j].getCode(bestPort), regions[j].getCode(biggie[1][bestPort]));
                }
            }
        }
        return O;
    }


    private static int[][] regDijkstra(Graph G, Graph curReg, int s) {
        int[][] combo = new int[2][G.getCodes().length];
        int[] dist = new int[G.getCodes().length];
        int[] prev = new int[G.getCodes().length];
        DistQueue q = new DistQueue(G.getCodes().length);

        dist[s] = 0;
        for (int i = 0; i < G.getCodes().length - 1; i++) {
            if (i != s) {
                dist[i] = Integer.MAX_VALUE - 10000000;
            }
            prev[i] = -1;
            q.insert(i, dist[i]);
        }

        while (!q.isEmpty()) {
            int u = q.delMin();
            boolean inRegion = false;
            //Check if this vertex is in the region
            for (String port : curReg.getCodes()) {
                if (port.equals(G.getCode(u))) {
                    inRegion = true;
                }
            }
            //If its in the region the distance is 0
            if (inRegion) {
                dist[u] = 0;
            }

            ArrayList<Integer> adj = G.adj(u);


            for (int vertex : adj) {
                if (q.inQueue(vertex)) {
                    int d = dist[u] + G.getEdgeWeight(u, vertex);
                    if (d < dist[vertex]) {
                        dist[vertex] = d;
                        prev[vertex] = u;
                        q.set(vertex, d);
                    }
                }
            }
        }


        combo[0] = dist;
        combo[1] = prev;
        return combo;
    }
}
    
    
    