package ru.zavedyaev.fillme

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import ru.zavedyaev.fillme.level.GameLevel
import ru.zavedyaev.fillme.level.ProgressInstance

class LevelsAdapter(
    val activity: LevelSelectActivity,
    val levels: List<GameLevel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.level_card_layout, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun getItemCount() = levels.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val level = levels[position]
        //holder.itemView.findViewById(R.id.levelIcon)
        holder.itemView.findViewById<TextView>(R.id.levelName).text =
                holder.itemView.resources.getString(R.string.level_name, position)
        //todo think about level packs
        val levelPack = 0
        val rating = ProgressInstance.progress.levelPackProgress[levelPack]!!.levelProgress[position]?.starsCount ?: 0
        //todo add default rating
        holder.itemView.findViewById<MaterialRatingBar>(R.id.levelRating).rating = rating.toFloat()
        holder.itemView.setOnClickListener {
            val i = Intent(activity, GameActivity::class.java)
            i.putExtra(GameActivity.LEVEL_PACK_ID_EXTRA_NAME, levelPack)
            i.putExtra(GameActivity.LEVEL_ID_EXTRA_NAME, position)
            activity.startActivity(i)
        }
    }

}