package com.benyq.guochat.wanandroid.ui.page

import android.text.TextUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.annotation.ExperimentalCoilApi
import com.benyq.guochat.wanandroid.R
import com.benyq.guochat.wanandroid.model.ArticleData
import com.benyq.guochat.wanandroid.model.vm.MainViewModel
import com.benyq.module_base.ui.WebViewActivity
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun MainPage(mainViewModel: MainViewModel = viewModel()) {

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Black)
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {

        AppTopBar("WanAndroid", functionIcon = R.drawable.ic_search, onFunction = {

        })

        val articleData = mainViewModel.pager.collectAsLazyPagingItems()
        val bannerData by mainViewModel.bannerData.observeAsState()
        LazyColumn(
            modifier = Modifier
                .padding(top = 0.dp)
                .weight(1f)
        ) {
            bannerData?.let { data ->
                item {
                    Banner(
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .fillMaxWidth()
                            .height(200.dp), dataList = data
                    )
                    Spacer(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp))
                }
            }
            articleData.apply {
                itemsIndexed(this) { index, article ->
                    ArticleItem(article = article!!) {
                        WebViewActivity.gotoWeb(context, article.link, article.title)
                    }
                }
                when (loadState.append) {
                    LoadState.Loading -> {
                        item {
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
                                    modifier = Modifier
                                        .clickable { retry() },
                                    text = "正在加载。。。",
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    is LoadState.Error -> {
                        item {
                            Text(
                                modifier = Modifier.padding(15.dp)
                                    .fillMaxWidth()
                                    .clickable { retry() },
                                text = "加载失败, 点击重试",
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
    mainViewModel.bannerData()
    mainViewModel.getArticleData()
}


@ExperimentalPagerApi
@Composable
@Preview
fun ShowMainPage() {
    MainPage()
}

@Composable
@Preview
fun ShowArticleItem() {
    ArticleItem(article = fakeData()[0]) {

    }
}

@Composable
fun ArticleItem(article: ArticleData, itemClickAction: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = itemClickAction),
        elevation = 5.dp,
        shape = RoundedCornerShape(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp, 8.dp)
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
                        },
                    tint = Color(0xFF36C1BC)
                )
            }

        }
    }
}

fun fakeData(): List<ArticleData> {
    val article = ArticleData(
        apkLink = "",
        audit = 1,
        author = "张鸿洋",
        canEdit = false,
        chapterId = 543,
        chapterName = "Android技术周报",
        collect = false,
        courseId = 13,
        desc = "",
        descMd = "",
        envelopePic = "",
        fresh = false,
        host = "",
        id = 19132,
        link = "https://www.wanandroid.com/blog/show/3039",
        niceDate = "2021-07-28 00:00",
        niceShareDate = "2021-07-28 00:10",
        origin = "",
        prefix = "",
        projectLink = "",
        publishTime = 1627401600000,
        realSuperChapterId = 542,
        selfVisible = 0,
        shareDate = 1627402200000,
        shareUser = "",
        superChapterId = 543,
        superChapterName = "技术周报",
        tags = listOf(),
        title = "Android 技术周刊 （2021-07-21 ~ 2021-07-28）",
        type = 0,
        userId = -1,
        visible = 1,
        zan = 0
    )

    return listOf(article, article, article, article, article, article, article)
}
