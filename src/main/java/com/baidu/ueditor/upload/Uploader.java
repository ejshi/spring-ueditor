package com.baidu.ueditor.upload;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baidu.ueditor.define.State;
import com.baidu.ueditor.storage.AbstractUEStorageTemplate;

public class Uploader {
	private HttpServletRequest request = null;
	private Map<String, Object> conf = null;

	
	public Uploader(HttpServletRequest request, Map<String, Object> conf) {
		this.request = request;
		this.conf = conf;
	}
	
	public final State doExec(AbstractUEStorageTemplate abstractStorageTemplate) {
        String filedName = (String) this.conf.get("fieldName");
        State state = null;

        if ("true".equals(this.conf.get("isBase64"))) {
            state = Base64Uploader.save(this.request.getParameter(filedName),
                    this.conf);
        } else {
            //使用新的存储策略，替换以前的存储方式
            //state = BinaryUploader.save(this.request, this.conf);
            state = abstractStorageTemplate.save(this.request, this.conf);
        }
        return state;
    }
}
