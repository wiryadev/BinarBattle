package com.wiryadev.binarbattle

import android.view.View
import com.google.android.material.snackbar.Snackbar


fun View.showSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}