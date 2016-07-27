package com.Graphics;

import com.Automata.Exceptions.StateAlreadyExistsException
import com.Automata.Implementations.GraphableState
import com.Automata.Interfaces.AutomatonType
import com.mxgraph.swing.handler.mxKeyboardHandler
import com.mxgraph.swing.handler.mxRubberband
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.util.mxDomUtils
import com.mxgraph.view.mxGraph
import java.awt.GraphicsConfiguration
import java.awt.event.*
import java.util.*
import javax.swing.JFrame
import javax.swing.JOptionPane

open class Grapher : JFrame {

    private var automaton: AutomatonType<GraphableState>
    private var graph: mxGraph
    private var vertices: ArrayList<Any> = ArrayList()

    constructor(automaton: AutomatonType<GraphableState>) : super("Automata") {
        this.automaton = automaton
        this.graph = mxGraph()
    }

    constructor(gc: GraphicsConfiguration?, automaton: AutomatonType<GraphableState>) : super(gc) {
        this.automaton = automaton
        this.graph = mxGraph()
    }

    constructor(title: String?, automaton: AutomatonType<GraphableState>) : super(title) {
        this.automaton = automaton
        this.graph = mxGraph()
    }

    constructor(title: String?, gc: GraphicsConfiguration?, automaton: AutomatonType<GraphableState>) : super(title, gc) {
        this.automaton = automaton
        this.graph = mxGraph()
    }


    open fun start() {
        initialDrawing()
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setSize(800, 600)
        isVisible = true
    }

    private fun initialDrawing() {

        val parent = graph.defaultParent

        graph.model.beginUpdate()
        try {
            val states = automaton.getStates()
            for (state in states) {
                val vertex = graph.insertVertex(parent, null, state.getValue(), state.getXCoordinate(), state.getYCoordinate(), 80.0, 60.0, "shape=ellipse;perimeter=ellipsePerimeter")
                vertices.add(vertex)
            }
        } finally {
            graph.model.endUpdate()
        }

        val graphComponent = mxGraphComponent(graph)
        graph.isMultigraph = false
        graph.isAllowDanglingEdges = false
        graphComponent.isConnectable = true
        graphComponent.setToolTips(true)

        mxRubberband(graphComponent)
        mxKeyboardHandler(graphComponent)

        contentPane.add(graphComponent)

        graphComponent.graphControl.addMouseListener(object : MouseAdapter() {

            override fun mouseReleased(e: MouseEvent?) {
                val cell = graphComponent.getCellAt(e!!.x, e.y)

                if (cell == null) {
                    try {
                        val stateName = JOptionPane.showInputDialog(graphComponent, "State Name: ", "New State",
                                JOptionPane.QUESTION_MESSAGE, null, null, null) as String
                        graph.model.beginUpdate()
                        val newState = GraphableState(stateName, e.x.toDouble(), e.y.toDouble())
                        automaton.addState(newState)
                        graph.insertVertex(parent, null, newState.getValue(), newState.getXCoordinate(),
                                newState.getYCoordinate(), 80.0, 60.0, "shape=ellipse;perimeter=ellipsePerimeter")
                    } catch (e: StateAlreadyExistsException){
                        JOptionPane.showConfirmDialog(
                                graphComponent,
                                e.message,
                                "Error",
                                JOptionPane.OK_OPTION);
                    } catch (e: Exception) {

                    }finally {
                        graph.model.endUpdate()
                    }
                }
            }
        })
    }
}