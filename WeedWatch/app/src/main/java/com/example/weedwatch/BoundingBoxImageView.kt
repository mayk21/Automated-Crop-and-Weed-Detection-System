package com.example.weedwatch

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class BoundingBoxImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {
    private val boxes: MutableList<BoundingBox> = mutableListOf() // List to store bounding boxes with labels
    private val paint: Paint = Paint().apply {
        strokeWidth = 5f
        style = Paint.Style.STROKE // Only stroke, no fill
    }

    // Class to hold bounding box and its label
    data class BoundingBox(val rect: RectF, val label: String, val confidence: Float)

    // Method to add bounding boxes with labels to the list
    fun setBoundingBoxes(newBoxes: List<BoundingBox>) {
        boxes.clear()
        boxes.addAll(newBoxes)
        invalidate() // Redraw the image with new bounding boxes
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Get the current image matrix to account for scaling
        val matrix = imageMatrix
        val transformedBox = RectF()

        // Draw the bounding boxes
        for (box in boxes) {
            // Set the color based on the label (green for wheat, red for weed)
            paint.color = if (box.label == "wheat") Color.GREEN else Color.RED

            // Transform the bounding box coordinates according to the image scale
            transformedBox.set(box.rect)
            matrix.mapRect(transformedBox)

            // Draw the transformed bounding box
            canvas.drawRect(transformedBox, paint)

            // Set up text paint
            val textPaint = Paint().apply {
                color = paint.color
                textSize = 40f
                style = Paint.Style.FILL
                isAntiAlias = true
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }

            // Format confidence text
         //   val labelText = "${box.label} (${String.format("%.2f", box.confidence)})"
         //   val labelText = String.format("%.2f", box.confidence)


            // Draw text near the top-left of the bounding box
      //      canvas.drawText(labelText, transformedBox.left + 10, transformedBox.top - 10, textPaint)
            

        }
    }
}
