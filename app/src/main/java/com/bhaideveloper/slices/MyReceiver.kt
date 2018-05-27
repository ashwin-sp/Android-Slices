package com.bhaideveloper.slices

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bhaideveloper.slices.MySliceProvider.Companion.state

class MyReceiver : BroadcastReceiver() {

    companion object {
        var CHECKED = "CHECKED"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if(intent.hasExtra(CHECKED))
        {
            MySliceProvider.state = intent.getBooleanExtra(CHECKED,state)
            println("State at receiver $state")
            val i = Intent("stateBroadcast")
            i.putExtra(CHECKED, state)
            context.contentResolver.notifyChange(MySliceProvider.getUri(context,"/"), null)
            context.sendBroadcast(i);
        }
    }
}
