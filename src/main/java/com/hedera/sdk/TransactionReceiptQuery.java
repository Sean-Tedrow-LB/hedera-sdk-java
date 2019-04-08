package com.hedera.sdk;

import com.hedera.sdk.proto.*;
import io.grpc.MethodDescriptor;

public final class TransactionReceiptQuery extends QueryBuilder<TransactionReceipt> {
    private final com.hedera.sdk.proto.TransactionGetReceiptQuery.Builder builder;

    public TransactionReceiptQuery() {
        super(TransactionReceipt::new);
        builder = inner.getTransactionGetReceiptBuilder();
    }

    @Override
    protected QueryHeader.Builder getHeaderBuilder() {
        return inner.getTransactionGetReceiptBuilder()
            .getHeaderBuilder();
    }

    public TransactionReceiptQuery setTransaction(TransactionId transactionId) {
        builder.setTransactionID(transactionId.inner);
        return this;
    }

    @Override
    protected MethodDescriptor<Query, Response> getMethod() {
        return CryptoServiceGrpc.getGetTransactionReceiptsMethod();
    }

    @Override
    protected void doValidate() {
        require(builder.hasTransactionID(), ".setTransaction() required");
    }
}
