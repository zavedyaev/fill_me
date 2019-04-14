package ru.zavedyaev.fillme

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import ru.zavedyaev.fillme.level.GameLevel
import ru.zavedyaev.fillme.level.LevelsPacks
import ru.zavedyaev.fillme.level.ProgressInstance


class LevelsAdapter(
    private val activity: LevelSelectActivity,
    private val levels: List<GameLevel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val expandedLevels = HashSet<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val viewTypeEnum = ViewType.getById(viewType)
        val view = when (viewTypeEnum) {
            ViewType.UNLOCKED -> LayoutInflater.from(context).inflate(R.layout.level_card_layout, parent, false)
            ViewType.LOCKED -> LayoutInflater.from(context).inflate(R.layout.locked_level_card_layout, parent, false)
        }
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun getItemViewType(position: Int): Int {
        val starsCount = ProgressInstance.progress.getStarsCount()
        //todo think about level packs
        val levelPack = 0
        val level = LevelsPacks.packs[levelPack]!!.levels[position]!!
        return if (level.starsToUnlock <= starsCount) {
            ViewType.UNLOCKED.id
        } else {
            ViewType.LOCKED.id
        }
    }

    override fun getItemCount() = levels.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        val viewTypeEnum = ViewType.getById(viewType)
        //todo think about level packs
        val levelPack = 0
        val level = LevelsPacks.packs[levelPack]!!.levels[position]!!
        when (viewTypeEnum) {
            ViewType.UNLOCKED -> {
                //todo level icon
                //holder.itemView.findViewById(R.id.levelIcon)
                val rating =
                    ProgressInstance.progress.levelPackProgress[levelPack]!!.levelProgress[position]?.starsCount ?: 0
                holder.itemView.findViewById<MaterialRatingBar>(R.id.levelRating).rating = rating.toFloat()
                holder.itemView.setOnClickListener {
                    val i = Intent(activity, GameActivity::class.java)
                    i.putExtra(GameActivity.LEVEL_PACK_ID_EXTRA_NAME, levelPack)
                    i.putExtra(GameActivity.LEVEL_ID_EXTRA_NAME, position)
                    activity.startActivity(i)
                }

                val expandMoreButton = holder.itemView.findViewById<ImageButton>(R.id.expandMoreButton)
                val expandLessButton = holder.itemView.findViewById<ImageButton>(R.id.expandLessButton)
                val levelDescription = holder.itemView.findViewById<View>(R.id.levelDescription)

                if (expandedLevels.contains(position)) {
                    expandMoreButton.visibility = View.GONE
                    expandLessButton.visibility = View.VISIBLE
                    levelDescription.visibility = View.VISIBLE
                } else {
                    expandMoreButton.visibility = View.VISIBLE
                    expandLessButton.visibility = View.GONE
                    levelDescription.visibility = View.GONE
                }

                expandMoreButton.setOnClickListener {
                    expandedLevels.add(position)
                    expandMoreButton.visibility = View.GONE
                    expandLessButton.visibility = View.VISIBLE

                    AnimationHelper.expand(levelDescription)
                }

                expandLessButton.setOnClickListener {
                    expandedLevels.remove(position)
                    expandMoreButton.visibility = View.VISIBLE
                    expandLessButton.visibility = View.GONE

                    AnimationHelper.collapse(levelDescription)
                }

                LevelDescriptionHelper.fillDescription(
                    { id: Int -> holder.itemView.findViewById(id) },
                    { id: Int -> holder.itemView.findViewById(id) },
                    holder.itemView.resources,
                    rating,
                    level.winConditions
                )
            }
            ViewType.LOCKED -> {
                val starsCount = ProgressInstance.progress.getStarsCount()
                holder.itemView.findViewById<TextView>(R.id.starsRequired).text =
                        holder.itemView.resources.getString(R.string.stars_required, starsCount, level.starsToUnlock)
            }
        }

        holder.itemView.findViewById<TextView>(R.id.levelName).text =
                holder.itemView.resources.getString(R.string.level_name, position)
    }
}

enum class ViewType(val id: Int) {
    LOCKED(0), UNLOCKED(1);

    companion object {
        fun getById(id: Int): ViewType {
            return enumValues<ViewType>().find { it.id == id }
                ?: throw IllegalArgumentException("Cannot find ViewType enum by id: $id")
        }
    }
}