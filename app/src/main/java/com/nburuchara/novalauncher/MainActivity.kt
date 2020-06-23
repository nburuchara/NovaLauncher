package com.nburuchara.novalauncher

import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.GridView
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeDrawer()
    }

    private lateinit var installedAppList : ArrayList <AppObject>
    val appList : ArrayList<AppObject> = ArrayList()

    private fun initializeDrawer() {
        var mBottomSheet : View = findViewById<View>(R.id.bottomSheet)
        val mDrawerGridView : GridView = findViewById<GridView>(R.id.drawerGrid)
        val mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet)
        mBottomSheetBehavior.isHideable = false
        mBottomSheetBehavior.peekHeight = 300

        installedAppList = getInstalledAppList()

        val adapter = AppAdapter(applicationContext, installedAppList)
        mDrawerGridView.adapter = adapter

        mBottomSheetBehavior.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
                TODO("Not yet implemented")
            }

            override fun onStateChanged(p0: View, p1: Int) {
                if (p1 == BottomSheetBehavior.STATE_HIDDEN && mDrawerGridView.getChildAt(0).y.toInt() != 0) {
                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
                if (p1 == BottomSheetBehavior.STATE_DRAGGING && mDrawerGridView.getChildAt(0).y.toInt() != 0) {
                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        })

//         - - FOR TESTING PURPOSES (TUTORIAL 2) - - //
//           for (i in 1..20) {
//            appList.add(AppObject("LMTtest", " ", resources.getDrawable(R.drawable.ic_launcher_foreground)))
//        }

    }

    private fun getInstalledAppList(): ArrayList<AppObject> {
        val list : ArrayList<AppObject> = ArrayList()

        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)
        val untreatedApps : ArrayList<ResolveInfo> =
            applicationContext.packageManager.queryIntentActivities(i,0) as ArrayList<ResolveInfo>

        for (untreatedApp in untreatedApps) {
            val appName = untreatedApp.activityInfo.loadLabel(packageManager).toString()
            val appPackageName = untreatedApp.activityInfo.packageName
            val appImage = untreatedApp.activityInfo.loadIcon(packageManager)

            val app : AppObject = AppObject(appName, appPackageName, appImage)
            if (!list.contains(app)) {
                list.add(app)
            }
        }

        return list
    }

}

