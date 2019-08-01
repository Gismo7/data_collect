package net.test.android.datacollection.di

import io.reactivex.Scheduler


interface BaseSchedulerProvider {

    fun computation(): Scheduler

    fun io(): Scheduler

    fun ui(): Scheduler
}