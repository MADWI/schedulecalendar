package pl.edu.zut.mad.schedule

import android.util.Log
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import io.reactivex.Observable
import org.joda.time.LocalDate
import pl.edu.zut.mad.schedule.data.ScheduleRepository
import pl.edu.zut.mad.schedule.data.model.ui.EmptyDay
import pl.edu.zut.mad.schedule.data.model.ui.LessonEvent
import pl.edu.zut.mad.schedule.util.NetworkConnection
import java.util.*
import kotlin.collections.ArrayList
import pl.edu.zut.mad.schedule.data.model.db.Day as DayDb
import pl.edu.zut.mad.schedule.data.model.ui.Day as DayUi


class SchedulePresenter(private val repository: ScheduleRepository, private val user: User,
                        private val view: ScheduleMvp.View, private val networkConnection: NetworkConnection)
    : ScheduleMvp.Presenter {

    override fun logout() {
        user.delete()
        repository.delete()
        view.showLoginView()
    }

    override fun refresh() {
        if (networkConnection.isAvailable()) {
            view.showLoginView()
        } else {
            view.showError()
        }
    }

    override fun loadData() {
        if (user.isSaved()) {
            loadLessons()
        } else {
            view.showLoginView()
        }
    }

    private fun loadLessons() {
        val minDate = repository.getScheduleMinDate()
        val maxDate = repository.getScheduleMaxDate()

        val maxDateCal = dateToCalendar(minDate)
        val minDateCal = dateToCalendar(maxDate)
        view.onDateIntervalCalculated(maxDateCal, minDateCal)

        val events: MutableList<CalendarEvent> = ArrayList()
        val dateDates = getCalendarDates(minDate, maxDate)
        Observable.fromIterable(dateDates)
                .flatMap { repository.getLessonsForDay(it) }
                .subscribe(
                        {
                            when(it) {
                                is DayUi -> events.addAll(mapToLessonsEvents(it))
                                is EmptyDay -> events.add(LessonEvent(it.date, null))
                            }
                        },
                        { Log.d(this.javaClass.simpleName, "error: ${it.message}") }, // TODO: print error to UI?
                        { view.onLessonsEventLoad(events) }
                )
    }

    private fun mapToLessonsEvents(day: DayUi): List<LessonEvent> = // TODO: move to mapper
            day.lessons.map { LessonEvent(day.date, it) }.toList()

    private fun dateToCalendar(date: LocalDate) =
            date.toDateTimeAtStartOfDay().toCalendar(Locale.getDefault())

    private fun getCalendarDates(minDate: LocalDate, maxDate: LocalDate): MutableList<LocalDate> {
        var nextDay = minDate
        val dateDates: MutableList<LocalDate> = ArrayList()
        while (!nextDay.isEqual(maxDate)) {
            dateDates.add(nextDay)
            nextDay = nextDay.plusDays(1)
        }
        return dateDates
    }
}
