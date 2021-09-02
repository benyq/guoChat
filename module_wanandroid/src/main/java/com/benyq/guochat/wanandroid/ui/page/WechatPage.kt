package com.benyq.guochat.wanandroid.ui.page

import android.text.TextUtils
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.benyq.guochat.wanandroid.R
import com.benyq.guochat.wanandroid.model.ArticleData
import com.benyq.guochat.wanandroid.model.fakeArticleData
import com.benyq.guochat.wanandroid.model.vm.WechatViewModel
import com.benyq.module_base.ui.WebViewActivity

/**
 *
 * @author benyq
 * @date 2021/9/2
 * @email 1520063035@qq.com
 * 旋转功能还未实现
 */

@Composable
@Preview
fun ShowWechatPage() {
    WechatPage()
}

@Composable
fun WechatPage(wechatViewModel: WechatViewModel = viewModel()) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {

        val articleData = wechatViewModel.pager.collectAsLazyPagingItems()
        val selectedAuthor by wechatViewModel.selectedAuthor.observeAsState()
        selectedAuthor?.run {
            articleData.refresh()
        }
        WechatPageTop(selectedAuthor?.name ?: "")
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            articleData.apply {
                itemsIndexed(this) { index, article ->
                    article?.run {
                        WechatArticleItem(
                            Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .padding(10.dp, 5.dp)
                                .clickable {
                                    WebViewActivity.gotoWeb(context, article.link, article.title)
                                }, article
                        )
                    }
                }
                when (loadState.append) {
                    LoadState.Loading -> {
                        item {
                            DataLoading()
                        }
                    }
                    is LoadState.Error -> {
                        item {
                            DataLoadError {
                                retry()
                            }
                        }
                    }
                }
            }
        }

    }

}


@Composable
fun WechatPageTop(title: String) {

    val isEnabled = remember { mutableStateOf(true) }
    val isRotated = remember { mutableStateOf(false) }

    val angle: Float by animateFloatAsState(
        targetValue = if (isRotated.value) -90F else 0F,
        animationSpec = tween(
            durationMillis = 500, // duration
            easing = FastOutSlowInEasing
        ),
        finishedListener = {
            // disable the button
            isEnabled.value = true
        }
    )
    Box(modifier = Modifier.fillMaxWidth().padding(10.dp, 5.dp)) {

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = title,
            fontSize = 20.sp,
            color = Color.Black
        )
        Icon(
            painter = painterResource(R.drawable.ic_more), contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .padding(6.dp).rotate(angle).clickable {
                    isRotated.value = !isRotated.value
                    isEnabled.value = false
                }
        )
    }
}

@Composable
@Preview
fun ShowWechatPageTop() {
    WechatPageTop("鸿洋")
}


@Composable
fun WechatArticleItem(modifier: Modifier, article: ArticleData) {
    Card(
        modifier = modifier,
        elevation = 5.dp,
        shape = RoundedCornerShape(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 8.dp)
        ) {

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (TextUtils.isEmpty(article.shareUser)) article.author else article.shareUser,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text = article.niceDate,
                    modifier = Modifier
                        .padding(start = 5.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 13.sp,
                    color = Color.Gray
                )

            }
            Text(
                text = article.title,
                modifier = Modifier
                    .padding(0.dp, 5.dp)
                    .fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp
            )
            var collect by remember {
                mutableStateOf(article.collect)
            }
            Row(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "${article.superChapterName} · ${article.chapterName}",
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Icon(
                    painter = painterResource(id = if (collect) R.drawable.ic_article_like else R.drawable.ic_article_unlike),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            collect = !collect
                            article.collect = collect
                        },
                    tint = Color(0xFF36C1BC)
                )
            }

        }
    }
}


@Composable
@Preview
fun ShowWechatArticleItem() {
    WechatArticleItem(Modifier.fillMaxWidth(), fakeArticleData()[0])
}