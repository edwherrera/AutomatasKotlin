package com.Automata.Implementations

import com.Automata.Exceptions.StateAlreadyExistsException
import com.Automata.Exceptions.StateNotFoundException
import com.Automata.Exceptions.TransitionAlreadyExistsException
import com.Automata.Interfaces.AutomatonType
import com.Automata.Interfaces.StateType
import java.util.*

open class DFA<T : StateType<T>> : AutomatonType<T> {

    private var _initialState: T? = null
    private var _states: MutableList<T> = ArrayList<T>()

    override fun getInitialState(): T? {
        return _initialState
    }

    override fun getStates(): MutableList<T> {
        return ArrayList(_states)
    }

    override fun addState(state: T) {
        if (getState(state.getValue()) != null) {
            throw StateAlreadyExistsException()
        }

        _states.add(state)
        if (_states.count() == 1) {
            _initialState = _states.first()
        }
    }

    override fun removeState(stateValue: String): T {
        _states.forEachIndexed { i, state ->
            if (state.getValue().equals(stateValue)) {
                clearTransitionsToState(stateValue)
                val removedState = _states.removeAt(i)
                if(_initialState!!.getValue().equals(stateValue)) {
                     _initialState = _states.firstOrNull()
                }
                return removedState
            }
        }

        throw StateNotFoundException()
    }

    override fun getState(stateValue: String): T? {
        for (state:T in _states) {
            if (state.getValue() == stateValue) {
                return state
            }
        }
        return null
    }

    override fun setInitialState(stateValue: String): T {
        val foundState = getState(stateValue) ?: throw StateNotFoundException()
        _initialState = foundState
        return foundState
    }

    override fun evaluate(string: String): Boolean {
        var state = getInitialState() ?: return false

        for (c in string.toCharArray()) {
            val destinyState = state.getDestinyStates(c.toString()).firstOrNull() ?: return false
            state = destinyState
        }

        return state.getIsFinal()
    }

    override fun addTransition(transition: String, fromState: T, toState: T)  {
        val fromState = getState(fromState.getValue()) ?: throw StateNotFoundException()
        val toState = getState(toState.getValue()) ?: throw StateNotFoundException()

        if (fromState.hasTransition(transition)) {
            throw TransitionAlreadyExistsException()
        }

        fromState.setDestinyState(toState, transition)
    }

    override fun hasState(stateValue: String): Boolean {
        return getState(stateValue) != null
    }

    override fun setFinalState(stateValue: String): T {
        val foundState = getState(stateValue) ?: throw StateNotFoundException()
        foundState.setIsFinal(!foundState.getIsFinal())
        return foundState
    }

    override fun getFinalStates(): MutableList<T> {
        val ml = _states.filter { state -> state.getIsFinal() }
        return ml.toMutableList()
    }

    private fun clearTransitionsToState(value: String) {
        _states.forEach { state ->
            state.removeTransitionsToState(value)
        }
    }

    override fun getAllTransitions(): MutableSet<String> {
        var transitions = mutableSetOf<String>()
        _states.forEach { state ->
            transitions.addAll(state.getTransitions().keys)
        }
        return transitions
    }
}