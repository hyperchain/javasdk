package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.HttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;

public class common {

    public static final String tlsca = "certs/tls/tlsca.ca";
    public static final String tls_peer_cert = "certs/tls/tls_peer.cert";
    public static final String tls_peer_priv = "certs/tls/tls_peer.priv";
    public static final String sdkcert_cert = "certs/sdkcert.cert";
    public static final String sdkcert_priv = "certs/sdkcert.priv";
    public static final String unique_pub = "certs/unique.pub";
    public static final String unique_priv = "certs/unique.priv";

    public static final String node1 = "localhost:8081";
    public static final String node2 = "localhost:8082";
    public static final String node3 = "localhost:8083";
    public static final String node4 = "localhost:8084";
    public static ProviderManager providerManager;

    static {
        HttpProvider httpProvider1 = new DefaultHttpProvider.Builder()
                .setUrl(node1)
//                .https(tlsca, tls_peer_cert, tls_peer_priv)
                .build();
        HttpProvider httpProvider2 = new DefaultHttpProvider.Builder()
                .setUrl(node2)
                .https(tlsca, tls_peer_cert, tls_peer_priv)
                .build();
        HttpProvider httpProvider3 = new DefaultHttpProvider.Builder()
                .setUrl(node3)
                .https(tlsca, tls_peer_cert, tls_peer_priv)
                .build();
        HttpProvider httpProvider4 = new DefaultHttpProvider.Builder()
                .setUrl(node4)
                .https(tlsca, tls_peer_cert, tls_peer_priv)
                .build();
        providerManager = new ProviderManager.Builder()
                .namespace("global")
//                .providers(httpProvider1, httpProvider2, httpProvider3, httpProvider4)
                .providers(httpProvider1)
//                .enableTCert(sdkcert_cert, sdkcert_priv, unique_pub, unique_priv)
                .build();
    }
}
