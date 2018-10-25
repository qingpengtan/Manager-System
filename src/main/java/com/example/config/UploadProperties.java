package com.example.config;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("upload")
public class UploadProperties {
    // 获取存放位置
    private Map<String, String> localtion;

    public static final String FILE_TYPE_IMAGE = "image";
    public static final String FILE_TYPE_AUDIO = "music";
    public static final String FILE_TYPE_VDIEO = "vdieo";

    // 单个文件大小
    private String maxFileSize;

    // 单次上传总文件大小
    private String maxRequestSize;

    public Map<String, String> getLocaltion() {
        return localtion;
    }

    public void setLocaltion(Map<String, String> localtion) {
        this.localtion = localtion;
    }

    public String getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(String maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String getMaxRequestSize() {
        return maxRequestSize;
    }

    public void setMaxRequestSize(String maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    public String getBasePath(String fileType) {
        if(StringUtils.isEmpty(fileType)){
            fileType = UploadProperties.FILE_TYPE_IMAGE;
        }
        String location = "";
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")) {
            location = this.getLocaltion().get("windows");
        } else {
            if(fileType.equals(UploadProperties.FILE_TYPE_AUDIO)){
                location = this.getLocaltion().get(" linux-audio");
            }else if(fileType.equals(UploadProperties.FILE_TYPE_VDIEO)){
                location = this.getLocaltion().get("linux-video");
            }else  {
                location = this.getLocaltion().get("linux-image");
            }
        }
        return location;
    }
}