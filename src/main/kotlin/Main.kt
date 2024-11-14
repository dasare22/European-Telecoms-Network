import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File

class DeskApp : Application() {
    private val graph = NetGraph()
    private val outputArea = TextArea()

    override fun start(stage: Stage) {
        val actionButton = Button("Insert File")
        val viewButton = Button("View MST")
        outputArea.isEditable = false

        // Process file
        actionButton.setOnAction {
            val file = FileChooser().showOpenDialog(stage)
            file?.let {
                loadNetGraphFromFile(it)
                outputArea.text = "File loaded successfully."
            }
        }

        // View button actions
        viewButton.setOnAction {
            val mstEdges = graph.calculateMST()
            val outputText = buildString {
                append("MST (Minimum Spanning Tree):\n")
                var distance = 0
                for (edge in mstEdges) {
                    append("${edge.city1.name} -- ${edge.city2.name} == ${edge.distance}\n")
                    distance += edge.distance
                }
                append("Total MST Distance: $distance")
            }
            outputArea.text = outputText
        }

        // Creating layout
        val layout = VBox(10.0, Label("MST Generator"), actionButton, viewButton, outputArea)
        val scene = Scene(layout, 600.0, 600.0)

        stage.scene = scene
        stage.title = "Telecoms App"
        stage.show()
    }

    private fun loadNetGraphFromFile(file: File) {
        graph.clear()
        file.forEachLine { line ->

//            println("Processing lines: $line")
            val parts = line.split(Regex(",\\s*"))  // split by , and whitespace
            if (line.isBlank()) return@forEachLine  //skips empty lines
//            println("Parts after split: $parts") //debbuging

            if (parts.size == 3) {
                val city1 = City(parts[0].trim())
                val city2 = City(parts[1].trim())
                val distance = parts[2].trim().toIntOrNull()
                if (distance != null) {
                    graph.add_an_Edge(Edge(city1, city2, distance))

                    println("edge added: ${city1.name} -- ${city2.name} with distance $distance")

                } else {
                    showError("wrong distance: $line")
                }
            } else { showError("wrong line format: $line")
            }

        }
    }
}

private fun showError(message: String) {
    val alert = Alert(Alert.AlertType.ERROR)
    alert.title = "Error"
    alert.headerText = null
    alert.contentText = message
    alert.showAndWait()
}

fun main() {
    Application.launch(DeskApp::class.java)
}

