import com.Automata.Exceptions.StateAlreadyExistsException
import com.Automata.Implementations.DFA
import com.Automata.Implementations.GraphableState
import com.Automata.Interfaces.AutomatonType
import com.mxgraph.swing.handler.mxKeyboardHandler
import com.mxgraph.swing.handler.mxRubberband
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.util.mxRectangle
import com.mxgraph.view.mxGraph
import javafx.application.Application
import javafx.embed.swing.SwingNode
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage

class LayoutSample : Application() {

    private var graphComponent:mxGraphComponent = mxGraphComponent(mxGraph())
    private var automaton:AutomatonType<GraphableState> = DFA<GraphableState>()
    private var stage: Stage = Stage()
    private var stateNodes:MutableMap<String, Any> = mutableMapOf()

    override fun start(stage: Stage) {
        this.stage = stage

        graphComponent = buildGraphComponent()
        val border = BorderPane()
        val hbox = addHBox()
        border.top = hbox
        border.bottom = addVBox()

        val scene = Scene(border)
        stage.scene = scene
        stage.title = "Automata"
        stage.width = 600.0
        stage.height = 600.0
        stage.show()
    }


    private fun addHBox(): HBox {

        val hbox = HBox()
        hbox.padding = Insets(15.0, 12.0, 15.0, 12.0)
        hbox.spacing = 10.0
        hbox.style = "-fx-background-color: #336699;"

        val addState = Button("Add State")
        addState.setPrefSize(100.0, 20.0)

        addState.onMouseClicked = EventHandler<javafx.scene.input.MouseEvent>{
            try {
                val stateName:String = TextInputDialog("State").showAndWait().get()
                val x: Double = TextInputDialog("x").showAndWait().get().toDouble()
                val y: Double = TextInputDialog("y").showAndWait().get().toDouble()
                graphComponent.graph.model.beginUpdate()
                val newState = GraphableState(stateName, x, y)
                automaton.addState(newState)
                val newNode = graphComponent.graph.insertVertex(graphComponent.graph.defaultParent, null, newState.getValue(), newState.getXCoordinate(),
                        newState.getYCoordinate(), 80.0, 60.0, "shape=ellipse;perimeter=ellipsePerimeter")
                stateNodes.put(stateName, newNode)
            } catch (e: StateAlreadyExistsException){

            } catch (e: Exception) {

            }finally {
                graphComponent.graph.model.endUpdate()
            }
            graphComponent.graph.refresh()
        }

        val addTransition = Button("Add Transition")
        addTransition.setPrefSize(120.0, 20.0)

        addTransition.onMouseClicked = EventHandler<javafx.scene.input.MouseEvent> {
            try {
              val transitionName = TextInputDialog("Transition").showAndWait().get()
                var states = mutableListOf("")
                automaton.getStates().forEach { state ->
                    states.add(state.getValue())
                }
                val fromState = ChoiceDialog<String>(states.first(), states).showAndWait().get()
                val toState = ChoiceDialog<String>(states.first(), states).showAndWait().get()

                automaton.addTransition(transitionName, automaton.getState(fromState)!!, automaton.getState(toState)!!)
                graphComponent.graph.model.beginUpdate()
                graphComponent.graph.insertEdge(graphComponent.graph.defaultParent, null, transitionName, stateNodes.get(fromState), stateNodes.get(toState))
            } catch (e: Exception) {

            } finally {
                graphComponent.graph.model.endUpdate()
            }

            graphComponent.graph.refresh()
        }

        val evaluate = Button("Evaluate")
        evaluate.setPrefSize(120.0, 20.0)

        evaluate.onMouseClicked = EventHandler<javafx.scene.input.MouseEvent> {
            try {
                val stringToEvaluate = TextInputDialog().showAndWait().orElse("")
                val result = automaton.evaluate(stringToEvaluate)
                val alert = javafx.scene.control.Alert(Alert.AlertType.INFORMATION)
                alert.contentText = if (result) "TRUE" else "FALSE"
                alert.showAndWait()
            } catch (e:Exception) {

            }
        }

        hbox.children.addAll(addState, addTransition)

        return hbox
    }

    private fun addVBox(): VBox {

        val vbox = VBox()

        vbox.children.add(SwingNode().apply { content =  graphComponent })

        return vbox
    }


    private fun buildGraphComponent(): mxGraphComponent {
        var graph = mxGraph()
        graph.minimumGraphSize = mxRectangle(0.0, 0.0, 600.0, 600.0)

        val graphComponent = mxGraphComponent(graph)
        graph.isMultigraph = false
        graph.isAllowDanglingEdges = false
        graphComponent.isConnectable = true
        graphComponent.setToolTips(true)

        mxRubberband(graphComponent)
        mxKeyboardHandler(graphComponent)

        return graphComponent
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            Application.launch(LayoutSample::class.java, *args)
        }
    }
}

//m50x