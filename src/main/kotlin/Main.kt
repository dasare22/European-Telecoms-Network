import javafx.application.Application
import javafx.geometry.Pos
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
        val viewButton = Button("View Minimum spanning tree")
        val vbox =VBox(20.0)
        vbox.alignment = Pos.CENTER  // Align elements to the center within VBox
        vbox.children.addAll(actionButton,viewButton)
        outputArea.isEditable = false

        // Handle file selection and processing
        actionButton.setOnAction {
            val file = FileChooser().showOpenDialog(stage)
            file?.let {
                loadNetGraphFromFile(it)
                outputArea.text = "File loaded successfully."
            }
        }

        // Generate and display the Minimum Spanning Tree (MST)
        viewButton.setOnAction {
            val mstEdges = graph.calculateMST()
            val outputText = buildString {
                append("Distance between the cities:\n")
                var distance = 0
                for (edge in mstEdges) {
                    append(" from ${edge.city1.name} to ${edge.city2.name} == ${edge.distance}\n")
                    distance += edge.distance
                }
                append("Total MST Distance(Cable length): $distance")
            }
            outputArea.text = outputText
        }

        // Set up the main layout of the application
        val layout = VBox(20.0, Label("MST Generator"), actionButton, viewButton, outputArea)
        layout.alignment = Pos.CENTER
        val scene = Scene(layout, 400.0, 400.0)

        stage.scene = scene
        stage.title = "Telecoms App"
        stage.show()
    }

    private fun loadNetGraphFromFile(file: File) {
        graph.clear()
        file.forEachLine { line ->

            // Split the line into parts based on commas and optional spaces
            val parts = line.split(Regex(",\\s*"))  // split by , and whitespace
            if (line.isBlank()) return@forEachLine  // Skip empty lines in the file
            // Ensure the line has the correct number of parts

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

