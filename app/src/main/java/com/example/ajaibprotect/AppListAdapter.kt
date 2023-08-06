package com.example.ajaibprotect.adapters

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.ajaibprotect.R

class AppListAdapter(private val appsList: List<ApplicationInfo>, private val packageManager: PackageManager) :
    BaseAdapter() {

    override fun getCount(): Int {
        return appsList.size
    }

    override fun getItem(position: Int): Any {
        return appsList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val holder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_app, parent, false)
            holder = ViewHolder()
            holder.appIconImageView = view.findViewById(R.id.appIconImageView)
            holder.appNameTextView = view.findViewById(R.id.appNameTextView)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val appInfo = appsList[position]
        holder.appIconImageView?.setImageDrawable(appInfo.loadIcon(packageManager))
        holder.appNameTextView?.text = appInfo.loadLabel(packageManager).toString()

        return view!!
    }

    private class ViewHolder {
        var appIconImageView: ImageView? = null
        var appNameTextView: TextView? = null
    }
}
