package com.Automata.Implementations

import com.Automata.Exceptions.StateAlreadyExistsException
import com.Automata.Exceptions.StateNotFoundException
import com.Automata.Exceptions.TransitionAlreadyExistsException
import com.Automata.Interfaces.AutomatonType
import com.Automata.Interfaces.StateType
import java.util.*

open class DFA : AutomatonType {

    private var _initialState: GraphableState? = null
    private var _states: MutableList<GraphableState> = ArrayList<GraphableState>()

    override fun getInitialState(): GraphableState? {
        return _initialState
    }

    override fun getStates(): MutableList<GraphableState> {
        return ArrayList(_states)
    }

    override fun addState(state: GraphableState) {
        if (getState(state.getValue()) != null) {
            throw StateAlreadyExistsException()
        }

        _states.add(state)
        if (_states.count() == 1) {
            _initialState = _states.first()
        }
    }

    override fun removeState(stateValue: String): GraphableState {
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

    override fun getState(stateValue: String): GraphableState? {
        for (state:GraphableState in _states) {
            if (state.getValue() == stateValue) {
                return state
            }
        }
        return null
    }

    override fun setInitialState(stateValue: String): GraphableState {
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

    override fun addTransition(transition: String, fromState: GraphableState, toState: GraphableState)  {
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

    override fun setFinalState(stateValue: String): GraphableState {
        val foundState = getState(stateValue) ?: throw StateNotFoundException()
        foundState.setIsFinal(!foundState.getIsFinal())
        return foundState
    }

    override fun getFinalStates(): MutableList<GraphableState> {
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