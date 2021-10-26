package com.github.didahdx.kyosk.ui.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Daniel Didah on 10/24/21.
 */

fun View.snackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}