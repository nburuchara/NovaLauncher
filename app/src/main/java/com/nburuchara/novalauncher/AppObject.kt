package com.nburuchara.novalauncher

import android.graphics.drawable.Drawable

class AppObject (private val name : String, private val packageName : String, private val img : Drawable){

    public
    // Getter Methods
    fun getName (): String {
        return name;
    }

    fun getPackageName (): String {
        return packageName
    }

    fun getImage (): Drawable {
        return img
    }
}