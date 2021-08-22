package com.benyq.guochat.wanandroid.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.benyq.guochat.wanandroid.R
import com.benyq.guochat.wanandroid.model.vm.LoginViewModel

/**
 *
 * @author benyq
 * @date 2021/8/5
 * @email 1520063035@qq.com
 */

@Composable
fun LoginPage(backAction: ()->Unit, viewModel: LoginViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(Color.White)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_close),
            contentDescription = null,
            modifier = Modifier.size(30.dp).clickable {
                backAction()
            }
        )
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Text(
                text = "登录",
                fontSize = 30.sp,
                fontWeight = FontWeight.W800,
                modifier = Modifier.padding(bottom = 25.dp)
            )

            TextField(
                value = viewModel.username,
                onValueChange = { viewModel.username = it },

                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start
                ),
                placeholder = {
                    Text("请输入账号")
                },
                trailingIcon = {
                    if (viewModel.username.isNotEmpty()) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    viewModel.username = ""
                                }
                        )
                    }
                },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, focusedIndicatorColor = Color.LightGray)
            )
            var showPassword by remember {
                mutableStateOf(false)
            }
            TextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                placeholder = {
                    Text("请输入密码")
                },
                visualTransformation = if (!showPassword) PasswordVisualTransformation('*')
                else VisualTransformation.None,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,

                    ),
                trailingIcon = {
                    Image(
                        painter = painterResource(id = if (showPassword) R.drawable.ic_psw_checked else R.drawable.ic_psw_uncheck),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                showPassword = !showPassword
                            }
                    )
                },
                 colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, focusedIndicatorColor = Color.LightGray)
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        viewModel.login()
                    }, modifier = Modifier
                        .padding(top = 30.dp)
                        .weight(3f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF36C1BC))
                ) {
                    Text(text = "登录", fontSize = 25.sp, color = Color.White)
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }


}

@Composable
@Preview()
fun ShowLoginPage() {
    LoginPage({})
}