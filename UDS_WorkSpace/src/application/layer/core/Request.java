package application.layer.core;

import application.layer.utils.LoggerUtility;
/*
 * This class represents the request packet structure.
 * This structure follows the ISO 14229-1 standard.
 * It defines the packet that is expected to be sent from the application
   layer of the client down to the transport layer.
 */
public class Request {

	/******************************* Data-Members ****************************/
	static A_MType messageType;	    	// Enum
	static short A_SA;			    	// 2-Bytes
	static short A_TA;
	static A_TA_Type targetAddressType;	
	static short[]   A_AE = null;		// Optional

	private int A_Length;				// Length of the data (excluding the header and length field)

	private ServiceID SID;
	private byte SF;
	private boolean suppressPositiveResponse;
	private byte[]  A_Data = null;

	/******************************** Constructor ****************************/
	public Request(ServiceID service, byte subFunction, boolean suppressPositiveResponse) {
		this.SID = service;
		this.SF = subFunction;
		this.suppressPositiveResponse = suppressPositiveResponse;

		if (suppressPositiveResponse == true)
		{
			SF |= 0x80;
		}
		else
		{
			SF &= ~0x80;
		}

		this.A_Data = null;

		this.A_Length = 2;    // SID(1 Byte) + SF(1 Byte)
	}

	public Request(ServiceID service, byte subFunction, boolean suppressPositiveResponse, byte[] Original_Data) {

		this(service, subFunction, suppressPositiveResponse);
		this.A_Data = Original_Data.clone();

		this.A_Length = 2 + A_Data.length;
	}

	/******************************* Getters *********************************/
	public ServiceID GetService(){
		return SID;
	}

	public byte GetSF(){
		return SF;
	}

	public boolean GetsuppressPositiveResponse() {
		return suppressPositiveResponse;
	}

	public byte[] GetData() {
		return A_Data.clone();
	}


	/******************************* Setters *********************************/
	public static void setMessageType(A_MType inputType) {
		messageType = inputType;	
		if (inputType == A_MType.DIAGNOSTICS)
		{
			A_AE = null;	
		}
	}

	public static void setSourceAddress(short inputSA) {
		A_SA = inputSA;	
	}

	public static void setTargetAddress(short inputTA) {
		A_TA = inputTA;	
	}

	public static void setTargetAddressType(A_TA_Type inputtargetAddressType) {
		targetAddressType = inputtargetAddressType;	
	}

	public static void setRemoteAddress(short[] inputRemoteAddress) {
		if (messageType == A_MType.REMOTE_DIAGNOSTICS)
		{
			A_AE = inputRemoteAddress.clone();	
		}
		else
		{
			LoggerUtility.warning("A_AE only supported for REMOTE_DIAGNOSTICS A_MType\n");
		}		
	}

	/******************************* Methods *********************************/
	public byte[] getPayload() {

		// Calculate total payload size
		int totalLength = 12; 		// messageType(1), A_SA(2), A_TA(2), targetAddressType(1), Length(4), SID(1), SF(1) 

		if (A_AE != null) {
			totalLength += A_AE.length * 2; 	// A_AE is short[], each short is 2 bytes
		}

		if (A_Data != null) {
			totalLength += A_Data.length;
		}

		// Create the payload array
		byte[] payload = new byte[totalLength];

		// Index for filling the array
		int index = 0;

		// Convert and copy messageType (assuming it's an enum with ordinal value)
		payload[index++] = (byte) messageType.ordinal();

		// Convert and copy A_SA (short -> 2 bytes)
		payload[index++] = (byte) (A_SA >> 8);
		payload[index++] = (byte) (A_SA);

		// Convert and copy A_TA (short -> 2 bytes)
		payload[index++] = (byte) (A_TA >> 8);
		payload[index++] = (byte) (A_TA);

		// Convert and copy targetAddressType (assuming it's an enum with ordinal value)
		payload[index++] = (byte) targetAddressType.ordinal();

		// Convert and copy A_AE (if available)
		if ((A_AE != null) && (messageType == A_MType.REMOTE_DIAGNOSTICS)) {
			for (short ae : A_AE) {
				payload[index++] = (byte) (ae >> 8);
				payload[index++] = (byte) (ae);
			}
		}

		// Copy SID
		payload[index++] = SID.getCode();

		// Copy SF
		payload[index++] = SF;

		// Copy A_Data (if available)
		if (A_Data != null) {
			System.arraycopy(A_Data, 0, payload, index++, A_Data.length);
			index += A_Data.length - 1;
		}
		
		// Copy A_Length
		payload[index++] = (byte) (A_Length >> 24);
		payload[index++] = (byte) (A_Length >> 16);
		payload[index++] = (byte) (A_Length >> 8);
		payload[index]   = (byte) (A_Length);

		return payload;
	}
	
	
	/***************************** Debugging Method ********************************/
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Request {");

	    // Basic Fields
	    sb.append(" messageType=").append(messageType);
	    sb.append(", A_SA=0x").append(String.format("%04X", A_SA & 0xFFFF));
	    sb.append(", A_TA=0x").append(String.format("%04X", A_TA & 0xFFFF));
	    sb.append(", targetAddressType=").append(targetAddressType);

	    // Handle Address Extension (A_AE) in Hex
	    if (A_AE != null && A_AE.length > 0) {
	        sb.append(", A_AE=[");
	        for (int i = 0; i < A_AE.length; i++) {
	            sb.append(String.format("0x%04X", A_AE[i] & 0xFFFF));
	            if (i < A_AE.length - 1) sb.append(", ");
	        }
	        sb.append("]");
	    } else {
	        sb.append(", A_AE=None");
	    }

	    // Handle SID and SF
	    sb.append(", SID= ").append(SID);
	    sb.append(", SF=0x").append(String.format("%02X", SF & 0xFF));
	    sb.append(", suppressPositiveResponse=").append(suppressPositiveResponse);

	    // Handle A_Data in Hex
	    if (A_Data != null && A_Data.length > 0) {
	        sb.append(", A_Data=[");
	        for (int i = 0; i < A_Data.length; i++) {
	            sb.append(String.format("0x%02X", A_Data[i] & 0xFF));
	            if (i < A_Data.length - 1) sb.append(", ");
	        }
	        sb.append("]");
	    } else {
	        sb.append(", A_Data=None");
	    }

	    // Append Data Length
	    sb.append(", A_Length=").append(A_Length);
	    sb.append(" }");

	    return sb.toString();
	}

}