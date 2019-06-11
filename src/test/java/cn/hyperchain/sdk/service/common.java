package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.HttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Common {

    public static final String tlsca = "certs/tls/tlsca.ca";
    public static final String tls_peer_cert = "certs/tls/tls_peer.cert";
    public static final String tls_peer_priv = "certs/tls/tls_peer.priv";
    public static final String sdkcert_cert = "certs/sdkcert.cert";
    public static final String sdkcert_priv = "certs/sdkcert.priv";
    public static final String unique_pub = "certs/unique.pub";
    public static final String unique_priv = "certs/unique.priv";

    public static InputStream tlsca_is = null;
    public static InputStream tls_peer_cert_is = null;
    public static InputStream tls_peer_priv_is = null;
    public static InputStream sdkcert_cert_is = null;
    public static InputStream sdkcert_priv_is = null;
    public static InputStream unique_pub_is = null;
    public static InputStream unique_priv_is = null;

    public static final String node1 = "localhost:8081";
    public static final String node2 = "localhost:8082";
    public static final String node3 = "localhost:8083";
    public static final String node4 = "localhost:8084";
    public static ProviderManager providerManager;

    static {

        try {
            sdkcert_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sdkcert_cert);
            sdkcert_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sdkcert_priv);
            unique_pub_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(unique_pub);
            unique_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(unique_priv);

            tlsca_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tlsca);
            tls_peer_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_cert);
            tls_peer_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_priv);
            HttpProvider httpProvider1 = new DefaultHttpProvider.Builder()
                    .setUrl(node1)
                    .https(tlsca_is, tls_peer_cert_is, tls_peer_priv_is)
                    .build();

            tlsca_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tlsca);
            tls_peer_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_cert);
            tls_peer_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_priv);
            HttpProvider httpProvider2 = new DefaultHttpProvider.Builder()
                    .setUrl(node2)
                    .https(tlsca_is, tls_peer_cert_is, tls_peer_priv_is)
                    .build();

            tlsca_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tlsca);
            tls_peer_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_cert);
            tls_peer_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_priv);
            HttpProvider httpProvider3 = new DefaultHttpProvider.Builder()
                    .setUrl(node3)
                    .https(tlsca_is, tls_peer_cert_is, tls_peer_priv_is)
                    .build();

            tlsca_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tlsca);
            tls_peer_cert_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_cert);
            tls_peer_priv_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(tls_peer_priv);
            HttpProvider httpProvider4 = new DefaultHttpProvider.Builder()
                    .setUrl(node4)
                    .https(tlsca_is, tls_peer_cert_is, tls_peer_priv_is)
                    .build();

            providerManager = new ProviderManager.Builder()
                    .namespace("global")
                    .providers(httpProvider1, httpProvider2, httpProvider3, httpProvider4)
                    .enableTCert(sdkcert_cert_is, sdkcert_priv_is, unique_pub_is, unique_priv_is)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
