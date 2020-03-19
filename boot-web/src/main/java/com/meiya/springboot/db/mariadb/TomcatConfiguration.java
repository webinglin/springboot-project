package com.meiya.springboot.db.mariadb;

import com.meiya.springboot.tomcat.TldSkipDefinedPatterns;
import org.apache.catalina.Context;
import org.apache.catalina.core.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.Set;
import java.util.stream.Stream;

/**
 * tomcat配置
 * @author linwb
 * @since  2019-12-18
 */
@Configuration
public class TomcatConfiguration {
    protected static final Logger logger = LoggerFactory.getLogger(TomcatConfiguration.class);

    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        TomcatServletWebServerFactory fac = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                super.postProcessContext(context);
            }
        };
        fac.addTldSkipPatterns(TldSkipDefinedPatterns.ADDITIONAL.toArray(new String[0]));
        return fac;
    }

    @Bean
    public ServletContextInitializer preCompileJspAtStartUp() {
        return servletContext -> {
            getDeepResourcePaths(servletContext, "/WEB-INF/jsp/").forEach(jspPath ->{
                logger.info("REGISTEING JSP: " + jspPath);
                ServletRegistration.Dynamic reg = servletContext.addServlet(jspPath, Constants.JSP_SERVLET_CLASS);
                reg.setInitParameter("jspFile", jspPath);
                reg.setLoadOnStartup(99);
                reg.addMapping(jspPath);
//                reg.setInitParameter("development","false");
            });
        };
    }

    private Stream<String> getDeepResourcePaths(ServletContext servletContext, String path){
        if(!path.endsWith(com.meiya.springboot.common.constants.Constants.SLASH)) {
            return Stream.of(path);
        }
        Set<String> resourcePaths = servletContext.getResourcePaths(path);
        if(CollectionUtils.isEmpty(resourcePaths)){
            return Stream.of();
        }
        return resourcePaths.stream().flatMap(p -> getDeepResourcePaths(servletContext, p));
    }
}
