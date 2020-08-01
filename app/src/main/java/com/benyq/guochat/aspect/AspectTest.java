package com.benyq.guochat.aspect;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.benyq.guochat.function.permissionX.PermissionCheckCallBack;
import com.benyq.guochat.function.permissionX.PermissionChecker;
import com.benyq.guochat.function.permissionX.PermissionX;
import com.benyq.mvvm.ext.Toasts;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

/**
 * @author benyq
 * @time 2020/7/30
 * @e-mail 1520063035@qq.com
 * @note Aspect 测试
 */
@Aspect
public class AspectTest {

    private static final String TAG = "benyqAspectTest";

    @Before("execution(* com.benyq.guochat.ui.base.BaseActivity.onCreate(..))")
    public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        Log.e(TAG, "onActivityMethodBefore: " + key);
    }


    @Pointcut("execution(@com.benyq.guochat.aspect.PermissionCheck * *(..)) && @annotation(ann)")
    public void checkPermission(PermissionCheck ann) {

    }

    // 碰到过的问题 ：
    // 1、不能使用lambda， 例如 PermissionCheckCallBack
    @Around("checkPermission(permissioncheck)")
    public void check(ProceedingJoinPoint joinPoint, PermissionCheck permissioncheck) throws Throwable {
        if (permissioncheck != null) {
            FragmentActivity context;
            if (joinPoint.getThis() instanceof Fragment) {
                context = ((Fragment) joinPoint.getThis()).requireActivity();
            }else {
                context = (FragmentActivity) joinPoint.getThis();
            }
            if (context != null) {
                String[] checkStrings = permissioncheck.checkString();
                PermissionChecker.request(context, new PermissionCheckCallBack() {
                    @Override
                    public void onResult(boolean success, List<String> denyList) {
                        if (success) {
                            try {
                                joinPoint.proceed();
                            }catch ( Throwable e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toasts.INSTANCE.show("权限" + denyList + "被拒绝!");
                        }
                    }
                }, checkStrings);
            }
        }else {
            joinPoint.proceed();
        }
    }
}