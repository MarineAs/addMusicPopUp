package com.example.myapplication

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.add_buttons_custom_view.view.*


class AddButtonsCustomView(context: Context, attr: AttributeSet?) : ConstraintLayout(context, attr),
    View.OnTouchListener, View.OnClickListener {
    var btnSelected: FloatingActionButton? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.add_buttons_custom_view, this, false)
        addView(view)
        btnPlay.setOnTouchListener(this)
        root.setOnClickListener(this)
    }

    fun makeVisibleCustomView() {
        this.visibility = View.VISIBLE
        animatedButtonsFadeIn()
    }

    private fun makeInvisibleCustomView() {
        this.visibility = View.INVISIBLE
    }

    private fun animatedButtonsFadeIn() {
        val moveToTopNowPlaying = 2 * btnNowPlaying.height.toFloat()
        val animationToUp = TranslateAnimation(0f, 0f, 0f, -moveToTopNowPlaying)
        animationToUp.duration = 400
        animationToUp.fillAfter = true
        btnNowPlaying.animation = animationToUp

        val moveToRightAddTo: Float = 2 * btnAddTo.width.toFloat()
        val moveToTopAddTo = btnAddFavorite.height.toFloat() / 2
        val animationToRight = TranslateAnimation(0f, moveToRightAddTo, 0f, -moveToTopAddTo)
        animationToRight.duration = 400
        animationToRight.fillAfter = true
        btnAddTo.animation = animationToRight

        val moveToLeftFavorite: Float = 2 * btnAddFavorite.width.toFloat()
        val moveToTopFavorite: Float = btnAddFavorite.height.toFloat() / 2
        val animationToLeft = TranslateAnimation(0f, -moveToLeftFavorite, 0f, -moveToTopFavorite)
        animationToLeft.duration = 400
        animationToLeft.fillAfter = true
        btnAddFavorite.animation = animationToLeft
    }

    private fun animatedBtnPlayStartPosition() {
        if (btnPlay.visibility == View.INVISIBLE) {
            btnPlay.visibility = View.VISIBLE
        }
        btnPlay.x = btnPlay.x - btnPlay.translationX
        btnPlay.y = btnPlay.y - btnPlay.translationY
    }

    private fun selectedBtn(button: FloatingActionButton) {
        btnSelected = button
        btnSelected?.isPressed=true
        btnSelected?.backgroundTintList = ColorStateList.valueOf(
            ResourcesCompat.getColor(
                resources,
                R.color.moreBtnPressedColor, null
            )
        )
    }

    private fun cancelUIChanges() {
        btnSelected?.backgroundTintList = ColorStateList.valueOf(
            ResourcesCompat.getColor(
                resources,
                R.color.moreBtnbackColor, null
            )
        )
        if (selectedText.visibility == View.VISIBLE) {
            selectedText.visibility = View.INVISIBLE
        }
        animatedBtnPlayStartPosition()
    }

    private fun movePlayButton(x: Float, y: Float) {
        btnPlay.x = x - btnPlay.width / 2
        btnPlay.y = y - btnPlay.height - btnPlay.height / 2
    }

    private fun addSelectedText() {
        when (btnSelected) {
            btnNowPlaying -> {
                selectedText.text = getResources().getString(R.string.add_to_now_playing)
                selectedText.visibility = View.VISIBLE
            }
            btnAddFavorite -> {
                selectedText.text = getResources().getString(R.string.add_to_favorites)
                selectedText.visibility = View.VISIBLE
            }
            btnAddTo -> {
                selectedText.text = getResources().getString(R.string.add_to_selected)
                selectedText.visibility = View.VISIBLE
            }
        }
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        val eventCordX = event.rawX
        val eventCordY = event.rawY
        val RANGE__TO_X_BOTTOM = 100
        val RANGE_TO_X_TOP = 300
        val RANGE__TO_Y_BOTTOM = 300
        val RANGE_TO_Y_TOP = 150
        val btnNowPlayingXBottom = btnNowPlaying.x - RANGE__TO_X_BOTTOM
        val btnNowPlayingXTop = btnNowPlaying.x + RANGE_TO_X_TOP
        val btnNowPlayingYBottom = btnNowPlaying.y + RANGE__TO_Y_BOTTOM
        val btnNowPlayingYTop = btnNowPlaying.y - RANGE_TO_Y_TOP
        val btnAddFavoriteXBottom =
            (btnAddFavorite.x - 2.1 * btnAddFavorite.width - RANGE__TO_X_BOTTOM).toFloat()
        val bntAddFavoriteXTop = btnAddFavorite.x - 2 * btnAddFavorite.width + RANGE_TO_X_TOP
        val btnAddToXBottom = btnAddTo.x + 2 * btnAddTo.width - RANGE__TO_X_BOTTOM
        val btnAddToTop = btnAddTo.x + 2 * btnAddTo.width + RANGE_TO_X_TOP
        val btnAddToYBottom = btnAddTo.y + 2 * btnAddTo.height - RANGE__TO_Y_BOTTOM
        val btnAddToYTop = btnAddTo.y + 2 * btnAddTo.height + RANGE_TO_Y_TOP

        if (event.action == MotionEvent.ACTION_MOVE) {
            movePlayButton(eventCordX, eventCordY)
        } else if (event.action == MotionEvent.ACTION_UP && btnPlay.visibility == View.VISIBLE) {
            if (eventCordX in btnAddToXBottom..btnAddToTop && eventCordY in btnAddToYBottom..btnAddToYTop) {
                selectedBtn(btnAddTo)
                btnPlay.visibility = View.INVISIBLE
                addSelectedText()
            } else if (eventCordX in btnAddFavoriteXBottom..bntAddFavoriteXTop && eventCordY in btnAddToYBottom..btnAddToYTop) {
                selectedBtn(btnAddFavorite)
                btnPlay.visibility = View.INVISIBLE
                addSelectedText()
            } else if (eventCordX in btnNowPlayingXBottom..btnNowPlayingXTop && eventCordY in btnNowPlayingYTop..btnNowPlayingYBottom) {
                selectedBtn(btnNowPlaying)
                btnPlay.visibility = View.INVISIBLE
                addSelectedText()
            }
        }
        return false
    }

    override fun onClick(v: View?) {
        makeInvisibleCustomView()
        cancelUIChanges()

    }

}