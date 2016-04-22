package org.apache.http.examples.client;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xman.utility.ClassPathResource;

import javax.net.ssl.SSLContext;
import java.io.IOException;

public abstract class HttpClient {
    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);

    public static void printResponse(CloseableHttpResponse response) {
        try (CloseableHttpResponse resp = response) {
            System.out.println("----------------------------------------");
            System.out.println(resp.getStatusLine());
            System.out.println(EntityUtils.toString(resp.getEntity()));
        } catch (IOException e) {
            log.error("Failed to output response", e);
        }
    }

    public static void executeRequest(HttpUriRequest request) {
        try {
            try (CloseableHttpClient httpclient = getSSLClient();
                 CloseableHttpResponse response = httpclient.execute(request)) {
                HttpEntity entity = response.getEntity();

                System.out.println(EntityUtils.toString(response.getEntity()));
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            log.error("Failed to execute request", e);
        }
    }

    public static CloseableHttpClient getSSLClient() throws Exception {
        // keystore file
        String file = System.getProperty("cert.file");
        // keystore password
        String pass = System.getProperty("cert.pass");
        // ssl version, such as TLSV1.2
        String ssl = System.getProperty("server.ssl");

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(
                        ClassPathResource.getAsURL(file),
                        pass.toCharArray(),
                        new TrustSelfSignedStrategy())
                .build();

        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{ssl},
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier()
        );

        return HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
    }
}
