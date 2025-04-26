package org.hf.boot.springboot.config;

import org.hf.boot.springboot.pojo.dto.UserInfoDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.task.TaskDecorator;

/**
 * 通用线程池任务装饰器
 * 在多线程中实现公共参数共享 - 1
 */
public class CustomThreadPoolTaskDecorator implements TaskDecorator {

    @Override
    @NotNull
    public Runnable decorate(@NotNull Runnable runnable) {
        UserInfoDTO userInfoDto = UserContext.getUser();
        return () -> {
            try {
                if (userInfoDto != null) {
                    userInfoDto.setSystemCode("default");
                    UserContext.addUser(userInfoDto);
                }
                runnable.run();
            } finally {
                UserContext.removeUser();
            }
        };
    }

}