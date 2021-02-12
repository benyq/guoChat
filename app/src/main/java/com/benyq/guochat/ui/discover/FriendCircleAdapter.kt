package com.benyq.guochat.ui.discover

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.benyq.guochat.R
import com.benyq.guochat.model.bean.FriendCircleBean
import com.benyq.guochat.ui.common.widget.NineGridLayout
import com.benyq.guochat.ui.common.widget.friend_circle.CircleCommentView
import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.ext.getColorRef
import com.benyq.module_base.ext.gone
import com.benyq.module_base.ext.visible
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.item_friend_circle.view.*


/**
 * @author benyq
 * @time 2020/7/11
 * @e-mail 1520063035@qq.com
 * @note
 */
class FriendCircleAdapter :
    BaseQuickAdapter<FriendCircleBean, BaseViewHolder>(R.layout.item_friend_circle) {

    private var mNineItemAction: ((List<View>, List<String>, Int) -> Unit)? = null

    init {
        //新版本要放在这!!
        addChildClickViewIds(R.id.ivLike, R.id.ivComments)
    }

    override fun convert(holder: BaseViewHolder, item: FriendCircleBean) {

        holder.setText(R.id.tvPublishName, item.publishName)
        val ivAvatar = holder.getView<ImageView>(R.id.ivAvatar)
        Glide.with(context).load(item.publishAvatar).into(ivAvatar)

        holder.setText(R.id.tvContent, item.content)
        val nineGridView = holder.getView<NineGridLayout>(R.id.nineGridImages)
        nineGridView.setItemAction(mNineItemAction)
        if (!item.imgList.isNullOrEmpty()) {
            nineGridView.addItems(item.imgList)
            nineGridView.visible()
        }else {
            nineGridView.gone()
        }
        holder.setImageResource(R.id.ivLike, if (item.like) R.drawable.ic_circle_liked else R.drawable.ic_circle_not_like)
        //点赞
        val tvLike = holder.getView<TextView>(R.id.tvLikeContract)
        val divider = holder.getView<View>(R.id.divider)
        if (item.likeList.isNullOrEmpty()) {
            tvLike.gone()
            divider.gone()
        }else {
            tvLike.visible()
            tvLike.text = setLikeContract(item.likeList!!)
            divider.visible()
            tvLike.movementMethod = LinkMovementMethod.getInstance()
        }
        //评论
        val circleCommentView = holder.getView<CircleCommentView>(R.id.circleCommentView)
        circleCommentView.setCommentsData(item.commentsList)
        circleCommentView.notifyDataSetChanged()

    }

    private fun setLikeContract(likeContracts: List<String>): SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        val imgSpan = ImageSpan(context, R.drawable.ic_circle_not_like_small)
        val spannableString = SpannableString("like")
        spannableString.setSpan(imgSpan, 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.append(spannableString)
        builder.append(" ")
        likeContracts.forEachIndexed { index, s ->
            builder.append(initNameSpannableString(s))
            if (index != likeContracts.size - 1) {
                builder.append(",")
            }
        }
        return builder
    }

    private fun initNameSpannableString(name: String): SpannableString {
        val spannableString = SpannableString(name)
        val clickSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toasts.show(name)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = context.getColorRef(R.color.color_697A9F)
                ds.isUnderlineText = false
            }
        }
        spannableString.setSpan(clickSpan, 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    fun setItemAction(action: ((List<View>, List<String>, Int) -> Unit)?) {
        mNineItemAction = action
    }
}