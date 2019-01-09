package com.hedera.sdk.common;

import java.io.Serializable;

import com.hedera.sdk.node.HederaNode;
import com.hedera.sdk.transaction.HederaTransaction;
import com.hederahashgraph.api.proto.java.ResponseCodeEnum;
import com.hederahashgraph.api.proto.java.TransactionGetReceiptResponse;
import com.hederahashgraph.api.proto.java.TransactionReceipt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The consensus result for a transaction, which might not be currently known, or may succeed or fail.
 */
public class HederaTransactionReceipt implements Serializable {
	final static Logger logger = LoggerFactory.getLogger(HederaTransactionReceipt.class);
	private static final long serialVersionUID = 1;

	/**
	 * The status of the transaction - {@link ResponseCodeEnum}
	 */
	public ResponseCodeEnum transactionStatus = ResponseCodeEnum.UNKNOWN;
	/**
	 * The node precheckCode for this transaction
	 */
	public ResponseCodeEnum nodePrecheck = ResponseCodeEnum.UNKNOWN;
	/**
	 * The {@link HederaAccountID} for this receipt
	 * initially null
	 */
	public HederaAccountID accountID = null;
	/**
	 * The {@link HederaFileID} for this receipt
	 * initially null
	 */
	public HederaFileID fileID = null;
	/**
	 * The {@link HederaContractID} for this receipt
	 * initially null
	 */
	public HederaContractID contractID = null;
	
	/**
	 * Default constructor
	 */
	public HederaTransactionReceipt() {
	   	logger.trace("Start - Object init");
	   	logger.trace("End - Object init");
	}
	/**
	 * Constructs from a transaction status, account id, file id and contract id
	 * @param nodePrecheck a {@link HederaPrecheckResult} 
	 * @param transactionStatus a {@link HederaTransactionStatus}
	 * @param accountID a {@link HederaAccountID}
	 * @param fileID a {@link HederaFileID}
	 * @param contractID a {@link HederaContractID}
	 */
	public HederaTransactionReceipt(ResponseCodeEnum nodePrecheck, ResponseCodeEnum transactionStatus, HederaAccountID accountID, HederaFileID fileID, HederaContractID contractID) { 
	   	logger.trace("Start - Object init transactionStatus {}, accountID {}, fileID {}, contractID {}", transactionStatus, accountID, fileID, contractID);
	   	this.transactionStatus = transactionStatus;
	   	this.nodePrecheck = nodePrecheck;
	   	this.accountID = accountID;
	   	this.fileID = fileID;
	   	this.contractID = contractID;
	   	logger.trace("End - Object init");
	}

	/**
	 * Constructs from a transaction status, account id, file id and contract id
	 * @param transactionStatus a {@link HederaTransactionStatus}
	 * @param accountID a {@link HederaAccountID}
	 * @param fileID a {@link HederaFileID}
	 * @param contractID a {@link HederaContractID}
	 */
	public HederaTransactionReceipt(ResponseCodeEnum transactionStatus, HederaAccountID accountID, HederaFileID fileID, HederaContractID contractID) { 
	   	logger.trace("Start - Object init transactionStatus {}, accountID {}, fileID {}, contractID {}", transactionStatus, accountID, fileID, contractID);
	   	this.transactionStatus = transactionStatus;
	   	this.accountID = accountID;
	   	this.fileID = fileID;
	   	this.contractID = contractID;
	   	logger.trace("End - Object init");
	}
	
	/**
	 * Construct from a {@link TransactionGetReceiptResponse} protobuf stream
	 * @param receiptResponse the {@link TransactionGetReceiptResponse}
	 */
	public HederaTransactionReceipt(TransactionGetReceiptResponse receiptResponse) {
	   	logger.trace("Start - Object init in transactionRecipt receiptResponse {}", receiptResponse);
	   	
		this.nodePrecheck = receiptResponse.getHeader().getNodeTransactionPrecheckCode();
		this.transactionStatus = receiptResponse.getReceipt().getStatus();
		   	
	   	if (receiptResponse.getReceipt().hasAccountID()) {
		   	this.accountID = new HederaAccountID(receiptResponse.getReceipt().getAccountID());
	   	} else {
	   		this.accountID = null;
	   	}
	   	if (receiptResponse.getReceipt().hasFileID()) {
	   		this.fileID = new HederaFileID(receiptResponse.getReceipt().getFileID());
	   	} else {
	   		this.fileID = null;
	   	}
	   	if (receiptResponse.getReceipt().hasContractID()) {
	   		this.contractID = new HederaContractID(receiptResponse.getReceipt().getContractID());
	   	}
	   	logger.trace("End - Object init");
	}
	/**
	 * Construct from a {@link TransactionReceipt} protobuf stream
	 * @param receipt the {@link TransactionReceipt}
	 */
	public HederaTransactionReceipt(TransactionReceipt receipt) {
	   	logger.trace("Start - Object init in transactionRecipt receipt {}", receipt);
	   	
	   	this.transactionStatus = receipt.getStatus();
	   	
	   	if (receipt.hasAccountID()) {
		   	this.accountID = new HederaAccountID(receipt.getAccountID());
	   	} else {
	   		this.accountID = null;
	   	}
	   	if (receipt.hasFileID()) {
	   		this.fileID = new HederaFileID(receipt.getFileID());
	   	} else {
	   		this.fileID = null;
	   	}
	   	if (receipt.hasContractID()) {
	   		this.contractID = new HederaContractID(receipt.getContractID());
	   	}
	   	logger.trace("End - Object init");
	}

	/**
	 * Generate a {@link TransactionReceipt} protobuf payload for this object
	 * @return {@link TransactionReceipt}  
	 */
	public TransactionReceipt getProtobuf() {
	   	logger.trace("Start - getProtobuf");
		
		TransactionReceipt.Builder transactionReceipt = TransactionReceipt.newBuilder();
		
		if (this.accountID != null) {
			transactionReceipt.setAccountID(this.accountID.getProtobuf());
		}
		if (this.contractID != null) {
			transactionReceipt.setContractID(this.contractID.getProtobuf());
		}
		if (this.fileID != null) {
			transactionReceipt.setFileID(this.fileID.getProtobuf());
		}

   		transactionReceipt.setStatus(this.transactionStatus);
		
	   	logger.trace("End - getProtobuf");

		return transactionReceipt.build();
	}
	/** 
	 * Gets a receipt for a given transaction ID
	 * @param transactionID the transactionID
	 * @param node the node
	 * @throws InterruptedException in the event of a node communication failure 
	 */
	public HederaTransactionReceipt(HederaTransactionID transactionID, HederaNode node) throws InterruptedException {

		HederaTransaction transaction = new HederaTransaction();
		transaction.setNode(node);
		if (transaction.getReceipt(transactionID)) {
			this.accountID = transaction.transactionReceipt().accountID;
			this.contractID = transaction.transactionReceipt().contractID;
			this.fileID = transaction.transactionReceipt().fileID;
			this.transactionStatus = transaction.transactionReceipt().transactionStatus;
			this.nodePrecheck = transaction.transactionReceipt().nodePrecheck;
		} else {
			this.transactionStatus = ResponseCodeEnum.UNKNOWN;
		}
	}
}