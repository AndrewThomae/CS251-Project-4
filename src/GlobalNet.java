import java.util.ArrayList;

public class GlobalNet
{
    //creates a global network 
    //O : the original graph
    //regions: the regional graphs
    public static Graph run(Graph O, Graph[] regions) {
        //Construct the graph
        Graph G = new Graph(O.V());
        G.setCodes(O.getCodes());

        for (Graph region : regions) {
            for (Edge edge : region.edges()) {
                G.addEdge(edge);
            }
        }
//        System.out.println("Check G");
//        System.out.println("Graph: " + G.toString());

        //Connect regions
        for (int i = 0; i < regions.length; i++) {
            for (int j = i + 1; j < regions.length; j++) {
                //biggie[0] is distance array
                //biggie[1] is previous position array
                int[][] biggie = regDijkstra(O, regions[i], O.index(regions[i].getCode(0)));
                int bestPort = -1;
                for (String code : regions[j].getCodes()) {
                    int pos = G.index(code);
                    if (bestPort == -1) {
                        bestPort = pos;
                        continue;
                    } if (biggie[0][pos] < biggie[0][bestPort]) {
                        bestPort = pos;
                    }
                }

                //add edges
                while (true) {
                    Edge adding = O.getEdge(bestPort ,biggie[1][bestPort]);
                    G.addEdge(adding);
                    //System.out.println("Successfully Added: (" + adding.u + ", " + adding.v + ", " + adding.w + ")");
                    bestPort = biggie[1][bestPort];
                    if (biggie[1][bestPort] == -1) {
                        break;
                    }
                }
            }
        }
        G = G.connGraph();
        return G;
    }


    private static int[][] regDijkstra(Graph G, Graph curReg, int s) {
        int[][] combo = new int[2][G.getCodes().length];
        int[] dist = new int[G.getCodes().length];
        int[] prev = new int[G.getCodes().length];
        DistQueue q = new DistQueue(G.getCodes().length);

        dist[s] = 0;
        for (int i = 0; i < G.getCodes().length; i++) {
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
                prev[u] = -1;
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
    
    
    