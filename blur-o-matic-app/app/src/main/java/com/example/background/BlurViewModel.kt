/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.background

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.background.workers.BlurWorker
import com.example.background.workers.CleanupWorker
import com.example.background.workers.SaveImageToFileWorker


class BlurViewModel(application: Application) : ViewModel() {

    // Retrieves the default singleton instance of WorkManager.
    private val workerManager = WorkManager.getInstance(application)

    // New instance variable for the WorkInfo
    internal var outputWorkInfos: LiveData<List<WorkInfo>>

    internal var imageUri: Uri? = null
    internal var outputUri: Uri? = null

    init {
        imageUri = getImageUri(application.applicationContext)
        // This transformation makes sure that whenever the current work Id changes the WorkInfo
        // the UI is listening to changes
        // You can tag multiple WorkRequests with the same tag to associate them.
        outputWorkInfos = workerManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
    }

    /**
     * Creates the input data bundle which includes the Uri to operate on
     * @return Data which contains the Image Uri as a String
     */
    private fun createInputDataForUri(): Data {
        // Input and output is passed in and out via Data objects.
        // Data objects are lightweight containers for key/value pairs.
        // They are meant to store a small amount of data that might pass into and out from WorkRequests.
        val builder = Data.Builder()
        // You're going to pass in the URI for the user's image into a bundle.
        // That URI is stored in a variable called imageUri.
        imageUri?.let { uri ->
            builder.putString(KEY_IMAGE_URI, uri.toString())
        }
        return builder.build()
    }

    /**
     * Create the WorkRequest to apply the blur and save the resulting image
     * @param blurLevel The amount to blur the image
     */
    internal fun applyBlur(blurLevel: Int) {
        // OneTimeWorkRequest: A WorkRequest that will only execute once.
        val cleanupRequest = OneTimeWorkRequestBuilder<CleanupWorker>()
            .build() // equivalent to OneTimeWorkRequest.from(CleanupWorker::class.java)
        // Add WorkRequest to Cleanup temporary images
        // beginUniqueWork() allows you to begin unique chains of work at a time
        var continuation = workerManager.beginUniqueWork(
            // This names the entire chain of work requests so that you can refer to and query them together.
            IMAGE_MANIPULATION_WORK_NAME,
            // use REPLACE because if the user decides to blur another image before the current one
            // is finished, we want to stop the current one and start blurring the new image.
            ExistingWorkPolicy.REPLACE,
            cleanupRequest
        )
        // Add WorkRequests to blur the image the number of times requested
        for (i in 0 until blurLevel) {
            // Add WorkRequest to blur the image
            val blurRequest = OneTimeWorkRequest.Builder(BlurWorker::class.java)
            // Input the Uri if this is the first blur operation
            // After the first blur operation the input will be the output of previous
            // blur operations.
            if (i == 0) {
                blurRequest.setInputData(createInputDataForUri())
            }
            continuation =
                continuation.then(blurRequest.build()) // then() returns a new WorkContinuation instance
        }
        // Add WorkRequest to save the image to the filesystem
        val saveBlurredImageRequest = OneTimeWorkRequest.Builder(SaveImageToFileWorker::class.java)
            // You'll use a tag to label your work instead of using the WorkManager ID,
            // because if your user blurs multiple images, all of the saving image WorkRequests
            // will have the same tag but not the same ID.
            .addTag(TAG_OUTPUT)
            .build()
        continuation = continuation.then(saveBlurredImageRequest)

        // Enqueues the WorkContinuation which is a chain of work
        continuation.enqueue()
    }

    private fun uriOrNull(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
        } else {
            null
        }
    }

    private fun getImageUri(context: Context): Uri {
        val resources = context.resources

        val imageUri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(R.drawable.android_cupcake))
            .appendPath(resources.getResourceTypeName(R.drawable.android_cupcake))
            .appendPath(resources.getResourceEntryName(R.drawable.android_cupcake))
            .build()

        return imageUri
    }

    internal fun setOutputUri(outputImageUri: String?) {
        outputUri = uriOrNull(outputImageUri)
    }

    class BlurViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(BlurViewModel::class.java)) {
                BlurViewModel(application) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
