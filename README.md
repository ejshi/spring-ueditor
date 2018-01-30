# spring-ueditor
spring、ueditor集成

引入spring-ueditor的jar包，继承AbstractUEStorageTemplate.java抽象类，实现saveFile接口，如下
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
    
  
  该接口用户实现用户使用UE上传的文件的保存，保存策略支持自定义，如保存到OSS、本地等

