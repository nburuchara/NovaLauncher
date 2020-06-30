package com.nburuchara.novalauncher
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.viewpager.widget.PagerAdapter

class ViewPagerAdapter (private val context: Context, private val pagerAppList: ArrayList<PagerObject>, private val cellHeight : Int , private val numColumn : Int) : PagerAdapter () {

    /**
     * ViewPageAdapter:
     *
     * POSITION: Refers to the page (of apps) you are currently looking at on your screen
     *
     * **/
    val appAdapterList : ArrayList<AppAdapter> = ArrayList()
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val layout : ViewGroup = inflater.inflate(R.layout.pager_layout, container, false) as ViewGroup

        val mGridView : GridView = layout.findViewById<GridView>(R.id.grid)
        mGridView.numColumns = numColumn
        val mGridAdapter : AppAdapter = AppAdapter(context, pagerAppList.get(position).getAppList(), cellHeight)
        mGridView.adapter = mGridAdapter

        appAdapterList.add(mGridAdapter)

        container.addView(layout)
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        return container.removeView(`object` as View?)
//        super.destroyItem(container, position, `object`)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return pagerAppList.size
    }

    public fun notifyGridChanged () {
        for (i in 0 until appAdapterList.size){
            appAdapterList.get(i).notifyDataSetChanged()
        }
    }
}