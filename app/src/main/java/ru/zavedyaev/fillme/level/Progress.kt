package ru.zavedyaev.fillme.level

//todo store it somehow
object ProgressInstance {
    val progress = Progress(
        hashMapOf(
            0 to LevelPackProgress(
                hashMapOf()
            )
        )
    )
}

data class Progress(
    val levelPackProgress: HashMap<Int, LevelPackProgress>
) {
    fun getStarsCount(): Int = levelPackProgress.values.sumBy { it.getStarsCount() }
}

data class LevelPackProgress(
    val levelProgress: HashMap<Int, LevelStatus>
) {
    fun getStarsCount(): Int = levelProgress.values.sumBy { it.starsCount }
}

data class LevelStatus(
    var starsCount: Int = 0
)