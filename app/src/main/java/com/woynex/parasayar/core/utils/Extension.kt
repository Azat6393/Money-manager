package com.woynex.parasayar.core.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar


fun View.showSnackBar(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()
}