package org.hf.boot.springboot.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hf.boot.springboot.pojo.entity.UserInfo;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 流式导出测试
 */
@Component
public class StreamTestResultHandlerFactory implements ResultHandlerFactory {

    @Override
    public CommonResultHandler<UserInfo, UserInfoResp> getResultHandler(HttpServletResponse response) throws IOException {

        return new CommonResultHandler<UserInfo, UserInfoResp>(response, UserInfoResp.class) {
            @Override
            public UserInfoResp processing(UserInfo userInfo) {
                UserInfoResp userInfoResp = new UserInfoResp();
                userInfoResp.setUserId(userInfo.getId());
                userInfoResp.setName(userInfo.getName());
                return userInfoResp;
            }
        };
    }

    @Data
    public static class UserInfoResp implements Serializable {

        private static final long serialVersionUID = 4619452394571212761L;

        @ColumnWidth(16)
        @ExcelProperty(value = "用户id",index = 0)
        @ApiModelProperty(value = "用户id")
        private Integer userId;

        @ExcelProperty(value = "用户姓名",index = 1)
        @ApiModelProperty(value = "用户姓名")
        private String name;
    }
}