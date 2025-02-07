package application.layer.core;
import application.layer.utils.LoggerUtility;

public class Response {

	/******************************** Data Members *******************************/
	private A_MType messageType;  			// Enum: Message Type
	private short A_SA;           			// Source Address (2 Bytes)
	private short A_TA;           			// Target Address (2 Bytes)
	private A_TA_Type targetAddressType; 	// Target Address Type Enum
	private short[] A_AE = null; 		 	// Optional Address Extension
	private byte responseSID;               // Service ID (1 Byte)
	private byte echoSF;                    // Subfunction (1 Byte)
	private byte[] A_Data = null;        	// Additional Data (Optional)
	private int A_Length;                	// Data Length (4 Bytes)

	private ResponseStatus responseStatus;
	private NRC recivedNRC;

	/********************************* Constructor ******************************/
	public Response(byte[] payload) {
		this.fromPayload(payload);
	}

	public Response() {
		// Default constructor
	}

	/********************************** Getters *********************************/
	public A_MType getMessageType(){ 
		return messageType; 
	}
	public short getSourceAddress(){
		return A_SA; 
	}
	public short getTargetAddress(){
		return A_TA; 
	}
	public A_TA_Type getTargetAddressType(){ 
		return targetAddressType; 
	}
	public short[] getAddressExtension(){
		return (A_AE != null) ? A_AE.clone() : null; 
	}
	public byte getServiceId(){
		return responseSID; 
	}
	public byte getSubFunction(){ 
		return echoSF; 
	}
	public byte[] getData(){
		return (A_Data != null) ? A_Data.clone() : null; 
	}
	public int getDataLength(){ 
		return A_Length; 
	}
	public NRC getRecivedNRC(){
		return recivedNRC;
	}
	public ResponseStatus getResponseStatus(){
		return responseStatus;
	}

	/******************************* Parsing Method ****************************/
	/*
	 * Parses a raw UDS response payload (byte array) and creates a Response object.
	 * @param payload Raw response bytes.
	 * @return Parsed Response object.
	 */
	public void fromPayload(byte[] payload) {
		if (payload == null || payload.length < 12) { // Minimum required length
			LoggerUtility.error("INCORRECT PAYLOAD (null or Invalid payload length)\n");
			responseStatus = ResponseStatus.INVALID_RESPONSE;
			return ;
//			throw new IllegalArgumentException("Invalid payload: Too short to be a valid response.");
		}

		// Extract A_Length (4 Bytes)
		int index = payload.length - 4;
		A_Length = ((payload[index++] & 0xFF) << 24) |
				((payload[index++] & 0xFF) << 16) |
				((payload[index++] & 0xFF) << 8)  |
				(payload[index] & 0xFF);

		// Extract messageType (Assumes A_MType enum has a fromValue() method)
		index = 0;
		messageType = A_MType.fromValue(payload[index++]);

		// Extract A_SA (Source Address - 2 Bytes)
		A_SA = (short) ((payload[index++] << 8) | (payload[index++] & 0xFF));

		// Extract A_TA (Target Address - 2 Bytes)
		A_TA = (short) ((payload[index++] << 8) | (payload[index++] & 0xFF));

		// Extract targetAddressType (Assumes A_TA_Type enum has a fromValue() method)
		targetAddressType = A_TA_Type.fromValue(payload[index++]);

		// Extract A_AE (if present, assume the first byte defines its length)
		if (messageType == A_MType.REMOTE_DIAGNOSTICS)
		{
			int aeLength = (payload.length - A_Length - 4 - 6) / 2;   // Number of short values in A_AE
			if (aeLength > 0) {
				A_AE = new short[aeLength];
				for (int i = 0; i < aeLength; i++) {
					A_AE[i] = (short) ((payload[index++] << 8) | (payload[index++] & 0xFF));
				}
			}
		}

		// Extract responseSID (Service ID - 1 Byte)
		byte code = payload[index++];
		if(code == 0x7f)
		{
			responseStatus = ResponseStatus.NEGATIVE_RESPONSE;
			responseSID = payload[index++];    //SID (e.g. 0x10)
			recivedNRC  = NRC.fromValue(payload[index]);
			if(recivedNRC == null) {
				LoggerUtility.error("UNSUPPORTED NRC\n");
				responseStatus = ResponseStatus.INVALID_RESPONSE;				
			}
		}
		else
		{
			responseStatus = ResponseStatus.POSTIVE_RESPONSE;
			responseSID = code;				   //SID + 0x40 (e.g. 0x50)
			// Extract Subfunction echo (echoSF - 1 Byte)
			echoSF = payload[index++];

			// Extract A_Data 
			int remainingBytes = payload.length - index - 4; // Excluding last 4 bytes for A_Length
			if (remainingBytes > 0) {
				A_Data = new byte[remainingBytes];
				System.arraycopy(payload, index, A_Data, 0, remainingBytes);
			}
		}
	}

	/***************************** Debugging Method ********************************/
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Response {");

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

		// Response Status
		sb.append(", responseStatus=").append(responseStatus);

		// Handle Negative Response
		if (responseStatus == ResponseStatus.NEGATIVE_RESPONSE) {
			sb.append(", responseSID=0x").append(String.format("%02X", responseSID & 0xFF));
			sb.append(", receivedNRC=").append(recivedNRC);
		} 
		// Handle Positive Response
		else if (responseStatus == ResponseStatus.POSTIVE_RESPONSE) {
			sb.append(", responseSID=0x").append(String.format("%02X", responseSID & 0xFF));
			sb.append(", echoSF=0x").append(String.format("%02X", echoSF & 0xFF));

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
		}
		// Append Data Length
		sb.append(", A_Length=").append(A_Length);
		sb.append(" }");

		return sb.toString();
	}
}