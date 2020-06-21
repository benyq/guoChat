package com.benyq.guochat.ui

import android.graphics.Color
import android.graphics.drawable.*
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.benyq.guochat.R
import com.benyq.guochat.ui.common.widget.satellite_menu.MenuItemView
import com.benyq.guochat.ui.common.widget.satellite_menu.OnMenuActionListener
import com.benyq.guochat.ui.common.widget.satellite_menu.SatelliteMenuLayout
import com.benyq.mvvm.ext.getDrawableRef
import com.benyq.mvvm.ext.loge

class TestActivity : AppCompatActivity(), View.OnClickListener {

    private val frameDuration = 20
    private lateinit var frameAnim: AnimationDrawable
    private lateinit var frameReverseAnim: AnimationDrawable

    private val frameAnimRes = intArrayOf(
        R.mipmap.compose_anim_1,
        R.mipmap.compose_anim_2,
        R.mipmap.compose_anim_3,
        R.mipmap.compose_anim_4,
        R.mipmap.compose_anim_5,
        R.mipmap.compose_anim_6,
        R.mipmap.compose_anim_7,
        R.mipmap.compose_anim_8,
        R.mipmap.compose_anim_9,
        R.mipmap.compose_anim_10,
        R.mipmap.compose_anim_11,
        R.mipmap.compose_anim_12,
        R.mipmap.compose_anim_13,
        R.mipmap.compose_anim_14,
        R.mipmap.compose_anim_15,
        R.mipmap.compose_anim_15,
        R.mipmap.compose_anim_16,
        R.mipmap.compose_anim_17,
        R.mipmap.compose_anim_18,
        R.mipmap.compose_anim_19
    )

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        createFabFrameAnim()
        createFabReverseFrameAnim()

        val mFab = ImageButton(this)
        val drawable =
            StateListDrawable()
        drawable.addState(intArrayOf(android.R.attr.state_pressed), createDrawable(Color.parseColor("#36465d")))
        drawable.addState(intArrayOf(-android.R.attr.state_enabled), createDrawable(Color.parseColor("#529ecc")))
        drawable.addState(intArrayOf(), createDrawable(Color.parseColor("#529ecc")))

        mFab.background = drawable

        mFab.setImageDrawable(frameAnim)
        val satelliteMenuLayout = SatelliteMenuLayout.SatelliteMenuLayoutBuilder()
            .apply {
                context = this@TestActivity
                fab = mFab
                addMenuItem(
                    R.color.photo,
                    R.mipmap.ic_messaging_posttype_photo,
                    "Photo",
                    R.color.text_color,
                    this@TestActivity
                )
                addMenuItem(
                    R.color.chat,
                    R.mipmap.ic_messaging_posttype_chat,
                    "Chat",
                    R.color.text_color,
                    this@TestActivity
                )
                addMenuItem(
                    R.color.quote,
                    R.mipmap.ic_messaging_posttype_quote,
                    "Quote",
                    R.color.text_color,
                    this@TestActivity
                )
                addMenuItem(
                    R.color.link,
                    R.mipmap.ic_messaging_posttype_link,
                    "Link",
                    R.color.text_color,
                    this@TestActivity
                )
                addMenuItem(
                    R.color.audio,
                    R.mipmap.ic_messaging_posttype_audio,
                    "Audio",
                    R.color.text_color,
                    this@TestActivity
                )
                addMenuItem(
                    R.color.text,
                    R.mipmap.ic_messaging_posttype_text,
                    "Text",
                    R.color.text_color,
                    this@TestActivity
                )
                addMenuItem(
                    R.color.video,
                    R.mipmap.ic_messaging_posttype_video,
                    "Video",
                    R.color.text_color,
                    this@TestActivity
                )
                revealColor = R.color.color_36465d
                onMenuActionListener(object: OnMenuActionListener {
                    override fun onMenuClose() {
                        mFab.setImageDrawable(frameReverseAnim)
                        frameReverseAnim.start()
                    }

                    override fun onMenuOpen() {
                        mFab.setImageDrawable(frameAnim)
                        frameAnim.start()
                    }
                })
            }.build()


        val rootView = findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(satelliteMenuLayout)

        onBackPressedDispatcher.addCallback(this,object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (satelliteMenuLayout.mMenuOpen) {
                    satelliteMenuLayout.hideMenu()
                } else {
                    finish()
                }
            }
        })
    }

    override fun onClick(v: View?) {
        val menuItemView: MenuItemView = v as MenuItemView
        Toast.makeText(this, "ddddd", Toast.LENGTH_SHORT).show()
    }

    private fun createFabFrameAnim() {
        frameAnim = AnimationDrawable()
        frameAnim.isOneShot = true
        frameAnimRes.forEach {
            frameAnim.addFrame(getDrawableRef(it)!!, frameDuration)
        }
    }

    private fun createFabReverseFrameAnim() {
        frameReverseAnim = AnimationDrawable()
        frameReverseAnim.isOneShot = true
        for (i in frameAnimRes.size - 1 downTo 0) {
            frameReverseAnim.addFrame(getDrawableRef(frameAnimRes[i])!!, frameDuration)
        }
    }

    private fun createDrawable(color: Int): Drawable? {
        val ovalShape = OvalShape()
        val shapeDrawable = ShapeDrawable(ovalShape)
        shapeDrawable.paint.color = color
        return shapeDrawable
    }


}
