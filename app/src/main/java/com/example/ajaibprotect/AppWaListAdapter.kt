package com.example.ajaibprotect

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import java.io.File

class AppWaListAdapter(context: Context, private val appsList: List<File>) :
    ArrayAdapter<File>(context, R.layout.item_app_list, appsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val app = appsList[position]
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_app_list, parent, false)

        val appIconImageView: ImageView = view.findViewById(R.id.appIconImageView)
        val appNameTextView: TextView = view.findViewById(R.id.appNameTextView)

        // Set app icon (you can customize this)
        appIconImageView.setImageResource(R.mipmap.ic_launcher)

        // Set app name (you can customize this)
        appNameTextView.text = app.nameWithoutExtension

        return view
    }
}
