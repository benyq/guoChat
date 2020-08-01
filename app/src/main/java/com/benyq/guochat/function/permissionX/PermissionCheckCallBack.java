package com.benyq.guochat.function.permissionX;

import java.util.List;

/**
 * @author benyq
 * @time 2020/7/30
 * @e-mail 1520063035@qq.com
 * @note
 */
public interface PermissionCheckCallBack {
    void onResult(boolean success, List<String> denyList);
}
