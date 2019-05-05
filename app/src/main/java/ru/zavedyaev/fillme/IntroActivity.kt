package ru.zavedyaev.fillme

import android.content.Intent
import com.chyrta.onboarder.OnboarderActivity
import com.chyrta.onboarder.OnboarderPage
import android.os.Bundle
import android.view.View


class IntroActivity : OnboarderActivity() {

    private lateinit var onboarderPages: MutableList<OnboarderPage>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onboarderPages = ArrayList()

        // Create your first page
//        val onboarderPage1 = OnboarderPage("Title 1", "Description 1", R.drawable.tutorial_1)
        val onboarderPage2 = OnboarderPage(R.string.onboard_title_2, R.string.onboard_body_2, R.drawable.tutorial_2)
        val onboarderPage3 = OnboarderPage(R.string.onboard_title_3, R.string.onboard_body_3, R.drawable.tutorial_3)
        val onboarderPage4 = OnboarderPage(R.string.onboard_title_4, R.string.onboard_body_4, R.drawable.tutorial_4)
        val onboarderPage5 = OnboarderPage(R.string.onboard_title_5, R.string.onboard_body_5, R.drawable.tutorial_5)
        val onboarderPage6 = OnboarderPage(R.string.onboard_title_6, R.string.onboard_body_6, R.drawable.tutorial_6)
        val onboarderPage7 = OnboarderPage(R.string.onboard_title_7, R.string.onboard_body_7, R.drawable.tutorial_7)
        val onboarderPage8 = OnboarderPage(R.string.onboard_title_8, R.string.onboard_body_8, R.drawable.tutorial_8)
        val onboarderPage9 = OnboarderPage(R.string.onboard_title_9, R.string.onboard_body_9, R.drawable.tutorial_9)


        // Add your pages to the list
//        onboarderPages.add(onboarderPage1)
        onboarderPages.add(onboarderPage2)
        onboarderPages.add(onboarderPage3)
        onboarderPages.add(onboarderPage4)
        onboarderPages.add(onboarderPage5)
        onboarderPages.add(onboarderPage6)
        onboarderPages.add(onboarderPage7)
        onboarderPages.add(onboarderPage8)
        onboarderPages.add(onboarderPage9)

        onboarderPages.forEach {
            it.setBackgroundColor(R.color.white)
            it.setTitleColor(R.color.text_black)
            it.setDescriptionColor(R.color.text_black)
        }


        shouldUseFloatingActionButton(true)
        setActiveIndicatorColor(R.color.colorAccent)
        setDividerVisibility(View.GONE)

        setOnboardPagesReady(onboarderPages)

    }

    override fun onFinishButtonPressed() {
        playButtonSound()
        val i = Intent(this, GameActivity::class.java)
        i.putExtra(GameActivity.LEVEL_PACK_ID_EXTRA_NAME, 0)
        i.putExtra(GameActivity.LEVEL_ID_EXTRA_NAME, 0)
        startActivity(i)
    }


    override fun onResume() {
        super.onResume()
        val serviceIntent = Intent(this, SoundService::class.java)
        serviceIntent.putExtra(SoundService.COMMAND_EXTRA_NAME, SoundServiceCommand.RESUME.name)
        startService(serviceIntent)

        val serviceIntent2 = Intent(this, SoundService::class.java)
        serviceIntent2.putExtra(SoundService.COMMAND_EXTRA_NAME, SoundServiceCommand.MENU_MUSIC.name)
        startService(serviceIntent2)
    }

    override fun onPause() {
        super.onPause()
        val serviceIntent = Intent(this, SoundService::class.java)
        serviceIntent.putExtra(SoundService.COMMAND_EXTRA_NAME, SoundServiceCommand.PAUSE.name)
        startService(serviceIntent)
    }

    private fun playButtonSound() {
        val serviceIntent = Intent(this, SoundService::class.java)
        serviceIntent.putExtra(SoundService.COMMAND_EXTRA_NAME, SoundServiceCommand.PLAY_SOUND_BUTTON.name)
        startService(serviceIntent)
    }
}