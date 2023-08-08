package com.example.ajaibprotect.adapters

import android.content.Context
import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.ajaibprotect.R

class SystemListAdapter(context: Context, private val appList: List<ApplicationInfo>) :
    ArrayAdapter<ApplicationInfo>(context, R.layout.item_system, appList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.item_system, parent, false)

        val appNameTextView = rowView.findViewById(R.id.appNameTextView) as TextView
        val appIconImageView = rowView.findViewById(R.id.appIconImageView) as ImageView

        val appInfo = appList[position]
        appNameTextView.text = appInfo.loadLabel(context.packageManager)
        appIconImageView.setImageDrawable(appInfo.loadIcon(context.packageManager))

        return rowView
    }
}
