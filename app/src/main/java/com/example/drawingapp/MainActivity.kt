package com.example.drawingapp

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text
import androidx.core.graphics.toColorInt
import com.example.drawingapp.databinding.ActivityMainBinding
import android.net.Uri
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable




class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var drawingView: DrawingView
    private lateinit var brushImageButton: ImageButton
    private lateinit var colorPalatteButton: ImageButton
    private lateinit var undoButton: ImageButton
    private lateinit var deleteAllButton: ImageButton
    private lateinit var importButton: ImageButton
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        drawingView = findViewById<DrawingView>(R.id.drawing_view)
        brushImageButton = findViewById<ImageButton>(R.id.btn_brush_size)
        colorPalatteButton = findViewById<ImageButton>(R.id.btn_color_palette)
        undoButton = findViewById<ImageButton>(R.id.btn_undo)
        deleteAllButton = findViewById<ImageButton>(R.id.btn_delete_all)
        importButton = findViewById<ImageButton>(R.id.btn_addfile)

        colorPalatteButton.setOnClickListener { showColorPalatteDialog() }
        brushImageButton.setOnClickListener { showBrushSizeDialoge() }
        drawingView.changeBrushSize(25.toFloat())
        importButton.setOnClickListener{
            checkPermissionAndOpenGallery()
//            Toast.makeText(this@MainActivity, "Import Button clicked", Toast.LENGTH_LONG).show()
//            checkAndRequestStoragePermission()
        }


        undoButton.setOnClickListener {
            drawingView.undo()
        }

        deleteAllButton.setOnClickListener {
            drawingView.deleteAll()
        }
    }


    private fun showColorPalatteDialog() {
        val colorPalatteDialog = Dialog(this@MainActivity)
        colorPalatteDialog.setContentView(R.layout.dialoge_color_palatte)

        // Find all color buttons
        val redBtn = colorPalatteDialog.findViewById<ImageButton>(R.id.red_btn)
        val greenBtn = colorPalatteDialog.findViewById<ImageButton>(R.id.green_btn)
        val blueBtn = colorPalatteDialog.findViewById<ImageButton>(R.id.blue_btn)
        val yellowBtn = colorPalatteDialog.findViewById<ImageButton>(R.id.yellow_btn)
        val blackBtn= colorPalatteDialog.findViewById<ImageButton>(R.id.black_btn)
        val whiteBtn = colorPalatteDialog.findViewById<ImageButton>(R.id.white_btn)
        val orangeBtn = colorPalatteDialog.findViewById<ImageButton>(R.id.orange_btn)
        val brownBtn = colorPalatteDialog.findViewById<ImageButton>(R.id.brown_btn)
        val purpleBtn = colorPalatteDialog.findViewById<ImageButton>(R.id.purple_btn)
        val babypinkBtn = colorPalatteDialog.findViewById<ImageButton>(R.id.babypink_btn)
        val grayBtn = colorPalatteDialog.findViewById<ImageButton>(R.id.gray_btn)
        val cyanBtn = colorPalatteDialog.findViewById<ImageButton>(R.id.cyan_btn)
        val magentaBtn = colorPalatteDialog.findViewById<ImageButton>(R.id.magenta_btn)
        val maroonBtn = colorPalatteDialog.findViewById<ImageButton>(R.id.maroon_btn)
        val tealBtn = colorPalatteDialog.findViewById<ImageButton>(R.id.teal_btn)

        // Set click listeners for each button
        redBtn.setOnClickListener {
            drawingView.changeColor(Color.RED)
            colorPalatteDialog.dismiss()
        }
        blueBtn.setOnClickListener {
            drawingView.changeColor(Color.BLUE)
            colorPalatteDialog.dismiss()
        }
        greenBtn.setOnClickListener {
            drawingView.changeColor(Color.GREEN)
            colorPalatteDialog.dismiss()
        }
        yellowBtn.setOnClickListener {
            drawingView.changeColor(Color.YELLOW)
            colorPalatteDialog.dismiss()
        }
        blackBtn.setOnClickListener {
            drawingView.changeColor(Color.BLACK)
            colorPalatteDialog.dismiss()
        }
        whiteBtn.setOnClickListener {
            drawingView.changeColor(Color.WHITE)
            colorPalatteDialog.dismiss()
        }
        orangeBtn.setOnClickListener {
            drawingView.changeColor("#FF5000".toColorInt())
            colorPalatteDialog.dismiss()
        }
        brownBtn.setOnClickListener {
            drawingView.changeColor("#652A2A".toColorInt())
            colorPalatteDialog.dismiss()
        }
        purpleBtn.setOnClickListener {
            drawingView.changeColor("#FF00FF".toColorInt())
            colorPalatteDialog.dismiss()
        }
        cyanBtn.setOnClickListener {
            drawingView.changeColor(Color.CYAN)
            colorPalatteDialog.dismiss()
        }
        magentaBtn.setOnClickListener {
            drawingView.changeColor(Color.MAGENTA)
            colorPalatteDialog.dismiss()
        }
        maroonBtn.setOnClickListener {
            drawingView.changeColor("#800080".toColorInt())
            colorPalatteDialog.dismiss()
        }
        babypinkBtn.setOnClickListener {
            drawingView.changeColor("#FFC0CB".toColorInt())
            colorPalatteDialog.dismiss()
        }
        tealBtn.setOnClickListener {
            drawingView.changeColor("#008080".toColorInt())
            colorPalatteDialog.dismiss()
        }
        grayBtn.setOnClickListener {
            drawingView.changeColor("#808080".toColorInt())
            colorPalatteDialog.dismiss()
        }

        colorPalatteDialog.show()
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

    //importing an image

//    private val pickImageLauncher =
//        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//            uri?.let {
//                try {
//                    val inputStream = contentResolver.openInputStream(it)
//                    val bitmap = BitmapFactory.decodeStream(inputStream)
//                    inputStream?.close()
//
//                    // Set bitmap as background of DrawingView
//                    binding.drawingView.background = BitmapDrawable(resources, bitmap)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    private fun checkPermissionAndOpenGallery() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else {
            requestPermissionLauncher.launch(permission)
        }
    }
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                drawingView.setImageBitmapFromUri(it, this)
            }
        }



    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }   




    override fun onClick(view: View?) {
    }



}