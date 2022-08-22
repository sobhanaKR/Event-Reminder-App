package com.example.AlarmDate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.AlarmDate.data.Counter
import com.example.AlarmDate.data.ShoppingItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.time.LocalDate
import java.util.*

class AlarmDateActivity: AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: ShoppingViewModelFactory by instance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProviders.of(this, factory)[ShoppingViewModel::class.java]
        installSplashScreen().apply {
         setKeepVisibleCondition {
             viewModel.isLoad.value
         }
        }
        setContentView(R.layout.activity_shopping)



        val adapter = ShoppingItemAdapter(listOf(), viewModel, this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            findViewById<RecyclerView>(R.id.rvShoppingItems).layoutManager = LinearLayoutManager(this)
            requireViewById<RecyclerView>(R.id.rvShoppingItems).adapter = adapter
        }
        viewModel.getAllShoppingItems().observe(this, Observer {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })



        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            ShoppingDialog(
                this,
                object : AddDialogListener {
                    override fun onAddButtonClicked(item: ShoppingItem) {
                        viewModel.upsert(item)
                    }
                }).show()
        }
    }

    fun openCalendar(curShoppingItem: ShoppingItem, nextBDay: LocalDate?) {

        createNotificationChannel()
        val cal = Calendar.getInstance()
        val intent = Intent(Intent.ACTION_EDIT)
        intent.type = "vnd.android.cursor.item/event"
        intent.putExtra("beginTime", cal.timeInMillis)
        intent.putExtra("allDay", false)
        intent.putExtra("rrule", "FREQ=YEARLY")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.putExtra("endTime", nextBDay?.dayOfYear)
        }
        intent.putExtra(CalendarContract.Events.TITLE, "Birthday Reminder for "+ curShoppingItem.name)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(this,intent, null)
    }

    private fun createNotificationChannel() {
        val service = CounterNotificationService(applicationContext)
        service.showNotification("sobha", Counter.value)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel (
CounterNotificationService.COUNTER_CHANNEL_ID,
                "counter",
                     NotificationManager.IMPORTANCE_DEFAULT
                    )

            channel.description = "Used for Birthday Reminder"
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}
