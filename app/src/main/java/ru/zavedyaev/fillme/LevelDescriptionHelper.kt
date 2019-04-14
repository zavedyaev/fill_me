package ru.zavedyaev.fillme

import android.content.res.Resources
import android.widget.TextView
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import ru.zavedyaev.fillme.level.WinConditions

object LevelDescriptionHelper {
    fun fillDescription(
        findMaterialRatingBarById: (id: Int) -> MaterialRatingBar,
        findTextViewById: (id: Int) -> TextView, resources: Resources,
        starsCount: Int, winConditions: WinConditions
    ) {
        val levelRating1 = findMaterialRatingBarById(R.id.levelRating1)
        if (starsCount > 0) {
            levelRating1.rating = 1f
        } else {
            levelRating1.rating = 0f
        }

        val levelRating2 = findMaterialRatingBarById(R.id.levelRating2)
        if (starsCount > 1) {
            levelRating2.rating = 1f
        } else {
            levelRating2.rating = 0f
        }

        val levelRating3 = findMaterialRatingBarById(R.id.levelRating3)
        if (starsCount > 2) {
            levelRating3.rating = 1f
        } else {
            levelRating3.rating = 0f
        }

        findTextViewById(R.id.levelRating1MinSquare).text =
                resources.getString(R.string.min_percentage, (winConditions.oneStar.minSquare * 100).toInt())
        findTextViewById(R.id.levelRating2MinSquare).text =
                resources.getString(R.string.min_percentage, (winConditions.twoStars.minSquare * 100).toInt())
        findTextViewById(R.id.levelRating3MinSquare).text =
                resources.getString(
                    R.string.min_percentage,
                    (winConditions.threeStars.minSquare * 100).toInt()
                )

        findTextViewById(R.id.levelRating1MaxCircles).text =
                resources.getString(R.string.max_circles_count, winConditions.oneStar.maxCirclesCount)
        findTextViewById(R.id.levelRating2MaxCircles).text =
                resources.getString(R.string.max_circles_count, winConditions.twoStars.maxCirclesCount)
        findTextViewById(R.id.levelRating3MaxCircles).text =
                resources.getString(R.string.max_circles_count, winConditions.threeStars.maxCirclesCount)
    }
}