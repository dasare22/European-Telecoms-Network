import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.control.ProgressBar
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File

class DeskApp : Application() {
    private val graph = NetGraph()
    private val outputArea = TextArea()
    private val errorArea = TextArea()
    private val statusLabel = Label("Status: Waiting for file input...")
    private val progressBar = ProgressBar(0.0)

    override fun start(stage: Stage) {
        val actionButton = Button("Insert File")
        val viewButton = Button("View Minimum Spanning Tree")
        val correctErrorsButton = Button("Save Errors")
        correctErrorsButton.isDisable = true // Initially disabled
        outputArea.isEditable = false
        errorArea.isEditable = false
        errorArea.style = "-fx-control-inner-background: #fdd;" // Red background for errors
        errorArea.isVisible = false // Hide the error area initially
        progressBar.isVisible = false

        val vbox = VBox(15.0)
        vbox.alignment = Pos.CENTER
        vbox.children.addAll(
            Label("MST Generator"),
            actionButton,
            viewButton,
            correctErrorsButton,
            progressBar,
            statusLabel,
            Label("Output:"),
            outputArea,
            errorArea // Error area will be shown only when necessary
        )

        // File loading action
        actionButton.setOnAction {
            val file = FileChooser().showOpenDialog(stage)
            if (file != null) {
                progressBar.isVisible = true
                statusLabel.text = "Loading file..."
                try {
                    val errors = loadNetGraphFromFile(file)
                    if (errors.isEmpty()) {
                        errorArea.isVisible = false // Hide the error area
                        errorArea.text = ""
                        correctErrorsButton.isDisable = true
                        statusLabel.text = "Status: File loaded successfully."
                        outputArea.text = "File loaded successfully. Ready to calculate MST."
                    } else {
                        errorArea.isVisible = true // Show the error area
                        errorArea.text = errors.joinToString("\n")
                        correctErrorsButton.isDisable = false
                        statusLabel.text = "Status: File loaded with errors."
                        outputArea.text = ""
                    }
                } catch (e: Exception) {
                    showError("Error loading file: ${e.message}")
                    statusLabel.text = "Status: File load failed."
                } finally {
                    progressBar.isVisible = false
                }
            }
        }

        // MST calculation action
        viewButton.setOnAction {
            if (graph.getEdges().isEmpty()) {
                showError("No valid graph loaded. Please load a file first.")
                statusLabel.text = "Status: Please load a file."
                return@setOnAction
            }

            val mstEdges = graph.calculateMST()
            val outputText = buildString {
                append("Minimum Spanning Tree (MST):\n\n")
                var totalDistance = 0
                for (edge in mstEdges) {
                    append("From ${edge.city1.name} to ${edge.city2.name}: ${edge.distance} km\n")
                    totalDistance += edge.distance
                }
                append("\nTotal MST Distance (Cable Length): $totalDistance km")
            }
            outputArea.text = outputText
            statusLabel.text = "Status: MST calculated successfully."
        }

        // Save errors action
        correctErrorsButton.setOnAction {
            val file = FileChooser().showSaveDialog(stage)
            if (file != null) {
                file.writeText(errorArea.text)
                statusLabel.text = "Status: Errors saved. Correct the file and reload."
            }
        }

        val scene = Scene(vbox, 600.0, 700.0)
        stage.scene = scene
        stage.title = "Telecoms App"
        stage.show()
    }

    private fun loadNetGraphFromFile(file: File): List<String> {
        val errors = mutableListOf<String>()
        graph.clear()

        file.readLines().forEachIndexed { lineNumber, line ->
            if (line.isBlank()) return@forEachIndexed

            val parts = line.split(Regex(",\\s*"))
            if (parts.size != 3) {
                errors.add("Line ${lineNumber + 1}: Invalid format - $line")
                return@forEachIndexed
            }

            val city1 = City(parts[0].trim())
            val city2 = City(parts[1].trim())
            val distance = parts[2].trim().toIntOrNull()

            if (distance == null || distance < 0) {
                errors.add("Line ${lineNumber + 1}: Invalid distance value - ${parts[2].trim()}")
                return@forEachIndexed
            }

            graph.addEdge(Edge(city1, city2, distance))
        }
        return errors
    }

    private fun showError(message: String) {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = "Error"
        alert.headerText = null
        alert.contentText = message
        alert.showAndWait()
    }
}

fun main() {
    Application.launch(DeskApp::class.java)
}
