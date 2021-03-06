package ru.zavedyaev.fillme.level

import android.content.Context
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ru.zavedyaev.fillme.R
import ru.zavedyaev.fillme.SharedPreferencesKey

object ProgressInstance {
    private lateinit var progress: Progress

    fun init(context: Context) {
        readProgress(context)
    }

    /**
     * Will return latest level id with < 3 stars or next available level. In case there are no levels left will return null
     */
    fun getLevelToContinue(): LevelPackAndLevelId? {
        val lastLevelWithProgressIds = progress.getLastLevelWithProgress() ?: return LevelPackAndLevelId(0, 0)

        val level = getLevelStatus(lastLevelWithProgressIds.levelPackId, lastLevelWithProgressIds.levelId)
        if (level.starsCount < 3) return lastLevelWithProgressIds

        val startCount = getStarsCount()

        val levelPack = LevelsPacks.packs.getValue(lastLevelWithProgressIds.levelPackId)
        if (levelPack.levels.containsKey(lastLevelWithProgressIds.levelId + 1)) {
            val nextLevel = levelPack.levels.getValue(lastLevelWithProgressIds.levelId + 1)
            return if (nextLevel.starsToUnlock <= startCount) {
                LevelPackAndLevelId(lastLevelWithProgressIds.levelPackId, lastLevelWithProgressIds.levelId + 1)
            } else {
                getPreviousLevelWithLessThan3Stars(
                    lastLevelWithProgressIds.levelPackId,
                    lastLevelWithProgressIds.levelId
                )
            }
        } else {
            return if (LevelsPacks.packs.containsKey(lastLevelWithProgressIds.levelPackId + 1)) {
                val nextLevelPack = LevelsPacks.packs.getValue(lastLevelWithProgressIds.levelPackId + 1)
                val nextLevel = nextLevelPack.levels.getValue(0)
                if (nextLevel.starsToUnlock <= startCount) {
                    LevelPackAndLevelId(lastLevelWithProgressIds.levelPackId + 1, 0)
                } else {
                    getPreviousLevelWithLessThan3Stars(
                        lastLevelWithProgressIds.levelPackId,
                        lastLevelWithProgressIds.levelId
                    )
                }
            } else {
                getPreviousLevelWithLessThan3Stars(
                    lastLevelWithProgressIds.levelPackId,
                    lastLevelWithProgressIds.levelId
                )
            }
        }
    }

    /**
     * Will return previous level with stars count < 3 or null if there are no such levels
     */
    private fun getPreviousLevelWithLessThan3Stars(levelPackId: Int, levelId: Int): LevelPackAndLevelId? {
        val maxLevelPackId = if (levelId == 0) levelPackId - 1 else levelPackId
        if (maxLevelPackId < 0) return null

        val startCount = getStarsCount()

        val levelPackIds = LevelsPacks.packs.keys.asSequence().sortedDescending().filter { it <= maxLevelPackId }

        var firstCheck = true
        levelPackIds.forEach { levelPackIdIter ->
            val levelIds = LevelsPacks.packs.getValue(levelPackIdIter).levels.keys.sortedDescending()
            val levelIdsFiltered = if (firstCheck && maxLevelPackId == levelPackId) {
                levelIds.filter { it < levelId }
            } else {
                levelIds
            }
            firstCheck = false

            levelIdsFiltered.forEach { levelIdIter ->
                val levelProgress = getLevelStatus(levelPackIdIter, levelIdIter)
                val level = LevelsPacks.packs.getValue(levelPackIdIter).levels.getValue(levelIdIter)
                if (levelProgress.starsCount < 3 && level.starsToUnlock <= startCount) {
                    return LevelPackAndLevelId(levelPackIdIter, levelIdIter)
                }
            }
        }

        return null
    }

    fun updateStarsCount(levelPackId: Int, levelId: Int, starsCount: Int, context: Context): LevelStatus {
        val levelPackProgress = progress.levelPackProgress.getOrElse(levelPackId) { LevelPackProgress(emptyMap()) }
        val levelProgress = levelPackProgress.levelProgress.getOrElse(levelId) { LevelStatus() }
        val updatedProgress = progress.copy(
            levelPackProgress =
            progress.levelPackProgress +
                    mapOf(
                        levelPackId to levelPackProgress.copy(
                            levelProgress =
                            levelPackProgress.levelProgress + mapOf(
                                levelId to levelProgress.copy(
                                    starsCount = starsCount
                                )
                            )

                        )
                    )

        )

        saveProgress(context, updatedProgress)
        return getLevelStatus(levelPackId, levelId)
    }

    fun getStarsCount() = progress.getStarsCount()
    fun getLevelStatus(levelPackId: Int, levelId: Int): LevelStatus {
        val levelPackProgress = progress.levelPackProgress.getOrElse(levelPackId) { LevelPackProgress(mapOf()) }
        return levelPackProgress.levelProgress.getOrElse(levelId) { LevelStatus(0) }
    }

    private val mapper = jacksonObjectMapper()

    /**
     * Will save progress to filesystem and update variable
     */
    private fun saveProgress(context: Context, updatedProgress: Progress) {
        val pref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        with(pref.edit()) {
            putString(SharedPreferencesKey.PROGRESS.name, mapper.writeValueAsString(updatedProgress))
            apply()
        }

        progress = updatedProgress
    }

    /**
     * Will read progress from filesystem and update variable
     */
    private fun readProgress(context: Context) {
        val pref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val progressStr = pref.getString(SharedPreferencesKey.PROGRESS.name, null)
        progress = if (progressStr == null) {
            Progress(mapOf(0 to LevelPackProgress(mapOf())))
        } else {
            mapper.readValue(progressStr, Progress::class.java)
        }
    }

    fun resetProgress(context: Context) {
        val updatedProgress = Progress(mapOf(0 to LevelPackProgress(mapOf())))
        saveProgress(context, updatedProgress)
    }
}

data class LevelPackAndLevelId(
    val levelPackId: Int,
    val levelId: Int
)

data class Progress(
    val levelPackProgress: Map<Int, LevelPackProgress>
) {
    @JsonIgnore
    fun getStarsCount(): Int = levelPackProgress.values.sumBy { it.getStarsCount() }

    /** will return the latest level pack + level which contains at least 1 star */
    @JsonIgnore
    fun getLastLevelWithProgress(): LevelPackAndLevelId? {
        var lastLevelWithProgressId: Int? = null
        val lastLevelPackWithProgressId = levelPackProgress.keys.asSequence().sortedDescending().find {
            lastLevelWithProgressId = levelPackProgress.getValue(it).getLastLevelWithProgress()
            lastLevelWithProgressId != null
        }
        return if (lastLevelPackWithProgressId != null && lastLevelWithProgressId != null) {
            LevelPackAndLevelId(lastLevelPackWithProgressId, lastLevelWithProgressId!!)
        } else null
    }
}

data class LevelPackProgress(
    val levelProgress: Map<Int, LevelStatus>
) {
    @JsonIgnore
    fun getStarsCount(): Int = levelProgress.values.sumBy { it.starsCount }

    /** will return the latest level which contains at least 1 star */
    @JsonIgnore
    fun getLastLevelWithProgress(): Int? {
        return levelProgress.keys.asSequence().sortedDescending().find { levelProgress.getValue(it).starsCount > 0 }
    }
}

data class LevelStatus(
    var starsCount: Int = 0
)