package com.benyq.guochat.wanandroid.ui.page

import android.os.Handler
import android.os.Looper
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.benyq.guochat.wanandroid.R
import com.benyq.guochat.wanandroid.model.BannerData
import com.google.accompanist.pager.*
import com.google.android.material.math.MathUtils
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun AppTopBar(
    title: String,
    onLeft: (() -> Unit)? = null,
    @DrawableRes leftIcon: Int? = null,
    onRight: (() -> Unit)? = null,
    @DrawableRes rightIcon: Int? = null
) {
    Row(
        modifier = Modifier
            .background(Color.White)
            .height(48.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        onLeft?.run {
            Icon(
                painter = painterResource(leftIcon ?: R.drawable.ic_back), contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .clickable(onClick = onLeft)
                    .padding(6.dp)
            )
        }
        Text(
            text = title, modifier = Modifier
                .weight(1f), fontSize = 18.sp, textAlign = TextAlign.Center
        )
        if (rightIcon != null) {
            Icon(
                painter = painterResource(rightIcon), contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .clickable(onClick = {
                        onRight?.invoke()
                    })
                    .padding(5.dp)
            )
        }
    }
}

@Preview
@Composable
fun ShowAppToBar() {
    AppTopBar("hello", rightIcon = R.drawable.ic_search, onLeft = {

    }, onRight = {

    })
}

@ExperimentalPagerApi
@Composable
fun Banner(
    modifier: Modifier,
    dataList: List<BannerData>
) {

    //Modifier.padding(top = 5.dp).fillMaxWidth().height(200.dp)
    Box(modifier = modifier) {
        val pagerState =
            rememberPagerState(pageCount = dataList.size, initialOffscreenLimit = dataList.size - 1)

        val handler = remember {
            Handler(Looper.getMainLooper())
        }
        val scope = rememberCoroutineScope()
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed(object : Runnable {
            override fun run() {
                scope.launch {
                    if (pagerState.pageCount > 0) {
                        pagerState.animateScrollToPage((pagerState.currentPage + 1) % pagerState.pageCount)
                    }
                }
                handler.postDelayed(this, 3000)
            }
        }, 3000)

        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxSize()
        ) { page ->
            val bannerData = dataList[page]
            Card(Modifier
                .graphicsLayer {
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                    MathUtils
                        .lerp(
                            0.75f,
                            1f,
                            1f - pageOffset.coerceIn(0f, 1f)
                        )
                        .also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }
                    alpha = MathUtils.lerp(
                        0.5f,
                        1f,
                        1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
                .clickable {

                }) {
                Image(
                    modifier = Modifier
                        .fillMaxSize(),
                    painter = rememberImagePainter(
                        data = bannerData.imagePath,
                        builder = {

                        }),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        )
    }

}


@Composable
fun DataLoading() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(end = 20.dp)
                .size(30.dp)
        )
        Text(
            text = "正在加载。。。",
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview
fun ShowDataLoading() {
    DataLoading()
}

@Composable
fun DataLoadError(errorAction: ()->Unit) {
    Text(
        modifier = Modifier.padding(15.dp)
            .fillMaxWidth()
            .clickable { errorAction() },
        text = "加载失败, 点击重试",
        color = Color.Black,
        textAlign = TextAlign.Center
    )
}

@Composable
@Preview
fun ShowDataLoadError() {
    DataLoadError{

    }
}



