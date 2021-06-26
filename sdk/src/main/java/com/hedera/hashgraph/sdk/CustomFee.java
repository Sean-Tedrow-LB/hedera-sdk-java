package com.hedera.hashgraph.sdk;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.common.base.MoreObjects;
import javax.annotation.Nullable;
import java.util.Objects;

abstract public class CustomFee {
    @Nullable
    private AccountId feeCollectorAccountId = null;

    public CustomFee() {
    }

    static CustomFee fromProtobuf(com.hedera.hashgraph.sdk.proto.CustomFee customFee) throws IllegalStateException {
        switch(customFee.getFeeCase()) {
            case FIXED_FEE:
                return CustomFixedFee.fromProtobuf(customFee);
            
            case FRACTIONAL_FEE:
                return CustomFractionalFee.fromProtobuf(customFee);
            
            default:
                throw new IllegalStateException("CustomFee#fromProtobuf: unhandled fee case: " + customFee.getFeeCase());
        }
    }

    public static CustomFee fromBytes(byte[] bytes) throws InvalidProtocolBufferException {
        return fromProtobuf(com.hedera.hashgraph.sdk.proto.CustomFee.parseFrom(bytes).toBuilder().build());
    }

    @Nullable 
    public AccountId getFeeCollectorAccountId() {
        return feeCollectorAccountId;
    }

    void doSetFeeCollectorAccountId(AccountId feeCollectorAccountId) {
        this.feeCollectorAccountId = Objects.requireNonNull(feeCollectorAccountId);
    }

    CustomFee deepClone() {
        if(this instanceof CustomFixedFee) {
            var fixedFee = (CustomFixedFee)this;
            return new CustomFixedFee()
                .setFeeCollectorAccountId(fixedFee.getFeeCollectorAccountId())
                .setAmount(fixedFee.getAmount())
                .setDenominatingTokenId(fixedFee.getDenominatingTokenId());
        } else {
            var fractionalFee = (CustomFractionalFee)this;
            return new CustomFractionalFee()
                .setFeeCollectorAccountId(fractionalFee.getFeeCollectorAccountId())
                .setNumerator(fractionalFee.getNumerator())
                .setDenominator(fractionalFee.getDenominator())
                .setMin(fractionalFee.getMin())
                .setMax(fractionalFee.getMax());
        }
    }

    abstract com.hedera.hashgraph.sdk.proto.CustomFee toProtobuf();
    
    public byte[] toBytes() {
        return toProtobuf().toByteArray();
    }
}