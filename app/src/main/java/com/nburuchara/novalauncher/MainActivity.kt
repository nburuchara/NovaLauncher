package com.nburuchara.novalauncher

import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Point
import android.os.Bundle
import android.view.PointerIcon
import android.view.View
import android.view.WindowManager
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {

    private lateinit var vPager : ViewPager
    private var cellHeight : Int = 0
    val NUMBER_OF_ROWS : Int = 5
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeHome()
        initializeDrawer()
    }

    private fun initializeHome() {
        val pagerAppList : ArrayList<PagerObject> = ArrayList()
        val appList : ArrayList<AppObject> = ArrayList()

        for (i in 1..20) {
            appList.add(AppObject("", "", resources.getDrawable(R.drawable.ic_launcher_foreground)))
        }

        pagerAppList.add(PagerObject(appList))
        pagerAppList.add(PagerObject(appList))
        pagerAppList.add(PagerObject(appList))

        cellHeight = getDisplayContentHeight() / NUMBER_OF_ROWS

        vPager = findViewById<ViewPager>(R.id.viewPager)
        val adapter = ViewPagerAdapter(this, pagerAppList)
        vPager.adapter = adapter

    }

    private lateinit var installedAppList : ArrayList <AppObject>
    val appList : ArrayList<AppObject> = ArrayList()



    private fun initializeDrawer() {
        var mBottomSheet : View = findViewById<View>(R.id.bottomSheet)
        val mDrawerGridView : GridView = findViewById<GridView>(R.id.drawerGrid)
        val mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet)
        mBottomSheetBehavior.isHideable = false
        mBottomSheetBehavior.peekHeight = 400

        installedAppList = getInstalledAppList()

        val adapter = AppAdapter(applicationContext, installedAppList)
        mDrawerGridView.adapter = adapter


        mBottomSheetBehavior.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                val grY = mDrawerGridView.y
//                if(newState == BottomSheetBehavior.STATE_COLLAPSED && mDrawerGridView.getChildAt(0).y.roundToInt() != 0)
//                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//                if(newState == BottomSheetBehavior.STATE_DRAGGING && mDrawerGridView.getChildAt(0).y.roundToInt() != 0)
//                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                if (newState == BottomSheetBehavior.STATE_HIDDEN && mDrawerGridView.getChildAt(0).y.toInt() != 0) {
                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
                if (newState == BottomSheetBehavior.STATE_DRAGGING && mDrawerGridView.getChildAt(0).y.toInt() != 0) {
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

    private fun getDisplayContentHeight(): Int {
        val windowsManager : WindowManager = windowManager
        val size : Point = Point()
        var screenHeight = 0
        var actionBarHeight = 0
        var statusBarHeight = 0

        
        return 0
    }

}

