package org.hf.boot.springboot.security.encryption;

/**
 * 接口出入参加解密内容上下文
 */
public class ApiDecAndEncContext {

    private static final ThreadLocal<ApiDecAndEncDTO> THREAD_LOCAL_API = new ThreadLocal<>();

    public static void addApiDecAndEncDto(ApiDecAndEncDTO dto) {
        THREAD_LOCAL_API.set(dto);
    }

    public static ApiDecAndEncDTO getApiDecAndEncDto() {
        return THREAD_LOCAL_API.get();
    }

    public static void removeContext() {
        THREAD_LOCAL_API.remove();
    }

}