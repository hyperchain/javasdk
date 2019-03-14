package cn.hyperchain.sdk.provider;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProviderManagerTest {
    public ProviderManager providerManager;
    public static String DEFAULTE_URL = "http://localhost:8081";

    @Before
    public void setUp() throws Exception {
        DefaultHttpProvider okhttpHttpProvider = DefaultHttpProvider.getInstance(DEFAULTE_URL);
        providerManager = new ProviderManager.Builder()
                .setHttpProviders(okhttpHttpProvider)
                .build();
    }

    @Test
    public void send() {
    }

    @Test
    public void sendRequest() {
    }
}