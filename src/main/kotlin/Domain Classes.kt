//  Domain classes

// Represents a City in the graph
data class City(val name: String)

// Represents an edge between two cities and the distance between them
data class Edge(val city1: City, val city2: City, val distance: Int)

// Network graph to store cities and edges
class NetGraph {
    private val cities = mutableSetOf<City>() // A set to store unique cities
    private val edges = mutableListOf<Edge>() // A list to store edges (connections between cities)

    // Add an edge between two cities to the graph
    fun addEdge(edge: Edge) {
        edges.add(edge)   // distance
        cities.add(edge.city1)
        cities.add(edge.city2)
    }

    // Get all the edges in the graph
    fun getEdges(): List<Edge> {
        return edges
    }

    // Get all the cities in the graph
    fun getCities(): Set<City>  {
        return cities  // get list of cities
    }

    // Calculate the Minimum Spanning Tree (MST) using Kruskal's Algorithm
    fun calculateMST(): List<Edge> {
        return KruskalAlgorithm.calculateMST(this)
    }

    // Clear all the edges in the graph
    fun clear() {
        edges.clear()
    }
}
