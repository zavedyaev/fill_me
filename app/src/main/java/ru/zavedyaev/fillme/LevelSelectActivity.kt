package ru.zavedyaev.fillme

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.zavedyaev.fillme.level.LevelsPacks

class LevelSelectActivity : BackgroundSoundActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)

        val rvLevels = this.findViewById<RecyclerView>(R.id.rvLevels)
        val columnsCount = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> if (resources.configuration.screenWidthDp >= 258 * 3) 3 else 2
            else -> 1
        }
        rvLevels.layoutManager = StaggeredGridLayoutManager(columnsCount, StaggeredGridLayoutManager.VERTICAL)
        rvLevels.adapter = LevelsAdapter(this, LevelsPacks.packs[0]!!.levels.values.toList(), this::playButtonSound)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        //return to main activity
        return when (item?.itemId) {
            android.R.id.home -> {
                playButtonSound()
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val i = Intent(this, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
    }
}
