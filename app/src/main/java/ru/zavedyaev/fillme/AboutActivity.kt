package ru.zavedyaev.fillme

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class AboutActivity : BackgroundSoundActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)


        findViewById<LinearLayout>(R.id.aboutMainLayout).setOnClickListener {
            playButtonSound()
            this.onBackPressed()
        }
        findViewById<MaterialCardView>(R.id.aboutCardView).setOnClickListener { }

        val pInfo = applicationContext.packageManager.getPackageInfo(packageName, 0)
        val version = pInfo.versionName

        findViewById<TextView>(R.id.versionTextView).text = getString(R.string.version, version)
        findViewById<TextView>(R.id.sourceCodeTextView).movementMethod = LinkMovementMethod.getInstance()
        findViewById<TextView>(R.id.designTextView).movementMethod = LinkMovementMethod.getInstance()
        findViewById<TextView>(R.id.musicTextView).movementMethod = LinkMovementMethod.getInstance()

        findViewById<MaterialButton>(R.id.feedbackButton).setOnClickListener {
            playButtonSound()

            val intent = Intent(Intent.ACTION_SENDTO)
            intent.type = "text/plain"
            intent.data = Uri.parse("mailto:zavedyaev@gmail.com")
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_email_title))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.feedback_email_body))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(Intent.createChooser(intent, getString(R.string.feedback_email_intent_title)))
        }

        findViewById<MaterialButton>(R.id.rateOrShareButton).setOnClickListener {
            playButtonSound()
            val appPackageName = packageName
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (anfe: android.content.ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }
        }
    }
}
