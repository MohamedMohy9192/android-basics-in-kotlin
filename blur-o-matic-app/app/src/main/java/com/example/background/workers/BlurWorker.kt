package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.R

private const val TAG = "BlurWorker"

class BlurWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {

        val appContext = applicationContext
        // Display a status notification using the function, makeStatusNotification
        // to notify the user about blurring the image.
        makeStatusNotification("Blurring image", appContext)
        return try {
            // Create a Bitmap from the cupcake image:
            val picture = BitmapFactory.decodeResource(
                appContext.resources,
                R.drawable.android_cupcake
            )
            // Get a blurred version of the bitmap
            val blurredPicture = blurBitmap(picture, appContext)
            // Write that bitmap to a temporary file
            val blurredPictureUri = writeBitmapToFile(appContext, blurredPicture)
            // Make a Notification displaying the URI
            makeStatusNotification("Blurred Image URI: $blurredPictureUri", appContext)

            Result.success()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            Log.e(TAG, "Error applying blur")
            Result.failure()
        }
    }
}