package com.nburuchara.novalauncher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView


class AppAdapter(
    private val context: Context, val appList: ArrayList<AppObject>, private val cellHeight: Int) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?,  parent: ViewGroup?): View {
        val v : View
        if (convertView == null) {
            var inflater : LayoutInflater = this@AppAdapter.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = inflater.inflate(R.layout.item_app, parent, false)
        } else {
            v = convertView
        }
        val mImage : ImageView = v.findViewById(R.id.myImage)
        val mLabel : TextView = v.findViewById(R.id.myLabel)

        val mLayout : LinearLayout = v.findViewById<LinearLayout>(R.id.layout)
        val lp : LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, cellHeight)

        mLayout.layoutParams = lp
        mImage.setImageDrawable(appList.get(position).getImage())
        mLabel.setText(appList.get(position).getName())

        mLayout.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                (context as MainActivity).itemPress(appList.get(position))
            }
        })

        mLayout.setOnLongClickListener(object: View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                (context as MainActivity).itemLongPress(appList.get(position))
                return true
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