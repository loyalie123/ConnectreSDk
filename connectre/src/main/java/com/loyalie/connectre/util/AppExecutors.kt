package com.loyalie.connectre.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors private constructor(val diskIo: Executor, val mainThread: Executor) {

    companion object {

        @Volatile
        private var INSTANCE: AppExecutors? = null

        fun getInstance(): AppExecutors =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppExecutors(
                    Executors.newSingleThreadExecutor(),
                    MainThreadExecutor()
                ).also { INSTANCE = it }
            }

        private class MainThreadExecutor : Executor {
            private val mainThreadHandler = Handler(Looper.getMainLooper())

            override fun execute(command: Runnable) {
                mainThreadHandler.post(command)
            }
        }

    }


}