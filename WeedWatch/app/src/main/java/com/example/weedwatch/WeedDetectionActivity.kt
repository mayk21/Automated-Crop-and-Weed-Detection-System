package com.example.weedwatch

import android.app.Activity
import android.content.Intent
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException

class WeedDetectionActivity : AppCompatActivity() {

    private val PICK_IMAGE = 1
    private val CAPTURE_IMAGE = 2
    private lateinit var imageView: ImageView
    private lateinit var detectButton: Button
    private lateinit var cameraButton: Button
    private lateinit var resultText: TextView
    private var imageUri: Uri? = null
    private val client = OkHttpClient()
    private val serverUrl = "http://10.4.17.175:5000/predict"
 //   private val serverUrl = "http://192.168.10.26:5000/predict"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weed_detection)

        imageView = findViewById(R.id.imageView)
        detectButton = findViewById(R.id.detectButton)
        cameraButton = findViewById(R.id.cameraButton)
        resultText = findViewById(R.id.resultText)

        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE)
        }

        cameraButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val imageFile = File.createTempFile("IMG_", ".jpg", cacheDir)
            imageUri = FileProvider.getUriForFile(
                this,
                "${applicationContext.packageName}.fileprovider",
                imageFile
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, CAPTURE_IMAGE)
        }

        detectButton.setOnClickListener {
            imageUri?.let { sendToServer(it) } ?: Toast.makeText(
                this,
                "Select or capture an image first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE -> {
                    imageUri = data?.data
                }
                CAPTURE_IMAGE -> {
                    // imageUri is already set in onCreate
                }
            }

            imageUri?.let {
                imageView.setImageURI(it)
                resultText.text = ""
                (imageView as BoundingBoxImageView).setBoundingBoxes(emptyList())
            }
        }
    }

    private fun sendToServer(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()

            if (bytes == null) {
                resultText.text = "Failed to read image data"
                return
            }

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "image",
                    "image.jpg",
                    bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
                )
                .build()

            val request = Request.Builder()
                .url(serverUrl)
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        resultText.text = "Error: ${e.message}"
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val jsonResponse = response.body?.string()
                    runOnUiThread {
                        try {
                            val jsonObject = JSONObject(jsonResponse)
                            val detections = jsonObject.getJSONArray("detections")

                            val boundingBoxes = mutableListOf<BoundingBoxImageView.BoundingBox>()

                            if (detections.length() == 0) {
                                resultText.text = "No object detected"
                                (imageView as BoundingBoxImageView).setBoundingBoxes(emptyList())
                                return@runOnUiThread
                            }

                            var wheatCount = 0
                            var weedCount = 0

                            for (i in 0 until detections.length()) {
                                val obj = detections.getJSONObject(i)
                                val label = obj.getString("class")
                                val confidence = obj.getDouble("confidence")
                                val x1 = obj.getInt("x1")
                                val y1 = obj.getInt("y1")
                                val x2 = obj.getInt("x2")
                                val y2 = obj.getInt("y2")

                                when (label.lowercase()) {
                                    "wheat" -> wheatCount++
                                    "weed" -> weedCount++
                                }

                                val box = BoundingBoxImageView.BoundingBox(
                                    rect = RectF(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat()),
                                    label = label,
                                    confidence = confidence.toFloat()
                                )

                                boundingBoxes.add(box)
                            }

                            val summaryText = "Detection Summary:\n• Wheat: $wheatCount\n• Weed: $weedCount"
                            resultText.text = summaryText
                            (imageView as BoundingBoxImageView).setBoundingBoxes(boundingBoxes)

                        } catch (e: Exception) {
                            resultText.text = "Error parsing response: ${e.message}"
                        }
                    }
                }
            })

        } catch (e: Exception) {
            resultText.text = "Error: ${e.message}"
        }
    }
}
