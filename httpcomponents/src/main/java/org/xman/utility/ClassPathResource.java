package org.xman.utility;

import java.io.InputStream;
import java.net.URL;

public class ClassPathResource {

    public static InputStream getAsStream(String name) {

        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
        if (stream == null) {
            throw new RuntimeException("Can not find out resource with name=" + name);
        }

        return stream;
    }

    public static URL getAsURL(String name) {

        URL url = Thread.currentThread().getContextClassLoader().getResource(name);
        if (url == null) {
            throw new RuntimeException("Can not find out resource with name=" + name);
        }

        return url;
    }

    public static String getFilePath(String name) {
        URL url = getAsURL(name);

        return url.getFile().substring(1);
    }
}
