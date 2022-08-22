package com.example.AlarmDate

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.AlarmDate.data.ShoppingItem
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit
import java.util.*


class ShoppingItemAdapter(
    var items: List<ShoppingItem>,
    private val viewModel: ShoppingViewModel,
    var alarmDateActivity: AlarmDateActivity,
): RecyclerView.Adapter<ShoppingItemAdapter.ShoppingViewHolder>() {
    var birthdateCurrentYear: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shopping_item, parent, false)
        return ShoppingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val curShoppingItem = items[position]
        holder.itemView.findViewById<TextView>(R.id.tvName).text =curShoppingItem.name
        holder.itemView.findViewById<TextView>(R.id.tvAmount).text = "${curShoppingItem.amount}"

        holder.itemView.findViewById<ImageView>(R.id.ivDelete).setOnClickListener {
            viewModel.delete(curShoppingItem)
        }
        val userDob: Date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("dd/M/YYYY").parse("${curShoppingItem.amount}")
        }  else {
            SimpleDateFormat("yyyy-M-dd").parse("${curShoppingItem.amount}")
        }


        val today = Date()
        val diff = today.time - userDob.time
        val numOfDays = (diff / (1000 * 60 * 60 * 24)).toInt()
        val age = numOfDays/365

        val dateArray = curShoppingItem.amount?.split("/")
        val y: Int? = dateArray?.get(0)?.toInt()
        val mon:Int? = dateArray?.get(1)?.toInt()
        val day:Int? = dateArray?.get(2)?.toInt()

        val todayD: LocalDate? = LocalDate.now()
        val birthday: LocalDate? = mon?.let { day?.let { it1 -> y?.let { it2 ->
            LocalDate.of(it1, it,
                it2)
        } } }

        var nextBDay: LocalDate? = todayD?.let { birthday?.withYear(it.year) }

        if (nextBDay?.isBefore(todayD) == true || nextBDay?.isEqual(todayD) == true) {
            nextBDay = nextBDay.plusYears(1)
        }

        val p: Period = Period.between(todayD, nextBDay)
        val p2: Long = ChronoUnit.DAYS.between(todayD, nextBDay)


        holder.itemView.findViewById<TextView>(R.id.tvAge).text = "Turns $age years"
        holder.itemView.findViewById<TextView>(R.id.daysToGo).text =
            "$p2 days to Go!"
         val animShake = AnimationUtils.loadAnimation(holder.itemView.findViewById<TextView>(R.id.daysToGo).context, R.anim.shake_anim);
        holder.itemView.findViewById<AppCompatImageView>(R.id.celeb).startAnimation(animShake)

        holder.itemView.findViewById<SwitchCompat>(R.id.reminder).setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) alarmDateActivity.openCalendar(curShoppingItem, nextBDay)
        }


    }




    inner class ShoppingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
       // this line

        
    }

}