package com.zheng.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.File;

public class UIUtil implements InitializingBean, ServletContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZhengAdminUtil.class);

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        LOGGER.info("===== 开始解压web前端 --> UI =====");
        String webjarName = PropertiesFileUtil.getInstance("ui").get("ui.name");
        String version = PropertiesFileUtil.getInstance("ui").get("ui.version");
        LOGGER.info("ui.jar 版本: {}", version);
        String jarPath = servletContext.getRealPath("/WEB-INF/lib/ui-" + version + ".jar");
        LOGGER.info("ui.jar 包路径: {}", jarPath);
        String resources = servletContext.getRealPath("/resources/") + File.separator + webjarName;
        LOGGER.info("ui.jar 解压到: {}", resources);
        JarUtil.decompress(jarPath, resources);
        LOGGER.info("===== 解压ui完成 =====");
    }
}
