package com.nburuchara.novalauncher

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ResolveInfo
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetBehavior


private const val TAG = "MainActivity"

class MainActivity() : AppCompatActivity() {

    private lateinit var vPager : ViewPager
    private var cellHeight : Int = 0
    val NUMBER_OF_ROWS : Int = 5
    var DRAWER_PEEK_HEIGHT : Int = 100
    var mAppDrag : AppObject? = null
    val PREFS_NAME  = "NovaPrefs"
    var numRow : Int = 0
    var numColumn : Int = 0
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        getPermissions()
        getData()

        val mTopDrawerLayout : LinearLayout = findViewById<LinearLayout>(R.id.topDrawerLayout)
        mTopDrawerLayout.post(object: Runnable {
            override fun run() {
                DRAWER_PEEK_HEIGHT = mTopDrawerLayout.height
                initializeHome()
                initializeDrawer()
            }
        })

        val mSettings : ImageButton = findViewById<ImageButton>(R.id.settings)
        mSettings.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d(TAG, "something went wrong here b4 Func")
                val i = Intent(applicationContext, SettingsActivity::class.java)
                startActivity(i)
                Log.d(TAG, "something went wrong here too aftr Func")
            }
        })
    }


    lateinit var mViewPagerAdapter : ViewPagerAdapter
    private fun initializeHome() {
        val pagerAppList : ArrayList<PagerObject> = ArrayList()

        val appList1 : ArrayList<AppObject> = ArrayList()
        val appList2 : ArrayList<AppObject> = ArrayList()
        val appList3 : ArrayList<AppObject> = ArrayList()

        for (i in 0 until (numRow*numColumn)) {
            appList1.add(AppObject("", "", resources.getDrawable(R.drawable.ic_launcher_foreground), false))
        }

        for (i in 0 until (numRow*numColumn)) {
            appList2.add(AppObject("", "", resources.getDrawable(R.drawable.ic_launcher_foreground), false))
        }

        for (i in 0 until (numRow*numColumn)) {
            appList3.add(AppObject("", "", resources.getDrawable(R.drawable.ic_launcher_foreground), false))
        }

        pagerAppList.add(PagerObject(appList1))
        pagerAppList.add(PagerObject(appList2))
        pagerAppList.add(PagerObject(appList3))

        cellHeight = (getDisplayContentHeight() - DRAWER_PEEK_HEIGHT) / numRow

        vPager = findViewById<ViewPager>(R.id.viewPager)

        mViewPagerAdapter = ViewPagerAdapter(this, pagerAppList, cellHeight, numColumn)
        vPager.adapter = mViewPagerAdapter

    }

    private lateinit var installedAppList : ArrayList <AppObject>
    val appList : ArrayList<AppObject> = ArrayList()


    lateinit var mDrawerGridView : GridView
    lateinit var mBottomSheet : View
    lateinit var mBottomSheetBehavior : BottomSheetBehavior<View>
    private fun initializeDrawer() {
        mDrawerGridView = findViewById<GridView>(R.id.drawerGrid)
        mBottomSheet = findViewById<View>(R.id.bottomSheet)
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet)
        mBottomSheetBehavior.isHideable = false
        mBottomSheetBehavior.peekHeight = DRAWER_PEEK_HEIGHT

        installedAppList = getInstalledAppList()

        val adapter = AppAdapter(this, installedAppList, cellHeight)
        mDrawerGridView.adapter = adapter

        mBottomSheetBehavior.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }
            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                var mAppDrag : AppObject? = null
                if (mAppDrag != null) {
                    return
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED && mDrawerGridView.getChildAt(0).y.toInt() != 0) {
                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
                if (newState == BottomSheetBehavior.STATE_DRAGGING && mDrawerGridView.getChildAt(0).y.toInt() != 0) {
                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        })
    }


    public fun itemPress (app : AppObject) {
        if (mAppDrag != null && !app.getName().equals("")) {
            Toast.makeText(this, "Cell already in use", Toast.LENGTH_SHORT).show()
            return
        }
        if (mAppDrag != null && !app.getIsAppInDrawer()) {
            app.setPackageName(mAppDrag?.getPackageName())
            app.setName(mAppDrag?.getName())
            app.setImage(mAppDrag?.getImage())

            if (!mAppDrag!!.getIsAppInDrawer()) {
                mAppDrag!!.setPackageName("")
                mAppDrag!!.setName("")
                mAppDrag!!.setImage(resources.getDrawable(R.drawable.ic_launcher_foreground))
                mAppDrag!!.setIsAppInDrawer(false)
            }
            mAppDrag = null
            mViewPagerAdapter.notifyGridChanged()
            return
        } else{
            val launchAppIntent  = applicationContext.packageManager.getLaunchIntentForPackage(app.getPackageName())
            if (launchAppIntent != null) {
                applicationContext.startActivity(launchAppIntent)
            }
        }
    }

    public fun itemLongPress (app : AppObject) {
        collapseDrawer()
        mAppDrag = app
        Log.d(TAG, mAppDrag?.getName().toString())
        Log.d(TAG, mAppDrag?.getPackageName().toString())
    }

    private fun collapseDrawer() {
        mDrawerGridView.y = DRAWER_PEEK_HEIGHT.toFloat()
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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

            val app : AppObject = AppObject(appName, appPackageName, appImage, true)
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

        if (actionBar != null) {
            actionBarHeight = actionBar?.height!!
        }

        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")

        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }

        val contentTop = findViewById<View>(android.R.id.content).top
        windowsManager.defaultDisplay.getSize(size)
        screenHeight = size.y

        return screenHeight - contentTop - actionBarHeight - statusBarHeight

        return 0
    }

    fun getData () {
        val mHomeScreenImage = findViewById<ImageView>(R.id.homeScreenImage)
        val sharedPreferences : SharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val imageUri = sharedPreferences.getString("imageUri", null)
        val numRow = sharedPreferences.getInt("numRow", 7)
        val numColumn = sharedPreferences.getInt("numColumn", 4)

        if (this.numColumn != numColumn || this.numRow != numRow) {
            this.numColumn = numColumn
            this.numRow = numRow
            initializeHome()
        }
        if (imageUri != null) {
            mHomeScreenImage.setImageURI(Uri.parse(imageUri))
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun getPermissions() {
        requestPermissions( Array <String> (1){android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1)
        requestPermissions( Array <String> (1){android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1)
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

}

