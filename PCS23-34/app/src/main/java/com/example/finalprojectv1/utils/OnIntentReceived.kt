package com.example.finalprojectv1.utils

import android.content.Intent
import com.example.finalprojectv1.activities.tripAdapter

interface OnIntentReceived {
    fun onIntent(lang: String, long: String, holder: tripAdapter.MyViewHolder)
}