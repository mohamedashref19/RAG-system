package org.example.arabic.service;

import io.milvus.client.MilvusServiceClient;
import io.milvus.param.R;
import io.milvus.param.RpcStatus;
import io.milvus.param.collection.CreateCollectionParam;
import io.milvus.grpc.DataType;
import io.milvus.param.collection.FieldType;
import org.example.arabic.config.MilvusConfig;

public class MilvusService {

    private final MilvusServiceClient milvusClient;

    public MilvusService() {
        this.milvusClient = MilvusConfig.createMilvusClient();
    }

    public void createCollection(String collectionName, int dimension) {
        FieldType idField = FieldType.newBuilder()
                .withName("id")
                .withDataType(DataType.Int64)
                .withPrimaryKey(true)
                .withAutoID(true)
                .build();

        FieldType vectorField = FieldType.newBuilder()
                .withName("embedding")
                .withDataType(DataType.FloatVector)
                .withDimension(dimension)
                .build();

        CreateCollectionParam param = CreateCollectionParam.newBuilder()
                .withCollectionName(collectionName)
                .withDescription("Collection for handwritten character embeddings")
                .withShardsNum(1)
                .addFieldType(idField)
                .addFieldType(vectorField)
                .build();

        R<RpcStatus> response = milvusClient.createCollection(param);

        if (response.getStatus() == 0) {  // 0 غالباً معناها نجاح العملية
            System.out.println("Collection created successfully!");
        } else {
            System.err.println("Failed to create collection: " + response.getStatus());
        }

    }
}
