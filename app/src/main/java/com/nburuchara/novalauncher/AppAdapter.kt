package com.nburuchara.novalauncher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView


class AppAdapter(private val context: Context, private var appList: ArrayList<AppObject>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v : View
        if (convertView == null) {
            var inflater : LayoutInflater = this@AppAdapter.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = inflater.inflate(R.layout.item_app, parent, false)
        } else {
            v = convertView
        }
        val mImage : ImageView = v.findViewById(R.id.myImage)
        val mLabel : TextView = v.findViewById(R.id.myLabel)

        val mLayout : LinearLayout = v.findViewById(R.id.layout)
        mImage.setImageDrawable(appList.get(position).getImage())
        mLabel.setText(appList.get(position).getName())

        mLayout.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                val launchAppIntent  = context.packageManager.getLaunchIntentForPackage(appList.get(position).getPackageName())
                if (launchAppIntent != null) {
                    context.startActivity(launchAppIntent)
                }
            }
        })
        return v
    }

    override fun getItem(position: Int): Any {
        return appList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return appList.size
    }


}