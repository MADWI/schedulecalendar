package pl.edu.zut.mad.schedule.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import org.joda.time.LocalDate
import pl.edu.zut.mad.schedule.R

class CalendarMonthView(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    init {
        LayoutInflater.from(context).inflate(R.layout.calendar_month, this, true)
        orientation = LinearLayout.VERTICAL
        setupMonthForDate(LocalDate.now())
    }

    private fun setupMonthForDate(date: LocalDate) {
        val firstDayOfMonth = LocalDate(date.year, date.monthOfYear, 1)
        var currentDate = firstDayOfMonth.minusDays(firstDayOfMonth.dayOfWeek - 1)
        iterateOverDays {
            it.text = currentDate.dayOfMonth.toString()
            currentDate = currentDate.plusDays(1)
        }
    }

    private fun iterateOverDays(onDay: (TextView) -> Unit) {
        repeat(childCount) { weekIndex ->
            val weekView = getChildAt(weekIndex) as ViewGroup
            repeat(weekView.childCount) { dayIndex ->
                val dayView = weekView.getChildAt(dayIndex) as TextView
                onDay(dayView)
            }
        }
    }
}
