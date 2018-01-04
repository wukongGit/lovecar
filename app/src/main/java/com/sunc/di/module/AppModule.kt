package com.sunc.di.module

import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Created by Administrator on 2017/11/23.
 */
@Module
class AppModule(private val context: Context){
    @Provides
    fun provideContext() = context
}