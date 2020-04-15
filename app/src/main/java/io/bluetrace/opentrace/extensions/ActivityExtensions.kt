package io.bluetrace.opentrace.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> AppCompatActivity.observe(liveData: LiveData<T>, handler: (T) -> Unit) {
    liveData.observe(this, Observer { handler(it) })
}