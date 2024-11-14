//  Domain classes

data class City(val name: String) // name of cities

data class Edge(val city1: City, val city2: City, val distance: Int) // cities connection and distance between them

//Network graph

class NetGraph {
    private val cities = mutableSetOf<City>()
    private val edges = mutableListOf<Edge>()

    fun add_a_City(city: City) {  //adding a city to graph
        cities.add(city)
    }
    fun add_an_Edge(edge: Edge) { // adding edge to graph
        edges.add(edge)   // distance
        cities.add(edge.city1)
        cities.add(edge.city2)
    }

    fun getEdges(): List<Edge> {
        return edges //get list of edges
    }

    fun getCities(): Set<City>  {
        return cities  // get list of cities
    }
    fun calculateMST(): List<Edge> {
        return KruskalAlgorithm.calculateMST(this)
    }

    fun clear() {
        edges.clear()
    }
}
