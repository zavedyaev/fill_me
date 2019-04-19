package ru.zavedyaev.fillme

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import ru.zavedyaev.fillme.level.ProgressInstance

class SettingsActivity : AppCompatActivity() {

    private lateinit var resetProgressButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        resetProgressButton = findViewById(R.id.resetProgressButton)
        resetProgressButton.setOnClickListener {
            ProgressInstance.resetProgress(this)
            Snackbar.make(findViewById(R.id.root), R.string.progress_erased, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        //return to main activity
        return when (item?.itemId) {
            android.R.id.home -> {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
