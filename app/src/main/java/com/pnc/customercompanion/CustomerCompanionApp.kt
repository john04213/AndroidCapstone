package com.pnc.customercompanion

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Starting point for Hilt. Registered in AndroidManifest.xml as android:name.
@HiltAndroidApp
class CustomerCompanionApp : Application()
