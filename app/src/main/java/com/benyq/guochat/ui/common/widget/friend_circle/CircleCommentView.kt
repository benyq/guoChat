package com.benyq.guochat.ui.common.widget.friend_circle

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.benyq.guochat.R
import com.benyq.guochat.model.bean.CircleComment
import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.ext.getColorRef


/**
 * @author benyq
 * @time 2020/7/11
 * @e-mail 1520063035@qq.com
 * @note 评论View
 */
class CircleCommentView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    private var commentsData: MutableList<CircleComment> = mutableListOf()


    init {
        orientation = VERTICAL
    }

    fun setCommentsData(value: List<CircleComment>?) {
        commentsData.clear()
        value?.let {
            commentsData.addAll(it)
        }
    }

    fun notifyDataSetChanged() {
        removeAllViews()
        if (commentsData.isEmpty()) {
            return
        }

        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, 0, 0, 0)

        commentsData.forEachIndexed { index, circleComment ->
            val view = initView(circleComment)
            addView(view, layoutParams)
        }

    }

    private fun initView(comment: CircleComment): TextView {
        val textView = TextView(context)
        textView.textSize = 12f
        textView.setTextColor(context.getColorRef(R.color.color3C4044))
        val builder = SpannableStringBuilder()
        builder.append(setClickableSpan(comment.publishName, comment.publishId))
        if (!comment.applyContractId.isNullOrEmpty() && !comment.applyContractName.isNullOrEmpty()) {
            builder.append("回复")
            builder.append(setClickableSpan(comment.applyContractName, comment.applyContractId))
        }
        builder.append(" : ")
        builder.append(comment.content)
        textView.text = builder
        textView.movementMethod = LinkMovementMethod.getInstance()
        return textView
    }


    private fun setClickableSpan(name: String, uid: String): SpannableString {
        val spannableString = SpannableString(name)
        val clickSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toasts.show("$name ------------ $uid")
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = context.getColorRef(R.color.color_697A9F)
                ds.isUnderlineText = false
            }
        }
        spannableString.setSpan(
            clickSpan,
            0,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }
}