package com.benyq.guochat.chat.ui.discover

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.guochat.chat.R
import com.benyq.guochat.chat.app.CIRCLE__TYPE_TEXT
import com.benyq.guochat.chat.app.IntentExtra
import com.benyq.guochat.chat.databinding.ActivityFriendCircleBinding
import com.benyq.guochat.chat.local.ChatLocalStorage
import com.benyq.guochat.chat.model.bean.CircleComment
import com.benyq.guochat.chat.model.bean.FriendCircleBean
import com.benyq.guochat.chat.model.vm.FriendCircleViewModel
import com.benyq.module_base.ui.base.LifecycleActivity
import com.benyq.module_base.ui.widget.HeaderView
import com.benyq.guochat.chat.ui.common.widget.satellite_menu.MenuItemView
import com.benyq.guochat.chat.ui.common.widget.satellite_menu.OnMenuActionListener
import com.benyq.guochat.chat.ui.common.widget.satellite_menu.SatelliteMenuLayout
import com.benyq.imageviewer.ImagePreview
import com.benyq.imageviewer.PreviewPhoto
import com.benyq.imageviewer.PreviewTypeEnum
import com.benyq.module_base.SmartJump
import com.benyq.module_base.ext.getDrawableRef
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ext.loadImage
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ImmersionBar
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs


/**
 * @author benyq
 * @time 2020/5/26
 * @e-mail 1520063035@qq.com
 * @note 果聊朋友圈，当前用户的
 */
@AndroidEntryPoint
class FriendCircleActivity : LifecycleActivity<FriendCircleViewModel, ActivityFriendCircleBinding>() {

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

    private lateinit var mSatelliteMenuLayout: SatelliteMenuLayout
    private val mAdapter by lazy { FriendCircleAdapter() }

    override fun initVM(): FriendCircleViewModel = getViewModel()

