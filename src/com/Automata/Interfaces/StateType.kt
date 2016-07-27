package com.Automata.Interfaces

import java.util.*

interface StateType<T : StateType<T>> {
    open fun getValue(): String
    open fun getIsFinal(): Boolean
    open fun setIsFinal(isFinal: Boolean)
    open fun getTransitions(): HashMap< String, ArrayList<T> >
    open fun getDestinyStates(transition: String): ArrayList<T>
    open fun hasTransition(transition: String): Boolean
    open fun setDestinyState(state: T, transition: String)
    open fun removeTransitionsToState(withValue:String)
}