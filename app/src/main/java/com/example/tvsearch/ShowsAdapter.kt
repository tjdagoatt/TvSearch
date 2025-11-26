package com.example.tvsearch

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ShowsAdapter(
    private val shows: MutableList<Show>
) : RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    inner class ShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPoster: ImageView = itemView.findViewById(R.id.ivPoster)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvRating: TextView = itemView.findViewById(R.id.tvRating)
        val tvSummary: TextView = itemView.findViewById(R.id.tvSummary)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_show, parent, false)
        return ShowViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        val show = shows[position]

        holder.tvTitle.text = show.name

        val ratingText = show.rating?.average?.toString() ?: "N/A"
        holder.tvRating.text = "Rating: $ratingText"

        val plainSummary = show.summary?.let {
            Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY).toString()
        } ?: "No summary available."

        holder.tvSummary.text = plainSummary

        val posterUrl = show.image?.medium
        Glide.with(holder.itemView.context)
            .load(posterUrl)
            .into(holder.ivPoster)

        holder.itemView.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Clicked: ${show.name}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int = shows.size

    fun updateData(newShows: List<Show>) {
        shows.clear()
        shows.addAll(newShows)
        notifyDataSetChanged()
    }
}
