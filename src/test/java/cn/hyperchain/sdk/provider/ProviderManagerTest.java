package cn.hyperchain.sdk.provider;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProviderManagerTest {
    public ProviderManager providerManager;
    public static String DEFAULT_URL = "localhost:9999";

    @Before
    public void setUp() throws Exception {
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL).build();
        providerManager = ProviderManager.createManager(defaultHttpProvider);
    }

    @Test
    public void send() {
    }

    @Test
    public void sendRequest() {
    }
}