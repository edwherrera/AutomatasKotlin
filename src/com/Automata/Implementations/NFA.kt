package com.Automata.Implementations

import com.Automata.Exceptions.StateNotFoundException
import com.Automata.Interfaces.StateType

class NFA : DFA{
    constructor() : super()

    override fun evaluate(string: String): Boolean {
        val currentState = getInitialState() ?: return false

        return evaluate(string, currentState)
    }

    private fun evaluate(string: String, currentState: GraphableState): Boolean {
        if (string.isEmpty()) return currentState.getIsFinal()
        val toEval = string.toCharArray().first()

        val destStates = currentState.getDestinyStates(toEval.toString())

        for(state in destStates) {
            if(evaluate(string.drop(1), state)) { return true }
        }

        return false
    }

    override fun addTransition(transition: String, fromState: GraphableState, toState: GraphableState) {
        val fromState = getState(fromState.getValue()) ?: throw StateNotFoundException()
        val toState = getState(toState.getValue()) ?: throw StateNotFoundException()
        fromState.setDestinyState(toState, transition)
    }

    fun evaluateWithEpsilon(string: String): Boolean {
        val currentState = getInitialState() ?: return false
        return evaluateWithEpsilon(string, currentState)
    }

    private fun evaluateWithEpsilon(string: String, currentState: GraphableState): Boolean {
        val epsilonExits = currentState.getDestinyStates(EPSILON)
        if(string.length == 0) {
            if(epsilonExits.size == 0) {
                return currentState.getIsFinal()
            }
            if(currentState.getIsFinal()){
                return true
            }

            for(exit in epsilonExits) {
                if (exit.getValue() != currentState.getValue()) {
                    if (evaluateWithEpsilon(EPSILON, exit)) {
                        return true
                    }
                }
            }
        } else {
            val valueExits = currentState.getDestinyStates(string.get(0).toString())
            for(exit in valueExits) {
                if(evaluateWithEpsilon(string.drop(1), exit)) {
                    return true
                }
            }
            for(epExit in epsilonExits) {
                if(epExit.getValue() != currentState.getValue()) {
                    if(evaluateWithEpsilon(string, epExit)) {
                        return true
                    }
                }
            }
            return false
        }
        return false
    }
}
