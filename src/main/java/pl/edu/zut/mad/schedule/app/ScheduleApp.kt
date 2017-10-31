package pl.edu.zut.mad.schedule.app

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import io.realm.Realm
import net.danlew.android.joda.JodaTimeAndroid


open class ScheduleApp : Application() {

    val component: ScheduleAppComponent by lazy {
        DaggerScheduleAppComponent
                .builder()
                .scheduleAppModule(ScheduleAppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        Realm.init(this)
        JodaTimeAndroid.init(this)
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this)
        }
    }
}
