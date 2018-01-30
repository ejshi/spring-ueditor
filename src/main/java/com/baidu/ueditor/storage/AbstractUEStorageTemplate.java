package com.baidu.ueditor.storage;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
/**
 * 存储文件
 * @author ThinkPad
 *
 */
public abstract class AbstractUEStorageTemplate {
    /**
     * 校验文件
     * @param file
     * @return
     */
    public static State checkFile(MultipartFile file , Map<String, Object> conf){
        if(file.isEmpty()){
            return new BaseState(false, AppInfo.NOT_EXIST);
        }
        //校验文件大小
        long maxSize = ((Long) conf.get("maxSize")).longValue();
        if (file.getSize() > maxSize) {
            return new BaseState(false, AppInfo.MAX_SIZE);
        }
        //文件后缀
        String suffix = FileType.getSuffixByFilename(file.getOriginalFilename());
        if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
            return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
        }
        return new BaseState(true, AppInfo.SUCCESS);
    }
    /**
     * 保存文件
     * @param request
     * @param conf config.json中的一些配置信息，详见com.baidu.ueditor.ConfigManager.getConfig(int)
     * @return
     */
    public State save(HttpServletRequest request, Map<String, Object> conf){
        if (!ServletFileUpload.isMultipartContent(request)) {
            return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
        }
        String upFile = (String) conf.get("fieldName");
        if(StringUtils.isEmpty(upFile)){
            return new BaseState(false, AppInfo.CONFIG_ERROR);
        }
        MultipartFile file = ((MultipartHttpServletRequest)request).getFile(upFile);
        
        State checkState = checkFile(file, conf);
        if(!checkState.isSuccess()){
            return checkState;
        }
        
        return saveFile(file, conf);
    }
    
    /**
     * 保存文件
     * <p>
     * 备注：保存成功返回相应的数据字段：url(存储路径)/type(文件后缀)/original(文件名称)<br>
     * 例如：<br>
        State storageState = new BaseState(true, AppInfo.SUCCESS);<br>
        storageState.putInfo("url", PathFormat.format("/upload/1232132.jpg"));<br>
        storageState.putInfo("type", ".jpg");<br>
        storageState.putInfo("original", "upload.jpg");<br>
     * <p>
     * @param file 上传文件
     * @param conf config.json中的一些配置信息，详见com.baidu.ueditor.ConfigManager.getConfig(int)
     * @return
     */
    public abstract State saveFile(MultipartFile file, Map<String, Object> conf);
    
    private static boolean validType(String type, String[] allowTypes) {
        List<String> list = Arrays.asList(allowTypes);
        return list.contains(type);
    }
    
}
