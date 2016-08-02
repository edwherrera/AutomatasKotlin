package com.Automata.Implementations

import com.Automata.Exceptions.StateNotFoundException
import com.Automata.Interfaces.StateType

class NFA<T: StateType<T>> : DFA<T> {
    constructor() : super()

    override fun evaluate(string: String): Boolean {
        val currentState = getInitialState() ?: return false

        return evaluate(string, currentState)
    }

    private fun evaluate(string: String, currentState: T): Boolean {
        if (string.isEmpty()) return currentState.getIsFinal()
        val toEval = string.toCharArray().first()

        val destStates = currentState.getDestinyStates(toEval.toString())

        for(state in destStates) {
            if(evaluate(string.drop(1), state)) { return true }
        }

        return false
    }

    override fun addTransition(transition: String, fromState: T, toState: T) {
        val fromState = getState(fromState.getValue()) ?: throw StateNotFoundException()
        val toState = getState(toState.getValue()) ?: throw StateNotFoundException()
        fromState.setDestinyState(toState, transition)
    }

}
