package com.Automata.Implementations

import com.Automata.Interfaces.StateType
import java.util.*

abstract class State<T : State<T>> : StateType<T> {

    private var value: String
    private var isFinal: Boolean = false
    private var transitions: HashMap<String, ArrayList<T>> = HashMap()

    constructor(value: String) {
        this.value = value
    }

    override fun getValue(): String {
        return value
    }

    override fun getIsFinal(): Boolean {
        return isFinal
    }

    override fun setIsFinal(isFinal: Boolean) {
        this.isFinal = isFinal
    }

    override fun getTransitions(): HashMap<String, ArrayList<T>> {
        return transitions
    }

    override fun getDestinyStates(transition: String): ArrayList<T> {
        return transitions.get(transition) ?: return ArrayList()
    }

    override fun hasTransition(transition: String): Boolean {
        return getDestinyStates(transition).count() > 0
    }

    override fun setDestinyState(state: T, transition: String) {
        val destinyStates = getDestinyStates(transition)
        if(destinyStates.all { destState -> destState.getValue() != state.value }) destinyStates.add(state)
        transitions.put(transition, destinyStates)
    }

    override fun equals(other: Any?): Boolean {
        val other = other ?: return false
        val casted = other as? State<*> ?: return false
        return value.equals(casted.value)
    }

    override fun removeTransitionsToState(withValue: String) {
        for (transition in transitions) {
            val destinyStates = transition.value.filter { state ->
                state.value == withValue
            }

            if(destinyStates.size == 0) {
                transitions.remove(transition.key)
            } else {
                transitions.put(transition.key, destinyStates as ArrayList<T>)
            }
        }
    }
}