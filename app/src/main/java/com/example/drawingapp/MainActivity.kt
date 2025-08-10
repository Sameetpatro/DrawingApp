package com.example.drawingapp

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private lateinit var drawingView: DrawingView
    private lateinit var brushImageButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        brushImageButton = findViewById<ImageButton>(R.id.brush_button)
        drawingView = findViewById<DrawingView>(R.id.drawing_view)
        brushImageButton.setOnClickListener { showBrushSizeDialoge() }
        drawingView.changeBrushSize(25.toFloat())
    }

    private fun showBrushSizeDialoge (){
        val brushDialog = Dialog(this@MainActivity)
        brushDialog.setContentView(R.layout.dialoge_brush_size)
        val seekBarProgress = brushDialog.findViewById<SeekBar>(R.id.brush_seekBar)
        val showBrushSeekbarProgress = brushDialog.findViewById<TextView>(R.id.brushSize_textview)

        seekBarProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                drawingView.changeBrushSize(seekBar!!.progress.toFloat())
                showBrushSeekbarProgress.text = seekBar!!.progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        brushDialog.show()
    }
}