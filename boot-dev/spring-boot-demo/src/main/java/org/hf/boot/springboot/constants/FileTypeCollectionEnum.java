package org.hf.boot.springboot.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * 文件（附件）类型枚举
 */
public enum FileTypeCollectionEnum {

    /**
     * 枚举项
     */
    PIC(1, new String[]{".jpg", ".png", ".jpeg"}, "application/octet-stream;charset=UTF-8"),
    DOC(2, new String[]{".doc", ".docx"}, "application/doc;charset=UTF-8"),
    PDF(3, new String[]{".pdf"}, "application/pdf;charset=UTF-8"),
    PPT(4, new String[]{".ppt", ".pptx"}, "application/ppt;charset=UTF-8"),
    EXCEL(5, new String[]{".xls", ".xlsx"}, "application/vnd.ms-excel;charset=UTF-8"),
    AUDIO(6, new String[]{".spx"}, "application/octet-stream"),
    MPEG(7, new String[]{".wav", ".mp3", ".ogg"}, "audio/mpeg");

    FileTypeCollectionEnum(Integer type, String[] fileSuffixArray, String contextType) {
        this.type = type;
        this.fileSuffixArray = fileSuffixArray;
        this.responseContextType = contextType;
    }

    private final Integer type;
    private final String[] fileSuffixArray;
    private final String responseContextType;

    public Integer getType() {
        return type;
    }

    public String[] getFileSuffixArray() {
        return fileSuffixArray;
    }

    public String getResponseContextType() {
        return responseContextType;
    }

    /**
     * 根据文件名称获取当前文件类型
     * @param fileName 文件名称
     */
    public static Integer getTypeByFileSuffix(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        for (FileTypeCollectionEnum typeEnum : FileTypeCollectionEnum.values()) {
            for (String fileSuffix : typeEnum.getFileSuffixArray()) {
                if (fileName.toLowerCase().endsWith(fileSuffix.toLowerCase())) {
                    return typeEnum.getType();
                }
            }
        }
        return null;
    }

}