package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import com.example.background.R

private const val TAG = "BlurWorker"

class BlurWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {

        val appContext = applicationContext
        // get the URI we passed in from the Data object:
        val resourceUri = inputData.getString(KEY_IMAGE_URI)

        // Display a status notification using the function, makeStatusNotification
        // to notify the user about blurring the image.
        makeStatusNotification("Blurring image", appContext)

        // ADD THIS TO SLOW DOWN THE WORKER
        sleep()
        // ^^^^
        return try {
            if (TextUtils.isEmpty(resourceUri)) {
                Log.i(TAG, "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }
            // Create a Bitmap from the cupcake image:
            val picture = BitmapFactory.decodeStream(
                appContext.contentResolver.openInputStream(Uri.parse(resourceUri))
            )
            // Get a blurred version of the bitmap
            val blurredPicture = blurBitmap(picture, appContext)
            // Write that bitmap to a temporary file
            val blurredPictureUri = writeBitmapToFile(appContext, blurredPicture)
            // Make a Notification displaying the URI
            makeStatusNotification("Blurred Image URI: $blurredPictureUri", appContext)
            // Provide the Output URI as an output Data to make this temporary image
            // easily accessible to other workers for further operations.
            // This will be useful in the next chapter when you create a Chain of workers.
            val outputData = workDataOf(KEY_IMAGE_URI to blurredPictureUri.toString())
            Result.success(outputData)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            Log.e(TAG, "Error applying blur")
            Result.failure()
        }
    }
}