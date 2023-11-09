package com.example.ajaibprotect

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class PermissionListAdapter(private val context: Context, private val permissions: List<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return permissions.size
    }

    override fun getItem(position: Int): Any {
        return permissions[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val permission = getItem(position) as String
        viewHolder.permissionTextView.text = permission

        return view
    }

    private class ViewHolder(view: View) {
        val permissionTextView: TextView = view.findViewById(android.R.id.text1)
    }
}
