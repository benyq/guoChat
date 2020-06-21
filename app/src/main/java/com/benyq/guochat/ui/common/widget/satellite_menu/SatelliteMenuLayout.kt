package com.benyq.guochat.ui.common.widget.satellite_menu

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Handler
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.benyq.guochat.dip2px
import com.benyq.mvvm.ext.getColorRef
import com.benyq.mvvm.ext.loge
import com.facebook.rebound.Spring
import com.facebook.rebound.SpringSystem
import com.tumblr.backboard.Actor
import com.tumblr.backboard.MotionProperty
import com.tumblr.backboard.imitator.Imitator
import com.tumblr.backboard.imitator.SpringImitator
import com.tumblr.backboard.performer.MapPerformer
import com.tumblr.backboard.performer.Performer
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author benyq
 * @time 2020/6/11
 * @e-mail 1520063035@qq.com
 * @note 点击弹出按钮，圆环
 * 参考github上的项目
 * https://github.com/tiancaiCC/SpringFloatingActionMenu
 */
class SatelliteMenuLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    private var mAnimating = false
    var mMenuOpen = false
        private set


    private lateinit var mFab: ImageButton
    private val itemViews = mutableListOf<MenuItemView>()
    private val mFollowCircles = mutableListOf<ImageButton>()
    private val mActionListeners = mutableListOf<OnMenuActionListener>()

    private val menuItems = mutableListOf<MenuItem>()
    private var mGravity: Int = Gravity.BOTTOM xor Gravity.END

    @ColorRes
    private var mRevealColor: Int = android.R.color.holo_purple

    var mContainerView: FrameLayout = FrameLayout(context)
    private var mRevealCircle: View

    init {
        mRevealCircle = generateRevealCircle()
        addView(mRevealCircle)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        // layout FAB and follow circles
        val fabWidth: Int = mFab.measuredWidth
        val fabHeight: Int = mFab.measuredHeight
        var fabX = 0
        var fabY = 0

        val margin = dip2px(context, 10).toInt()
        fabX = right - fabWidth - margin
        fabY = bottom - fabHeight - margin
        mFab.layout(fabX, fabY, fabX + fabWidth, fabY + fabHeight)
        mFollowCircles.forEach {
            //因为宽高一样
            it.layout(fabX, fabY, fabX + fabWidth, fabY + fabHeight)
        }
        mRevealCircle.layout(fabX, fabY, fabX + mRevealCircle.measuredWidth, fabY + mRevealCircle.measuredHeight)
        layoutMenuItems()
    }

    private fun layoutMenuItems() {
        val itemWidth = itemViews[0].measuredWidth
        val itemHeight = itemViews[0].measuredHeight

        val itemDiameter: Int = dip2px(context, menuItems[0].diameter).toInt()
        val itemRadius = itemDiameter / 2

        val ringRadius = (itemDiameter * 1.5).toInt()
        val containerWidth = measuredWidth
        val containerHeight = measuredHeight
        //第一个排在中间
        val firstX = containerWidth / 2 - itemRadius
        val firstY = containerHeight / 2 - itemRadius
        loge("measuredWidth $firstX, measuredHeight $firstY")

        itemViews[0].layout(firstX, firstY, firstX + itemWidth, firstY + itemHeight)

        val arcunit: Double = 2 * Math.PI / (itemViews.size - 1)
        for (i in 1 until itemViews.size) {
            val view = itemViews[i]
            val arc = arcunit * i
            val x = (containerWidth / 2 + ringRadius * sin(arc) - itemRadius).toInt()
            val y = (containerHeight / 2 + ringRadius * cos(arc) - itemRadius).toInt()
            view.layout(x, y, x + view.measuredWidth, y + view.measuredHeight)
        }
    }

    fun showMenu() {
        applyMenuOpenAnimation()
        hideFollowCircles()
        revealIn()
        mActionListeners.forEach {
            it.onMenuOpen()
        }
    }

    fun hideMenu() {
        applyMenuCloseAnimation()
        showFollowCircles()
        revealOut()
        mActionListeners.forEach {
            it.onMenuClose()
        }
    }

    private fun hideFollowCircles(){
        mFollowCircles.forEach {
            it.visibility = View.INVISIBLE
        }
    }

    private fun showFollowCircles(){
        mFollowCircles.forEach {
            it.visibility = View.VISIBLE
        }
    }

    private fun revealIn() {

        mRevealCircle.visibility = View.VISIBLE
        mRevealCircle.animate()
            .scaleX(100f)
            .scaleY(100f)
            .setDuration(600)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object: AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    animation?.removeAllListeners()
                    mMenuOpen = true
                    mAnimating = false
                }

                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                    if (indexOfChild(mContainerView) == -1){
                        addView(mContainerView)
                    }
                    mAnimating = true
                }
            })
            .start()
    }

    private fun revealOut() {
        mRevealCircle.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(600)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object: AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    mRevealCircle.visibility = View.INVISIBLE
                    animation?.removeAllListeners()
                    if (indexOfChild(mContainerView) != -1){
                        removeView(mContainerView)
                    }
                    mMenuOpen = false
                    mAnimating = false
                }

                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                    mAnimating = true
                }
            })
            .start()
    }

    private fun applyMenuCloseAnimation() {
        val alphaDuration = 130L

        val firstView = itemViews[0]

        val springSystem = SpringSystem.create()

        val springScaleX = springSystem.createSpring()
        val springScaleY = springSystem.createSpring()

        springScaleX.addListener(MapPerformer(firstView, View.SCALE_X, 1f, 0f))
        springScaleY.addListener(MapPerformer(firstView, View.SCALE_Y, 1f, 0f))

        springScaleX.endValue = 1.0
        springScaleY.endValue = 1.0

        firstView.animate().alpha(160f)

        for (i in itemViews.size - 1 downTo 1) {
            val itemView = itemViews[i]
            Handler().postDelayed({

                val itemSpringScaleX = springSystem.createSpring()
                val itemSpringScaleY = springSystem.createSpring()
                itemSpringScaleX.addListener(MapPerformer(itemView, View.SCALE_X, 1f, 0f))
                itemSpringScaleY.addListener(MapPerformer(itemView, View.SCALE_Y, 1f, 0f))

                itemSpringScaleX.endValue = 1.0
                itemSpringScaleY.endValue = 1.0

                val itemSpringX = springSystem.createSpring()
                val itemSpringY = springSystem.createSpring()
                itemSpringX.addListener(MapPerformer(itemView, View.X, itemView.left.toFloat(), firstView.left.toFloat()))
                itemSpringY.addListener(MapPerformer(itemView, View.Y, itemView.top.toFloat(), firstView.top.toFloat()))
                loge("applyMenuCloseAnimation ${itemView.top.toFloat()}  --- ${firstView.top.toFloat()}")
                itemSpringX.endValue = 1.0
                itemSpringY.endValue = 1.0

            }, 70L * (itemViews.size - i - 1))
        }
    }

    /**
     * 在itemView 的缩放上，rebound并没有什么特殊的效果
     */
    private fun applyMenuOpenAnimation() {
        val alphaDuration = 130L
        val firstView = itemViews[0]


        //make start position at center
        for (itemView in itemViews) {
            itemView.disableAlphaAnimation()
            itemView.scaleX = 0f
            itemView.scaleY = 0f
        }

        val springSystem = SpringSystem.create()

        val springScaleX = springSystem.createSpring()
        val springScaleY = springSystem.createSpring()
        val removeSpring = RemoveItemSpringListener(this, firstView)
        springScaleX.addListener(MapPerformer(firstView, View.SCALE_X, 0f, 1f))
        springScaleY.addListener(MapPerformer(firstView, View.SCALE_Y, 0f, 1f))
        springScaleX.addListener(removeSpring)
        springScaleY.addListener(removeSpring)
        springScaleX.endValue = 1.0
        springScaleY.endValue = 1.0

        for (i in 1 until itemViews.size) {
            Handler().postDelayed({
                val itemView = itemViews[i]
                val itemSpringScaleX = springSystem.createSpring()
                val itemSpringScaleY = springSystem.createSpring()
                itemSpringScaleX.addListener(MapPerformer(itemView, View.SCALE_X, 0f, 1f))
                itemSpringScaleY.addListener(MapPerformer(itemView, View.SCALE_Y, 0f, 1f))

                itemSpringScaleX.endValue = 1.0
                itemSpringScaleY.endValue = 1.0

                val itemSpringX = springSystem.createSpring()
                val itemSpringY = springSystem.createSpring()
                itemSpringX.addListener(MapPerformer(itemView, View.X, firstView.left.toFloat(), itemView.left.toFloat()))
                itemSpringY.addListener(MapPerformer(itemView, View.Y, firstView.top.toFloat(), itemView.top.toFloat()))

                itemSpringX.endValue = 1.0
                itemSpringY.endValue = 1.0

            }, 70L * (i - 1))
        }
    }

    private fun applyFollowAnimation(){
        /* Animation code */
        val springSystem = SpringSystem.create()
        // create the springs that control movement
        val springX = springSystem.createSpring()
        val springY = springSystem.createSpring()

        // bind circle movement to events
        Actor.Builder(springSystem, mFab)
            .addMotion(springX, Imitator.TRACK_DELTA, Imitator.FOLLOW_EXACT, MotionProperty.X)
            .addMotion(springY, Imitator.TRACK_DELTA, Imitator.FOLLOW_EXACT, MotionProperty.Y)
            .build()
        // add springs to connect between the views
        val followsX =
            arrayOfNulls<Spring>(itemViews.size)
        val followsY =
            arrayOfNulls<Spring>(itemViews.size)

        mFollowCircles.forEachIndexed { i, v ->
            // create spring to bind views
            followsX[i] = springSystem.createSpring()
            followsY[i] = springSystem.createSpring()
            followsX[i]!!.addListener(Performer(mFollowCircles[i], View.TRANSLATION_X))
            followsY[i]!!.addListener(Performer(mFollowCircles[i], View.TRANSLATION_Y))

            // imitates another character
            val followX = SpringImitator(followsX[i]!!)
            val followY = SpringImitator(followsY[i]!!)

            //  imitate the previous character
            if (i == 0) {
                springX.addListener(followX)
                springY.addListener(followY)
            } else {
                followsX[i - 1]!!.addListener(followX)
                followsY[i - 1]!!.addListener(followY)
            }
        }
    }

    fun setSatelliteMenuLayoutBuilder(builder: SatelliteMenuLayoutBuilder) {
        builder.let { it ->
            mActionListeners.addAll(it.mActionListeners)
            mGravity = it.gravity
            menuItems.addAll(it.menuItems)
            mFab = it.fab
            mRevealColor = it.revealColor

            mFollowCircles.addAll(generateFollowCircles())
            mFollowCircles.forEach {
                addView(it)
            }
            itemViews.addAll(generateItemViews())
            itemViews.forEach { menu ->
//                menu.visibility = View.INVISIBLE
                mActionListeners.add(menu)
                mContainerView.addView(menu)
            }
            itemViews[0].bringToFront()
            val fabLp = LayoutParams(
                180,
                180
            )
            fabLp.gravity = it.gravity
            addView(mFab, fabLp)

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            mFab.setOnClickListener {
                if (mAnimating) {
                    return@setOnClickListener
                }
                builder.onFabClickListener?.onClick(it)
                loge(" mFab.setOnClickListener $mMenuOpen")
                if (mMenuOpen) {
                    hideMenu()
                } else {
                    showMenu()
                }
            }
            viewTreeObserver.addOnGlobalLayoutListener {
                applyFollowAnimation()
            }
        }
    }

    private fun generateFollowCircles(): List<ImageButton> {
        val diameter = 180
        return menuItems.map {
            val lp = LayoutParams(diameter, diameter)
            val circle = ImageButton(context)
            circle.background = ShapeDrawable(OvalShape()).apply {
                paint.color = context.getColorRef(it.bgColor)
            }
            circle.layoutParams = lp
            circle.setImageResource(it.icon)
            circle
        }
    }

    private fun generateRevealCircle(): View {
        val view = View(context)
        val lp = LayoutParams(180, 180)
        lp.gravity = mGravity
        view.background = ShapeDrawable(OvalShape()).apply {
            paint.color = ContextCompat.getColor(context, mRevealColor)
        }
        view.layoutParams = lp
        view.isClickable = true
        view.visibility = View.INVISIBLE
        return view
    }

    private fun generateItemViews(): List<MenuItemView> {
        return menuItems.map {
            val view = MenuItemView(context, it)
            view.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            view.setMenuItem(it)
            view
        }
    }


    class SatelliteMenuLayoutBuilder {

        lateinit var context: Context

        val menuItems = mutableListOf<MenuItem>()

        lateinit var fab: ImageButton

        val gravity = Gravity.BOTTOM or Gravity.END

        @ColorRes
        var revealColor = android.R.color.holo_purple

        var onFabClickListener: OnClickListener? = null

        var mActionListeners = mutableListOf<OnMenuActionListener>()


        fun addMenuItem(
            @ColorRes bgColor: Int, icon: Int, label: String,
            @ColorRes textColor: Int, itemClickListener: View.OnClickListener?
        ) {
            menuItems.add(MenuItem(bgColor, icon, label, itemClickListener, textColor))
        }

        fun onMenuActionListener(listener: OnMenuActionListener) {
            mActionListeners.add(listener)
        }

        fun build(): SatelliteMenuLayout {
            return SatelliteMenuLayout(context).apply {
                setSatelliteMenuLayoutBuilder(this@SatelliteMenuLayoutBuilder)
            }
        }
    }
}