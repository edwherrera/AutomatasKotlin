package com.Automata.Interfaces

import com.Automata.Exceptions.*

interface AutomatonType<T : StateType<T>> {
    open fun getInitialState(): T?
    open fun getStates(): MutableList<T>

    @Throws(StateAlreadyExistsException::class)
    open fun addState(state:T)

    @Throws(StateNotFoundException::class)
    open fun removeState(stateValue:String): T

    open fun getState(stateValue: String): T?

    @Throws(StateNotFoundException::class)
    open fun setInitialState(stateValue: String): T

    open fun evaluate(string: String): Boolean

    @Throws(StateNotFoundException::class)
    open fun addTransition(transition: String, fromState: T, toState: T)

    open fun hasState(stateValue: String): Boolean

    @Throws(StateNotFoundException::class, TransitionAlreadyExistsException::class)
    open fun setFinalState(stateValue: String): T

    @Throws(NotImplementedError::class)
    open fun getFinalStates(): MutableList<T> {
        throw NotImplementedError()
    }

    @Throws(NotImplementedError::class)
    open fun getAllTransitions(): MutableSet<String> {
        throw NotImplementedError()
    }
}
