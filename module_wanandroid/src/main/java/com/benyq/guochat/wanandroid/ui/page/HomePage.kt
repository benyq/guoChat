package com.benyq.guochat.wanandroid.ui.page

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import com.benyq.guochat.wanandroid.model.UserData
import com.benyq.guochat.wanandroid.model.vm.HomeViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import com.benyq.guochat.wanandroid.R
import com.benyq.guochat.wanandroid.ui.theme.GrayApp
import com.benyq.module_base.ext.loge
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * @author benyq
 * @time 2021/8/7
 * @e-mail 1520063035@qq.com
 * @note
 */

@ExperimentalCoilApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun HomePage(viewModel: HomeViewModel = viewModel()) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(GrayApp)
    when (viewModel.currentPosition) {
        0, 1, 2 -> {
            systemUiController.setStatusBarColor(GrayApp)
        }
        3 -> {
            systemUiController.setStatusBarColor(Color.White)
        }
        4 -> {
            systemUiController.setStatusBarColor(Color(0xFF36C1BC))
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        val scope = rememberCoroutineScope()
        val pagerState = rememberPagerState(pageCount = 5)

        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxWidth()
                .weight(1f), dragEnabled = false
        ) { page ->
            when (page) {
                0 -> {
                    MainPage()
                }
                1 -> {
                    ProjectPage()
                }
                2 -> {
                    LoginPage({})
                }
                3 -> {
                    WechatPage()
                }
                4 -> {
                    val test = MutableLiveData<UserData>()
                    MePage(userLiveData = test, clickAction = MineClickAction())
                }
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )

        HomeBottomBar(current = viewModel.currentPosition, currentChanged = {
            viewModel.currentPosition = it
            scope.launch {
                pagerState.scrollToPage(it)
            }
        })
    }
}


@ExperimentalAnimationApi
@Composable
fun HomeBottomBar(current: Int, currentChanged: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp, 2.dp)
            .fillMaxWidth()
            .background(Color.White)
            .padding(4.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HomeBottomBarItem(
            R.drawable.ic_tab_main, "首页",
            Color(0xFFF44336),
            Color(0xFFE57373),
            current == 0,
        ) {
            currentChanged(0)
        }
        HomeBottomBarItem(
            R.drawable.ic_tab_project,
            "项目",
            Color(0xFF03A9F4),
            Color(0xFF64B5F6),
            current == 1
        ) {
            currentChanged(1)
        }
        HomeBottomBarItem(
            R.drawable.ic_tab_square,
            "广场",
            Color(0xFF607D8B),
            Color(0xFF90A4AE),
            current == 2
        ) {
            currentChanged(2)
        }
        HomeBottomBarItem(
            R.drawable.ic_tab_wechat,
            "公众号",
            Color(0xFF4CAF50),
            Color(0xFF81C784),
            current == 3
        ) {
            currentChanged(3)
        }
        HomeBottomBarItem(
            R.drawable.ic_tab_mine, "我的",
            Color(0xFF9C27B0),
            Color(0xFFBA68C8),
            current == 4
        ) {
            currentChanged(4)
        }

    }

}

@ExperimentalAnimationApi
@Composable
fun HomeBottomBarItem(
    @DrawableRes iconId: Int,
    title: String,
    tintActive: Color,
    tintInactive: Color,
    selected: Boolean,
    clickAction: () -> Unit
) {

    val transition = updateTransition(selected, label = "barItem")
    val color by transition.animateColor(label = "barItemColor") { bool ->
        if (bool) Color(tintInactive.red, tintInactive.green, tintInactive.blue, 0.5f) else Color(
            tintInactive.red,
            tintInactive.green,
            tintInactive.blue,
            0f
        )
    }
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .clickable(interactionSource, indication = null) {
                clickAction.invoke()
            }
            .padding(0.dp, 8.dp, 0.dp, 8.dp)
            .background(color, shape = RoundedCornerShape(24.dp))
            .padding(13.dp, 9.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = if (selected) tintActive else tintInactive
        )
        AnimatedVisibility(selected) {
            Text(
                modifier = Modifier.padding(start = 6.dp),
                text = title,
                fontSize = 14.sp,
                color = if (selected) tintActive else tintInactive
            )
        }
    }
}


@ExperimentalAnimationApi
@Composable
@Preview("Animations")
fun ShowHomeBottomBar() {
    HomeBottomBar(2, { index ->

    })
}
