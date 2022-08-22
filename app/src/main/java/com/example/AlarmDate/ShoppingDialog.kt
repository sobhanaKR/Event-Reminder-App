package com.example.AlarmDate

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Service
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.widget.AppCompatEditText
import com.example.AlarmDate.data.ShoppingItem
import java.util.*

class ShoppingDialog(context: Context, var addDialogListener: AddDialogListener) :
    AppCompatDialog(context) {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_shopping_item)
        findViewById<AppCompatEditText>(R.id.etDate)?.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> //Do Something
                {
                    val mYear = 0
                    val mMonth = 0
                    val mDay = 0
                    val datePickerForBday: DatePickerDialog? =
                        findViewById<AppCompatEditText>(R.id.etDate)?.let {
                            DatePickerDialog(it?.context,
                                R.style.CustomDatePickerDialogTheme,
                                 { _, year, month, dayOfMonth ->
                                    var monthStr = ""
                                    monthStr = if (month + 1 < 10) {
                                        "0" + (month + 1).toString()
                                    } else {
                                        (month + 1).toString()
                                    }
                                    var dayStr = ""
                                    dayStr = if (dayOfMonth < 10) {
                                        "0$dayOfMonth"
                                    } else {
                                        dayOfMonth.toString()
                                    }
                                    (findViewById<AppCompatEditText>(R.id.etDate))?.setText("$dayStr/$monthStr/$year")
                                },
                                mYear,
                                mMonth,
                                mDay)
                        }
                    val c = Calendar.getInstance() as Calendar
                    val year = c.get(Calendar.YEAR)
                    val month = c.get(Calendar.MONTH)
                    val day = c.get(Calendar.DAY_OF_MONTH)
                    val currentDate = System.currentTimeMillis()
                    datePickerForBday?.datePicker?.maxDate = currentDate
                    datePickerForBday?.datePicker?.init(year, month, day, datePickerForBday)
                    datePickerForBday?.datePicker?.spinnersShown = true
                    datePickerForBday?.datePicker?.calendarViewShown = false
                    datePickerForBday?.show()
                    datePickerForBday?.setOnCancelListener { dialog ->
                        datePickerForBday.dismiss()
                        try {
                            val imm =
                                (findViewById<AppCompatEditText>(R.id.etDate))?.context?.getSystemService(
                                    Service.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow((findViewById<AppCompatEditText>(R.id.etDate))?.windowToken,
                                0)
                        } catch (e: Exception) {
                        }
                    }
                }

            }
            true
        }

        findViewById<TextView>(R.id.tvAdd)?.setOnClickListener {
            val name =  findViewById<TextView>(R.id.etName)?.text.toString()
            val amount =  findViewById<TextView>(R.id.etDate)?.text.toString()
            if(name.isNullOrEmpty()) {
                Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val item = ShoppingItem(name, amount)
            addDialogListener.onAddButtonClicked(item)
            dismiss()
        }

        findViewById<TextView>(R.id.tvCancel)?.setOnClickListener {
            cancel()
        }
    }
}