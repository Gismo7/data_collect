package net.test.android.datacollection.di

import net.test.android.datacollection.AbstractDataCollectManager

interface DataCollectDependencies {

    fun provideDataCollectManager(): AbstractDataCollectManager
}