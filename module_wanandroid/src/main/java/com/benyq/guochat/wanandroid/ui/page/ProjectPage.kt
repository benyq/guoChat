package com.benyq.guochat.wanandroid.ui.page

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.benyq.guochat.wanandroid.R
import com.benyq.guochat.wanandroid.model.ArticleData
import com.benyq.guochat.wanandroid.model.fakeArticleData
import com.benyq.guochat.wanandroid.model.vm.ProjectViewModel
import com.benyq.guochat.wanandroid.ui.theme.GrayApp
import com.benyq.module_base.ext.loge
import com.benyq.module_base.ui.WebViewActivity
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 *
 * @author benyq
 * @date 2021/8/30
 * @email 1520063035@qq.com
 * 类似于 可移动的Dialog 还未实现
 */
@ExperimentalCoilApi
@Composable
@Preview
fun ShowProjectPage() {
    ProjectPage()
}

@ExperimentalCoilApi
@Composable
@Preview
fun ShowProjectItem() {
    ProjectItem(
        fakeArticleData()[0], Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(10.dp, 10.dp)
    )
}

@ExperimentalCoilApi
@Composable
fun ProjectPage(projectViewModel: ProjectViewModel = viewModel()) {

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val articleData = projectViewModel.pager.collectAsLazyPagingItems()
        val selectedCid by projectViewModel.selectedCid.observeAsState()
        selectedCid?.run {
            if (this.isNotEmpty()) {
                Log.e("benyq", "ProjectPage: $this")
                articleData.refresh()
            }
        }
        val listState = rememberLazyListState()
        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
            articleData.apply {
                itemsIndexed(this) { index, article ->
                    ProjectItem(article!!, Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(10.dp, 10.dp)
                        .clickable {
                            WebViewActivity.gotoWeb(context, article.link, article.title)
                        })
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFEDEDED))
                    )
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

        Button(modifier = Modifier
            .padding(10.dp)
            .size(80.dp, 40.dp)
            .align(Alignment.BottomEnd), onClick = {
            projectViewModel.changeCategory()
        }) {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                fontSize = 16.sp, color = Color.White, text = "刷新"
            )
        }
    }
}


@ExperimentalCoilApi
@Composable
fun ProjectItem(articleData: ArticleData, modifier: Modifier) {
    Row(modifier = modifier) {
        Image(
            painter = rememberImagePainter(
                data = articleData.envelopePic,
                builder = {
                    placeholder(R.drawable.empty_drawable)
                }), contentDescription = null, modifier = Modifier
                .weight(1f)
                .fillMaxHeight(), contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(3f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(), maxLines = 2, overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp, color = Color.Black, text = articleData.title
            )
            Text(
                modifier = Modifier.fillMaxWidth(), maxLines = 1, overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp, color = Color(0xFF999999), text = articleData.desc
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                var collect by remember {
                    mutableStateOf(articleData.collect)
                }
                Text(
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    color = Color(0xffAAAAAA),
                    text = if (articleData.author.isEmpty()) articleData.shareUser else articleData.author
                )
                Icon(
                    painter = painterResource(id = if (collect) R.drawable.ic_article_like else R.drawable.ic_article_unlike),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            collect = !collect
                            articleData.collect = collect
                        },
                    tint = Color(0xFF36C1BC)
                )
            }
        }

    }
}