package com.example.ajaibprotect.adapters

import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.ajaibprotect.R

// Kelas adapter untuk menampilkan daftar aplikasi
class AppListAdapter(private val appsList: List<ResolveInfo>, private val packageManager: PackageManager) :
    BaseAdapter() {

    // Mengembalikan jumlah item dalam daftar aplikasi
    override fun getCount(): Int {
        return appsList.size
    }

    // Mengembalikan objek aplikasi pada posisi tertentu
    override fun getItem(position: Int): Any {
        return appsList[position]
    }

    // Mengembalikan ID item pada posisi tertentu
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // Digunakan untuk mengatur tampilan item pada posisi tertentu di dalam ListView
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val holder: ViewHolder

        if (view == null) {
            // Jika convertView null, buat tampilan baru dan deklarasikan holder
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_app, parent, false)
            holder = ViewHolder()
            holder.appIconImageView = view.findViewById(R.id.appIconImageView)
            holder.appNameTextView = view.findViewById(R.id.appNameTextView)
            view.tag = holder
        } else {
            // Jika convertView tidak null, dapatkan holder dari tag
            holder = view.tag as ViewHolder
        }

        // Ambil informasi aplikasi dari appsList pada posisi tertentu
        val appInfo = appsList[position]

        // Atur ikon aplikasi pada ImageView
        holder.appIconImageView?.setImageDrawable(appInfo.loadIcon(packageManager))

        // Atur teks label aplikasi pada TextView
        holder.appNameTextView?.text = appInfo.loadLabel(packageManager).toString()

        // Hasil tampilan dikembalikan
        return view!!
    }

    // Inner class untuk menyimpan referensi ke elemen-elemen tampilan
    private class ViewHolder {
        var appIconImageView: ImageView? = null
        var appNameTextView: TextView? = null
    }
}
