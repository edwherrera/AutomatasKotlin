package com.Automata.Interfaces

import com.Automata.Exceptions.*
import com.Automata.Implementations.GraphableState

interface AutomatonType {

    val EPSILON: String
        get() = "ε"

    open fun getInitialState(): GraphableState?
    open fun getStates(): MutableList<GraphableState>

    @Throws(StateAlreadyExistsException::class)
    open fun addState(state:GraphableState)

    @Throws(StateNotFoundException::class)
    open fun removeState(stateValue:String): GraphableState

    open fun getState(stateValue: String): GraphableState?

    @Throws(StateNotFoundException::class)
    open fun setInitialState(stateValue: String): GraphableState

    open fun evaluate(string: String): Boolean

    @Throws(StateNotFoundException::class)
    open fun addTransition(transition: String, fromState: GraphableState, toState: GraphableState)

    open fun hasState(stateValue: String): Boolean

    @Throws(StateNotFoundException::class, TransitionAlreadyExistsException::class)
    open fun setFinalState(stateValue: String): GraphableState

    @Throws(NotImplementedError::class)
    open fun getFinalStates(): MutableList<GraphableState> {
        throw NotImplementedError()
    }

    @Throws(NotImplementedError::class)
    open fun getAllTransitions(): MutableSet<String> {
        throw NotImplementedError()
    }
}
