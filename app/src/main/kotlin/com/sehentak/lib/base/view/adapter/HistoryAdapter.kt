package com.sehentak.lib.base.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sehentak.base.helper.setTextHtml
import com.sehentak.lib.base.R
import com.sehentak.lib.base.model.VersionMdl

class HistoryAdapter(
    private val data: List<VersionMdl>
): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val viewVersion: TextView = view.findViewById(R.id.history_tv_version)
        val viewDate: TextView = view.findViewById(R.id.history_tv_date)
        val viewAuthor: TextView = view.findViewById(R.id.history_tv_author)
        val viewDescription: TextView = view.findViewById(R.id.history_tv_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val content = data[position]

        var version = content.name
        if (!version.contains("version", true)) {
            version = "Version $version"
        }
        holder.viewVersion.setTextHtml(version)

        var date = content.commit
        date = if (date == null) content.date
        else "$date - ${content.date}"
        holder.viewDate.text = date

        var author = content.author
        if (content.email != null) {
            author = "$author - ${content.email}"
        }
        holder.viewAuthor.text = author

        val description = content.description
        if (description != null) {
            holder.viewDescription.setTextHtml(content.description)
        } else holder.viewDescription.visibility = View.GONE
    }
}