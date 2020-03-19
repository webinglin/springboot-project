/*
 * Copyright (c) 2012-2022 厦门市美亚柏科信息股份有限公司.
 */

package com.meiya.springboot.tomcat;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 自定义的忽略扫描TLD文件的JAR包
 * @author linwb
 * @since 2019-04-23
 */
public class TldSkipDefinedPatterns {
    public static final Set<String> ADDITIONAL;
    static {
        Set<String> patterns = new LinkedHashSet<>();
        patterns.add("udunits-*.jar");
        patterns.add("httpservices-*.jar");
        patterns.add("httpmime-*.jar");
        patterns.add("httpcore-*.jar");
        patterns.add("joda-time-*.jar");
        patterns.add("jdom2-*.jar");
        patterns.add("quartz-*.jar");
        patterns.add("c3p0-*.jar");
        patterns.add("protobuf-java-*.jar");
        patterns.add("guava-*.jar");
        patterns.add("bzip2-*.jar");
        patterns.add("jcommander-*.jar");
        patterns.add("jcip-annotations-*.jar");
        patterns.add("jna-*.jar");
        patterns.add("jsr-275-*.jar");
        patterns.add("geoapi-*.jar");
        patterns.add("sis-utility-*.jar");
        patterns.add("sis-metadata-*.jar");
        patterns.add("sis-referencing-*.jar");
        patterns.add("sis-storage-*.jar");
        patterns.add("jwnl-*.jar");
        patterns.add("opennlp-maxent-*.jar");
        patterns.add("xmpcore-*.jar");
        patterns.add("regexp-*.jar");
        patterns.add("maven-scm-provider-svn-commons-*.jar");
        patterns.add("maven-scm-provider-svnexe-*.jar");
        patterns.add("plexus-utils-*.jar");
        patterns.add("maven-scm-api-*.jar");
        patterns.add("commons-vfs2-*.jar");
        patterns.add("jj2000-*.jar");
        patterns.add("jsoup-*.jar");
        patterns.add("cdm-*.jar");
        patterns.add("jaxb1-impl.jar");
        patterns.add("jaxb-impl.jar");
        patterns.add("jsr173_1.0_api.jar");
        patterns.add("activation.jar");
        patterns.add("jaxb-api.jar");

        ADDITIONAL = Collections.unmodifiableSet(patterns);
    }

}
