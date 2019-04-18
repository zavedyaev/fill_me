package ru.zavedyaev.fillme

import android.app.Application
import ru.zavedyaev.fillme.level.ProgressInstance

@Suppress("unused") //used in manifest
class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        ProgressInstance.init(this)
    }
}