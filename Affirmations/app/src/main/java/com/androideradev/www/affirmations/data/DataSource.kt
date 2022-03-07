package com.androideradev.www.affirmations.data

import android.content.Context
import com.androideradev.www.affirmations.R
import com.androideradev.www.affirmations.model.Affirmation

object DataSource {

    fun loadAffirmations(context: Context) = context
        .resources
        .getStringArray(R.array.affirmations)
        .map { Affirmation(it) }


}