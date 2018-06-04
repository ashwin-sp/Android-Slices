package com.bhaideveloper.slices

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import com.bhaideveloper.slices.MyReceiver.Companion.CHECKED
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.NotificationCompat.getExtras
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter


class MainActivity : AppCompatActivity() {

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if(intent.hasExtra(CHECKED))
            {
                println("broadcast received with intent")
                MySliceProvider.state = intent.getBooleanExtra(CHECKED, false)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerReceiver(broadcastReceiver, IntentFilter("stateBroadcast"))
        toggle.setOnCheckedChangeListener { p0, p1 ->
            MySliceProvider.state = p1
            this.contentResolver.notifyChange(MySliceProvider.getUri(this,"/task"),null)
        }

    }

    override fun onResume() {
        super.onResume()
        registerReceiver(broadcastReceiver, IntentFilter("stateBroadcast"))
        toggle.isChecked = MySliceProvider.state
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onDestroy() {
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }


}
