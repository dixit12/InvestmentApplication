package com.dixitgarg.investmentapplication.basicadapterdelegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class AdapterDelegateModule {
    abstract fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View
    abstract fun cleanUp()
}