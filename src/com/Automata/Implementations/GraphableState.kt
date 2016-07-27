package com.Automata.Implementations

import com.Automata.Interfaces.Graphable

/**
 * Created by eherrerra on 7/22/16.
 */
open class GraphableState : State<GraphableState>, Graphable {

    private var x: Double
    private var y: Double

    constructor(value: String, x: Double, y: Double) : super(value) {
        this.x = x;
        this.y = y;
    }

    override fun getXCoordinate(): Double {
        return x
    }

    override fun getYCoordinate(): Double {
        return y
    }

    override fun setXCoordinate(x: Double) {
        this.x = x
    }

    override fun setYCoordinate(y: Double) {
        this.y = y
    }
}