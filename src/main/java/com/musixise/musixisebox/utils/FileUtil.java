package com.musixise.musixisebox.utils;

import com.musixise.musixisebox.config.Constants;
import com.musixise.musixisebox.enums.FILE_TYPE;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhaowei on 2018/4/5.
 */
public class FileUtil {

     public static File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
        String filePath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator");
        File tmpFile = new File(filePath, multipart.getOriginalFilename());
        tmpFile.createNewFile();
        multipart.transferTo(tmpFile);
        return tmpFile;
    }

    public static String getFullFilePath(String fileName, FILE_TYPE fileType) {

        if (StringUtils.isNotBlank(fileName)) {
            if (fileName.indexOf("http") == -1) {
                if (fileType.equals(FILE_TYPE.IMAGE)) {
                    return String.format(Constants.QINIU_IMG_DOMAIN, fileName);
                } else if (fileType.equals(FILE_TYPE.AUDIO)) {
                    return String.format(Constants.QINIU_AUDIO_DOMAIN, fileName);
                }
            }
        }
        return fileName;
    }

    public static String getImageFullName(String name) {
         return getFullFilePath(name, FILE_TYPE.IMAGE);
    }

    public static String getAudioFullName(String name) {
         return getFullFilePath(name, FILE_TYPE.AUDIO);
    }

}
