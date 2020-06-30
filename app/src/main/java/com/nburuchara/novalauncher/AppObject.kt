package com.nburuchara.novalauncher

import android.graphics.drawable.Drawable

class AppObject (private var name : String, private var packageName : String, private var img : Drawable, private var isAppInDrawer : Boolean){

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

    fun getIsAppInDrawer () : Boolean {
        return isAppInDrawer
    }

        // Setter Methods
    fun setPackageName (pName: String?) {
        if (pName != null) {
            packageName = pName
        }
    }

    fun setName (appName : String?) {
        if (appName != null) {
            name = appName
        }
    }

    fun setImage (ourImage : Drawable?) {
        if (ourImage != null) {
            img = ourImage
        }
    }

    fun setIsAppInDrawer (appInDrawer : Boolean) {
        isAppInDrawer = appInDrawer
    }
}