package com.sunc.utils

import com.sunc.car.lovecar.App

import io.paperdb.Book
import io.paperdb.Paper

object DBUtils {
    private val DEFAULT_DB_NAME = "LoveCarDB"
    private var BOOK: Book? = null

    init {
        Paper.init(App.instance)
        create(DEFAULT_DB_NAME)
    }

    val allKeys: List<String>?
        get() {
            return try {
                BOOK!!.allKeys
            } catch (e: Throwable) {
                null
            }
        }

    fun <T> write(key: String, value: T): Book? {
        return try {
            BOOK!!.write(key, value)
        } catch (e: Throwable) {
            BOOK
        }

    }

    fun <T> read(key: String): T? {
        return read<T>(key, null)
    }

    fun <T> read(key: String, defaultValue: T?): T? {
        return try {
            BOOK!!.read<T>(key, defaultValue)
        } catch (e: Throwable) {
            defaultValue
        }
    }

    fun delete(key: String) {
        try {
            BOOK!!.delete(key)
        } catch (e: Throwable) {
        }

    }

    fun exist(key: String): Boolean {
        return try {
            BOOK!!.exist(key)
        } catch (e: Throwable) {
            false
        }

    }

    fun destroy() {
        try {
            BOOK!!.destroy()
        } catch (e: Throwable) {
        }

    }

    fun create(name: String) {
        BOOK = Paper.book(name)
    }
}
