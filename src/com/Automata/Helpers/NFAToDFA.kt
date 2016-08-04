package com.Automata.Helpers

import com.Automata.Implementations.DFA
import com.Automata.Implementations.GraphableState
import com.Automata.Implementations.NFA
import com.Automata.Interfaces.AutomatonType
import com.Automata.Interfaces.StateType


class NFAToDFA {

    fun convert(nfa: NFA<GraphableState>): DFA<GraphableState> {

        val dfa = DFA<GraphableState>()

        val transitionValues = getTransitionValues(nfa)
        val exitMatrix:MutableList<Node<GraphableState>> = mutableListOf()
        val possibleRoads:MutableList<MutableList<GraphableState>> = mutableListOf(mutableListOf())

        var initial = nfa.getInitialState()

        exitMatrix.add(Node(mutableListOf(), mutableListOf(initial!!), ""))

        possibleRoads.add(exitMatrix.first().exitStates)
        var i:Int = 0
        while(i < possibleRoads.size) {

            for(transitionValue in transitionValues) {
                val union:MutableList<GraphableState> = union(possibleRoads.get(i), transitionValue)
                exitMatrix.add(Node(possibleRoads[i], union, transitionValue))
                val possibleRoadsContainsUnion: Boolean =
                        possibleRoads.any { road ->
                            road.all { roadState ->
                                union.find { unionState ->
                                    unionState.getValue() == roadState.getValue()
                                } != null
                            }
                        }

                if(!possibleRoadsContainsUnion) {
                    possibleRoads.add(union)
                }
            }

            i++

        }

        for(road in possibleRoads) {
            var stateName = ""
            var isFinal = false
            for(state in road) {
                stateName += state.getValue()
                val newState = nfa.getState(stateName)
                if((newState != null && newState.getIsFinal()) || state.getIsFinal()) {
                    isFinal = true
                }
            }
            val toAdd = GraphableState(stateName, 50.0,50.0)
            toAdd.setIsFinal(isFinal)
            dfa.addState(toAdd)
        }

        for(exit in exitMatrix) {
            var fromStateName = ""
            for(entryState in exit.entryStates) {
                fromStateName += entryState.getValue()
            }
            var toStateName = ""
            for(exitState in exit.exitStates) {
                toStateName += exitState.getValue()
            }
            dfa.addTransition(exit.transitionName, dfa.getState(fromStateName)!!, dfa.getState(toStateName)!!)
        }

        return dfa
    }

    private fun union(states: MutableList<GraphableState>, transitionValue: String): MutableList<GraphableState> {
        val stateUnion = mutableListOf<GraphableState>()

        for(state in states) {
            val exitStates = state.getDestinyStates(transitionValue)
            stateUnion.addAll(exitStates)
        }

        return stateUnion.distinctBy { state -> state.getValue() }.toMutableList()
    }

    private fun getTransitionValues(automaton: AutomatonType<GraphableState>): List<String> {
        return automaton.getStates().flatMap { state -> state.getTransitions().map { transition -> transition.key } }
                .distinct()
    }
}

private class Node<T : StateType<T>>{
    var entryStates: MutableList<T>
    var exitStates: MutableList<T>
    var transitionName: String

    constructor(entryStates: MutableList<T>, exitStates: MutableList<T>, transitionName: String) {
        this.entryStates = entryStates
        this.exitStates = exitStates
        this.transitionName = transitionName
    }
}