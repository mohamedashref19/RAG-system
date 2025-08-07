package org.example.arabic.config;
import io.milvus.client.MilvusServiceClient;
import io.milvus.param.ConnectParam;
public class MilvusConfig {
    private static final String HOST = "localhost";
    private static final int PORT = 19530;

    public static MilvusServiceClient createMilvusClient() {
        ConnectParam connectParam = ConnectParam.newBuilder()
                .withHost(HOST)
                .withPort(PORT)
                .build();

        return new MilvusServiceClient(connectParam);
    }
}
