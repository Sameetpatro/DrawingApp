package com.example.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.graphics.Path
import android.util.TypedValue
import android.view.MotionEvent
import org.w3c.dom.Text

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs){

    //drawing path
    private lateinit var drawPath: FingerPath

    //what to draw
    private lateinit var canvasPaint: Paint

    //defines what to draw
    private lateinit var drawPaint: Paint
    private var color = Color.BLACK
    private lateinit var canvas: Canvas
    private lateinit var canvasBitmap : Bitmap
    private var brushSize: Float = 0.toFloat()
    private var paths = mutableListOf<FingerPath>()

    init{
        setUpDrawing()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888) //this works when user zooms or changes its orientation
        canvas = Canvas(canvasBitmap)
    }


    //icon is pending here
    private fun undo(paths: MutableList<FingerPath>){
        if(!paths.isEmpty()){
            paths.removeAt(paths.size - 1)
            invalidate() // reflect back to screen
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean { //called by system when user is going to touch the screen
        val touchX = event?.x
        val touchY = event?.y
        when(event?.action){
            //fired when user put finger on screen
            MotionEvent.ACTION_DOWN ->{
                drawPath.color = color
                drawPath.brushThickness = brushSize.toFloat()

                drawPath.reset() // resetting path before we see initial point
                drawPath.moveTo(touchX!!,touchY!!)
            }

            //this event will be fired when the user starts to move his finger
            //this will be fired continously until user picks up his finger
            MotionEvent.ACTION_MOVE ->{
                drawPath.lineTo(touchX!!, touchY!!)
                //this will be fired until user picks up his finger from screen
                //fires continuously until user is still touching screen
            }
            MotionEvent.ACTION_UP ->{
                canvas.drawPath(drawPath, drawPaint) //commit path to bitmap
                drawPath = FingerPath(color, brushSize)
                paths.add(FingerPath(color, brushSize))
                //when user lifts his finger
            }
            else -> return false

        }
        invalidate() //refreshing the layout to reflect the drawing changes
//        return super.onTouchEvent(event)
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas?.drawBitmap(canvasBitmap, 0f,0f,drawPaint) //creates a canvas before drawing
        if(!drawPath.isEmpty){
            drawPaint.strokeWidth = drawPath.brushThickness
            drawPaint.color = drawPath.color
            canvas.drawPath(drawPath,drawPaint) //drawing path on canvas
        }
    }
    private fun setUpDrawing(){
        drawPaint = Paint()
        drawPath = FingerPath(color, brushSize)
        drawPaint.color = color
        drawPaint.style = Paint.Style.STROKE
        drawPaint.strokeCap = Paint.Cap.ROUND
        drawPaint.strokeJoin = Paint.Join.ROUND
        brushSize = 20.toFloat()
        canvasPaint = Paint(Paint.DITHER_FLAG) //DITHER is a color blending technique, it mix nearby  pixels to create smoother transition for change in color

    }

    fun changeBrushSize(newSize: Float){
        brushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,newSize,resources.displayMetrics)
        //Typedvalue function makes sure the size of brush is same over all devices even if they have different resolution
        //resources.displaymetrics is the user device screen density
        drawPaint.strokeWidth = brushSize
    }

    internal inner class FingerPath(var color: Int, var brushThickness: Float) : Path()
}