package com.bhaideveloper.slices

import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import androidx.core.graphics.drawable.IconCompat

import androidx.slice.Slice
import androidx.slice.SliceProvider
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.ListBuilder.INFINITY
import androidx.slice.builders.SliceAction
import com.bhaideveloper.slices.MyReceiver.Companion.CHECKED

class MySliceProvider : SliceProvider() {
    /**
     * Instantiate any required objects. Return true if the provider was successfully created,
     * false otherwise.
     */

    override fun onCreateSliceProvider(): Boolean {
        return true
    }
//
//    /**
//     * Converts URL to content URI (i.e. content://com.bhaideveloper.slices...)
//     */
//    override fun onMapIntentToUri(intent: Intent?): Uri {
//        // Note: implementing this is only required if you plan on catching URL requests.
//        // This is an example solution.
//        var uriBuilder: Uri.Builder = Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
//        if (intent == null) return uriBuilder.build()
//        val data = intent.data
//        if (data != null && data.path != null) {
//            val path = data.path.replace("/", "")
//            uriBuilder = uriBuilder.path(path)
//        }
//        val context = context
//        if (context != null) {
//            uriBuilder = uriBuilder.authority(context.packageName)
//        }
//        return uriBuilder.build()
//    }

    /**
     * Construct the Slice and bind data if available.
     */
    override fun onBindSlice(sliceUri: Uri): Slice? {
        return if (sliceUri.path == "/") {
            // Path recognized. Customize the Slice using the androidx.slice.builders API.
            // Note: ANR and StrictMode are enforced here so don't do any heavy operations. 
            // Only bind data that is currently available in memory.
             val spannableStr = SpannableStringBuilder("URI found.")
             spannableStr.setSpan(StyleSpan(Typeface.ITALIC),0, spannableStr.length, 0)
             ListBuilder(context, sliceUri, INFINITY)
                    .addRow {it.setTitle(spannableStr)
                    it.setPrimaryAction(createActivityAction())
                    it.addEndItem(createToggleAction(!state))
                    }
                    .build()
        } else {
            // Error: Path not found.
             ListBuilder(context, sliceUri, INFINITY)
                    .addRow { it.setTitle("URI not found.") }
                    .build()

        }
    }

    private fun createActivityAction(): SliceAction {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(CHECKED, state).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return SliceAction(PendingIntent.getActivity(context, 0, intent, 0),
                IconCompat.createWithResource(context, R.drawable.ic_launcher_foreground),
                "Open MainActivity."
        )
    }

    private fun createToggleAction(boolean: Boolean): SliceAction{
        val intent = Intent()
        intent.setClass(context, MyReceiver::class.java)
        intent.putExtra(CHECKED, boolean)
        val toggleIntent = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        return SliceAction(toggleIntent, "Toggle", state)
    }

    /**
     * Slice has been pinned to external process. Subscribe to data source if necessary.
     */
//    override fun onSlicePinned(sliceUri: Uri?) {
//        context.contentResolver.notifyChange(Uri.parse("content://com.bhaideveloper.slices/"), null)
//        // When data is received, call context.contentResolver.notifyChange(sliceUri, null) to
//        // trigger MySliceProvider#onBindSlice(Uri) again.
//    }

//    /**
//     * Unsubscribe from data source if necessary.
//     */
//    override fun onSliceUnpinned(sliceUri: Uri?) {
//        // Remove any observers if necessary to avoid memory leaks.
//    }

    companion object {
        var state: Boolean = false
        fun getUri(context: Context, path: String): Uri {
            return Uri.Builder()
                    .scheme(ContentResolver.SCHEME_CONTENT)
                    .authority(context.packageName)
                    .appendPath(path)
                    .build()
        }
    }
}
