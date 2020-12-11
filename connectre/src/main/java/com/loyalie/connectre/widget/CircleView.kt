package com.loyalie.connectre.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CircleView : View {

    var circleRadius: Int = 30
    var strokeWidth: Int = 0

    var circleColor: Int = 0
    var drawOnlyStroke: Boolean = false

    private var xyCordinates: Float = 0.0f

    private val paint: Paint = Paint()

    constructor(context: Context, circleRadius: Int, circleColor: Int) : super(context) {
        this.circleRadius = circleRadius
        this.circleColor = circleColor

        initValues()
    }

    constructor(
        context: Context,
        circleRadius: Int,
        circleColor: Int,
        drawOnlyStroke: Boolean,
        strokeWidth: Int
    ) : super(context) {
        this.circleRadius = circleRadius
        this.circleColor = circleColor

        this.drawOnlyStroke = drawOnlyStroke
        this.strokeWidth = strokeWidth

        initValues()
    }

    constructor(context: Context) : super(context) {
        initValues()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        initValues()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        initValues()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthHeight = (2 * (circleRadius)) + strokeWidth

        setMeasuredDimension(widthHeight, widthHeight)
    }

    private fun initValues() {
        paint.isAntiAlias = true

        if (drawOnlyStroke) {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = strokeWidth.toFloat()
        } else {
            paint.style = Paint.Style.FILL
        }
        paint.color = circleColor

        //adding half of strokeWidth because
        //the stroke will be half inside the drawing circle and half outside
        xyCordinates = (circleRadius + (strokeWidth / 2)).toFloat()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(xyCordinates, xyCordinates, circleRadius.toFloat(), paint)
    }


}