    override fun provideViewBinding() = ActivityFriendCircleBinding.inflate(layoutInflater)

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .statusBarView(R.id.toolbar)
            .autoDarkModeEnable(true, 0.2f)
            .init()
    }

    override fun initView() {
        ImmersionBar.setTitleBar(this, binding.toolbar)

        val user = ChatLocalStorage.userAccount
        binding.ivAvatar.loadImage(user.avatarUrl)
        binding.tvNickName.text = user.nick
        Glide.with(this)
            .load("https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=11232128,2567744034&fm=26&gp=0.jpg")
            .centerCrop().into(binding.ivBg)

        setSatelliteMenu()

        binding.rvFriendCircle.layoutManager = LinearLayoutManager(this)
        binding.rvFriendCircle.itemAnimator?.changeDuration = 0
        mAdapter.setItemAction { views, list, i ->
            ImagePreview.setCacheView(views)
                .setData(list.map { PreviewPhoto(it, PreviewTypeEnum.IMAGE) })
                .setCurPosition(i)
                .show(this)
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val friendCircleBean = mAdapter.data[position]
            if (view.id == R.id.ivLike) {
                viewModelGet().friendCircleLike(friendCircleBean.circleId, friendCircleBean.like)
                friendCircleBean.like = !friendCircleBean.like
                mAdapter.notifyItemChanged(position)
            } else if (view.id == R.id.ivComments) {
                AddCircleCommentDialog.newInstance().apply {
                    setConfirmAction {
                        if (friendCircleBean.commentsList == null) {
                            friendCircleBean.commentsList = mutableListOf()
                        }
                        friendCircleBean.commentsList?.let { comments ->
                            comments.add(
                                CircleComment(
                                    "3",
                                    it,
                                    user.chatId,
                                    user.nick,
                                    "",
                                    "",
                                    ""
                                )
                            )
                        }
                        mAdapter.notifyItemChanged(position)
                    }
                    show(supportFragmentManager)
                }
            }
        }
        binding.rvFriendCircle.adapter = mAdapter
    }

    override fun initData() {
        viewModelGet().queryFriendCircles()
    }

    override fun initListener() {
        binding.headerView.setBackAction { finish() }
        binding.appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            private var offset = 0
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                val offHeight: Int = (appBarLayout.totalScrollRange * 0.8).toInt()
                val percentage = abs(verticalOffset) * 1f / appBarLayout.totalScrollRange

                if (offset >= verticalOffset) {
                    //向上
                    if (percentage > 0.8) {
                        binding.ivAvatar.alpha = 1 - percentage
                        binding.tvNickName.alpha = 1 - percentage
                    }
                } else {
                    //向下
                    if (percentage < 0.8) {
                        binding.ivAvatar.alpha = 1f
                        binding.tvNickName.alpha = 1f
                    } else {
                        binding.ivAvatar.alpha = 1 - percentage
                        binding.tvNickName.alpha = 1 - percentage
                    }
                }
                offset = verticalOffset
                if (percentage == 1f) {
                    binding.headerView.setHeaderViewMode(HeaderView.toolbarTypeNormal)
                    binding.headerView.setToolbarTitle(getString(R.string.guo_chat_circle))
                }
                if (percentage == 0f) {
                    binding.headerView.setHeaderViewMode(HeaderView.toolbarTypeDark)
                    binding.headerView.setToolbarTitle("")
                }
                ImmersionBar.with(this@FriendCircleActivity)
                    .statusBarDarkFont(abs(verticalOffset) >= offHeight, 0.2f).init()
            }
        })

    }

    override fun dataObserver() {
        viewModelGet().mFriendCircleData.observe(this, Observer {
            mAdapter.setNewInstance(it.toMutableList())
        })
        viewModelGet().mFriendCircleLikeData.observe(this, Observer {
//            initData()
        })
    }

    private fun setSatelliteMenu() {
        createFabFrameAnim()
        createFabReverseFrameAnim()

        val mFab = ImageButton(this)
        val drawable =
            StateListDrawable()
        drawable.addState(
            intArrayOf(android.R.attr.state_pressed),
            createDrawable(Color.parseColor("#36465d"))
        )
        drawable.addState(
            intArrayOf(-android.R.attr.state_enabled),
            createDrawable(Color.parseColor("#529ecc"))
        )
        drawable.addState(intArrayOf(), createDrawable(Color.parseColor("#529ecc")))

        mFab.background = drawable

        mFab.setImageDrawable(frameAnim)
        val menuItemListener = createMenuListener()
        mSatelliteMenuLayout = SatelliteMenuLayout.SatelliteMenuLayoutBuilder()
            .apply {
                context = this@FriendCircleActivity
                fab = mFab
                addMenuItem(
                    R.color.photo,
                    R.mipmap.ic_messaging_posttype_photo,
                    "图片",
                    R.color.text_color,
                    menuItemListener
                )
                addMenuItem(
                    R.color.chat,
                    R.mipmap.ic_messaging_posttype_chat,
                    "Chat",
                    R.color.text_color,
                    menuItemListener
                )
                addMenuItem(
                    R.color.quote,
                    R.mipmap.ic_messaging_posttype_quote,
                    "Quote",
                    R.color.text_color,
                    menuItemListener
                )
                addMenuItem(
                    R.color.link,
                    R.mipmap.ic_messaging_posttype_link,
                    "链接",
                    R.color.text_color,
                    menuItemListener
                )
                addMenuItem(
                    R.color.audio,
                    R.mipmap.ic_messaging_posttype_audio,
                    "音频",
                    R.color.text_color,
                    menuItemListener
                )
                addMenuItem(
                    R.color.text,
                    R.mipmap.ic_messaging_posttype_text,
                    "文字",
                    R.color.text_color,
                    menuItemListener
                )
                addMenuItem(
                    R.color.video,
                    R.mipmap.ic_messaging_posttype_video,
                    "视频",
                    R.color.text_color,
                    menuItemListener
                )
                revealColor = R.color.color_36465d
                onMenuActionListener(object : OnMenuActionListener {
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
        rootView.addView(mSatelliteMenuLayout)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (mSatelliteMenuLayout.mMenuOpen) {
                    mSatelliteMenuLayout.hideMenu()
                } else {
                    finish()
                }
            }
        })
    }

    private fun createMenuListener(): View.OnClickListener {
        return View.OnClickListener {
            if (it is MenuItemView) {
                when (it.getMenuIcon()) {
                    R.mipmap.ic_messaging_posttype_photo -> {
                        SmartJump.from(this)
                            .startForResult(AddCircleActivity::class.java, { resultCode, data ->
                                if (resultCode == Activity.RESULT_OK && data != null) {
                                    val content = data.getStringExtra(IntentExtra.addCircleContent)
                                    val images: Array<String> = data.getStringArrayExtra(
                                        IntentExtra.addCircleImages
                                    ) ?: arrayOf()

                                    //一开始 rvFriendCircle.scrollToPosition(0)失效的原因是因为RecyclerView在NestedScrollView内部导致的
                                    mAdapter.addData(
                                        0, FriendCircleBean(
                                            "3",
                                            "张三",
                                            "",
                                            "https://tse1-mm.cn.bing.net/th?id=OIP.FYHKQ4QDg7JI02RToq-CqgHaHM&w=180&h=160&c=8&rs=1&qlt=90&dpr=1.25&pid=3.1&rm=2",
                                            CIRCLE__TYPE_TEXT,
                                            content,
                                            images.toList(),
                                            "",
                                            "",
                                            false,
                                            listOf(),
                                            null
                                        )
                                    )
                                    binding.appBar.setExpanded(true)
                                    binding.rvFriendCircle.scrollToPosition(0)
                                }
                            })
                    }
                    R.mipmap.ic_messaging_posttype_text -> {

                    }
                }
                mSatelliteMenuLayout.hideMenu()
            }
        }
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
