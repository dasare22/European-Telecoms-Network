
object KruskalAlgorithm {
    fun calculateMST(graph: NetGraph): List<Edge> {
        val MSTEdges = mutableListOf<Edge>()
        val sortedEdges = graph.getEdges().sortedBy { it.distance } // kotlin function to sort edges by distance h
        val parent = mutableMapOf<City, City>()

        // Initialize each city to be its own parent in the union find structure
        graph.getCities().forEach { city -> parent[city] = city }

        //  function to find the root of a city
        fun search(city: City): City {  //r, ensure each city is connected to the root
            if (parent[city] != city) {
                parent[city] = search(parent[city]!!) // recursive call path compression
            }
            return parent[city]!!   //non-null assertion,ensures value not null
        }

        // connect 2 cities , union checks nodes that are already connected
        fun union(city1: City, city2: City) {
            val root1 = search(city1)
            val root2 = search(city2)
            if (root1 != root2) {
                parent[root2] = root1
            }
        }

        // add non-cycle edges //i

        for (edge in sortedEdges) {
            if (search(edge.city1) != search(edge.city2)) {
                MSTEdges.add(edge)
                union(edge.city1, edge.city2)
            }
        }

        return MSTEdges
    }
}
