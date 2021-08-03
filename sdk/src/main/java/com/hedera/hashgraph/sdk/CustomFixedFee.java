package com.hedera.hashgraph.sdk;

import com.google.common.base.MoreObjects;
import javax.annotation.Nullable;
import com.hedera.hashgraph.sdk.proto.FixedFee;

public class CustomFixedFee extends CustomFee {
    private long amount = 0;
    @Nullable
    private TokenId denominatingTokenId = null;

    public CustomFixedFee() {
    }

    static CustomFixedFee fromProtobuf(com.hedera.hashgraph.sdk.proto.CustomFee customFee) {
        var fixedFee = customFee.getFixedFee();
        var returnFee = new CustomFixedFee().setAmount(fixedFee.getAmount());
        if(customFee.hasFeeCollectorAccountId()) {
            returnFee.setFeeCollectorAccountId(AccountId.fromProtobuf(customFee.getFeeCollectorAccountId()));
        }
        if(fixedFee.hasDenominatingTokenId()) {
            returnFee.setDenominatingTokenId(TokenId.fromProtobuf(fixedFee.getDenominatingTokenId()));
        }
        return returnFee;
    }

    public CustomFixedFee setFeeCollectorAccountId(AccountId feeCollectorAccountId) {
        doSetFeeCollectorAccountId(feeCollectorAccountId);
        return this;
    }

    public long getAmount() {
        return amount;
    }

    public Hbar getHbarAmount() {
        return Hbar.fromTinybars(amount);
    }

    public CustomFixedFee setAmount(long amount) {
        this.amount = amount;
        return this;
    }

    public CustomFixedFee setHbarAmount(Hbar amount) {
        denominatingTokenId = null;
        this.amount = amount.toTinybars();
        return this;
    }

    @Nullable
    public TokenId getDenominatingTokenId() {
        return denominatingTokenId;
    }

    public CustomFixedFee setDenominatingTokenId(@Nullable TokenId tokenId) {
        denominatingTokenId = tokenId;
        return this;
    }

    public CustomFixedFee setDemoninatingTokenToSameToken() {
        denominatingTokenId = new TokenId(0, 0, 0);
        return this;
    }

    @Override
    void validateChecksums(Client client) throws BadEntityIdException {
        super.validateChecksums(client);
        if(denominatingTokenId != null) {
            denominatingTokenId.validateChecksum(client);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("feeCollectorAccountId", getFeeCollectorAccountId())
            .add("amount", getAmount())
            .add("demoninatingTokenId", getDenominatingTokenId())
            .toString();
    }

    @Override
    com.hedera.hashgraph.sdk.proto.CustomFee toProtobuf() {
        var customFeeBuilder = com.hedera.hashgraph.sdk.proto.CustomFee.newBuilder();
        var fixedFeeBuilder = FixedFee.newBuilder().setAmount(getAmount());
        if(getFeeCollectorAccountId() != null) {
            customFeeBuilder.setFeeCollectorAccountId(getFeeCollectorAccountId().toProtobuf());
        }
        if(getDenominatingTokenId() != null) {
            fixedFeeBuilder.setDenominatingTokenId(getDenominatingTokenId().toProtobuf());
        }
        return customFeeBuilder.setFixedFee(fixedFeeBuilder.build()).build();
    }
}
