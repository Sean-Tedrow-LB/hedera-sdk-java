package com.hedera.hashgraph.sdk;

import com.hedera.hashgraph.sdk.proto.Query;
import com.hedera.hashgraph.sdk.proto.QueryHeader;
import com.hedera.hashgraph.sdk.proto.Response;
import com.hedera.hashgraph.sdk.proto.ResponseHeader;
import com.hedera.hashgraph.sdk.proto.ScheduleGetInfoQuery;
import com.hedera.hashgraph.sdk.proto.ScheduleServiceGrpc;
import io.grpc.MethodDescriptor;
import java8.util.concurrent.CompletableFuture;

import javax.annotation.Nullable;
import java.util.Objects;

public class ScheduleInfoQuery extends com.hedera.hashgraph.sdk.Query<ScheduleInfo, ScheduleInfoQuery> {
    @Nullable
    private ScheduleId scheduleId = null;

    public ScheduleInfoQuery() {
    }

    @Nullable
    public ScheduleId getScheduleId() {
        return scheduleId;
    }

    public ScheduleInfoQuery setScheduleId(ScheduleId scheduleId) {
        Objects.requireNonNull(scheduleId);
        this.scheduleId = scheduleId;
        return this;
    }

    @Override
    void validateChecksums(Client client) throws BadEntityIdException {
        if (scheduleId != null) {
            scheduleId.validateChecksum(client);
        }
    }

    @Override
    void onMakeRequest(com.hedera.hashgraph.sdk.proto.Query.Builder queryBuilder, QueryHeader header) {
        var builder = ScheduleGetInfoQuery.newBuilder();
        if (scheduleId != null) {
            builder.setScheduleID(scheduleId.toProtobuf());
        }

        queryBuilder.setScheduleGetInfo(builder.setHeader(header));
    }

    @Override
    ResponseHeader mapResponseHeader(Response response) {
        return response.getScheduleGetInfo().getHeader();
    }

    @Override
    QueryHeader mapRequestHeader(com.hedera.hashgraph.sdk.proto.Query request) {
        return request.getScheduleGetInfo().getHeader();
    }

    @Override
    ScheduleInfo mapResponse(Response response, AccountId nodeId, Query request) {
        return ScheduleInfo.fromProtobuf(response.getScheduleGetInfo());
    }

    @Override
    MethodDescriptor<Query, Response> getMethodDescriptor() {
        return ScheduleServiceGrpc.getGetScheduleInfoMethod();
    }

    @Override
    public CompletableFuture<Hbar> getCostAsync(Client client) {
        // deleted accounts return a COST_ANSWER of zero which triggers `INSUFFICIENT_TX_FEE`
        // if you set that as the query payment; 25 tinybar seems to be enough to get
        // `Token_DELETED` back instead.
        return super.getCostAsync(client).thenApply((cost) -> Hbar.fromTinybars(Math.max(cost.toTinybars(), 25)));
    }
}
