package com.web_application_development.evoting;

import org.apache.commons.io.IOUtils;
import java.nio.charset.Charset;

public class IntegrationTestUtils {

    public static String getFile(String fileName) throws Exception {
        ClassLoader classLoader = IntegrationTestUtils.class.getClassLoader();
        return IOUtils.toString(classLoader.getResourceAsStream(fileName), Charset.defaultCharset());
    }
}