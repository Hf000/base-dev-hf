package org.hf.boot.springboot.constants;

/**
 * 文件类型枚举
 */
public enum FileTypeEnum {

    /**
     * JPEG/JPG
     */
    JPEG("FFD8FF", "jpg", "image/jpeg"),

    /**
     * PNG
     */
    PNG("89504E47", "png", "image/png"),

    /**
     * GIF
     */
    GIF("47494638", "gif", "image/gif"),

    /**
     * TIFF
     */
    TIFF("49492A00"),

    /**
     * Windows bitmap
     */
    BMP("424D"),

    /**
     * CAD
     */
    DWG("41433130"),

    /**
     * Adobe photoshop
     */
    PSD("38425053"),

    /**
     * Rich Text Format
     */
    RTF("7B5C727466"),

    /**
     * XML
     */
    XML("3C3F786D6C", null, "text/xml"),

    /**
     * HTML
     */
    HTML("68746D6C3E"),

    /**
     * Outlook Express
     */
    DBX("CFAD12FEC5FD746F"),

    /**
     * Outlook
     */
    PST("2142444E"),

    /**
     * doc;xls;dot;ppt;xla;ppa;pps;pot;msi;sdw;db
     */
    OLE2("0xD0CF11E0A1B11AE1"),

    /**
     * Microsoft Word/Excel
     */
    XLS_DOC("D0CF11E0"),

    /**
     * Microsoft Access
     */
    MDB("5374616E64617264204A"),

    /**
     * Word Perfect
     */
    WPB("FF575043"),

    /**
     * Postscript
     */
    EPS_PS("252150532D41646F6265"),

    /**
     * Adobe Acrobat
     */
    PDF("255044462D312E", null, "application/pdf"),

    /**
     * Windows Password
     */
    PWL("E3828596"),

    /**
     * ZIP Archive
     */
    ZIP("504B0304"),

    /**
     * ARAR Archive
     */
    RAR("52617221"),

    /**
     * WAVE
     */
    WAV("57415645"),

    /**
     * AVI
     */
    AVI("41564920"),

    /**
     * Real Audio
     */
    RAM("2E7261FD"),

    /**
     * Real Media
     */
    RM("2E524D46"),

    /**
     * Quicktime
     */
    MOV("6D6F6F76"),

    /**
     * Windows Media
     */
    ASF("3026B2758E66CF11"),

    /**
     * MIDI
     */
    MID("4D546864");

    /**
     * 文件头信息
     */
    private String value = "";
    private String ext = "";
    private String contextType = "";

    FileTypeEnum(String value) {
        this.value = value;
    }

    FileTypeEnum(String value, String ext) {
        this(value);
        this.ext = ext;
    }

    FileTypeEnum(String value, String ext, String contextType) {
        this(value, ext);
        this.contextType = contextType;
    }

    public String getExt() {
        return ext;
    }

    public String getValue() {
        return value;
    }

    public String getContextType() {
        return contextType;
    }

}
