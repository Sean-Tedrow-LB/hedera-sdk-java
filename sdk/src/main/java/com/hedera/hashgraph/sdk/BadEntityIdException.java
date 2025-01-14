package com.hedera.hashgraph.sdk;

public class BadEntityIdException extends Exception {
    public final long shard;
    public final long realm;
    public final long num;
    public final String presentChecksum;
    public final String expectedChecksum;

    BadEntityIdException(long shard, long realm, long num, String presentChecksum, String expectedChecksum) {
        super(String.format("Entity ID %d.%d.%d-%s was incorrect.", shard, realm, num, presentChecksum));
        this.shard = shard;
        this.realm = realm;
        this.num = num;
        this.presentChecksum = presentChecksum;
        this.expectedChecksum = expectedChecksum;
    }
}
