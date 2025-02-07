package application.layer.core;

public enum ServiceID {

	DIAGNOSTIC_SESSION_CONTROL((byte) 0x10),
	ECU_RESET((byte) 0x11),
	CLEAR_DIAGNOSTIC_INFORMATION((byte) 0x14),
	READ_DTC_INFORMATION((byte) 0x19),
	READ_DATA_BY_IDENTIFIER((byte) 0x22),
	READ_MEMORY_BY_ADDRESS((byte) 0x23),
	READ_SCALING_DATA_BY_IDENTIFIER((byte) 0x24),
	SECURITY_ACCESS((byte) 0x27),
	COMMUNICATION_CONTROL((byte) 0x28),
	AUTHENTICATION((byte) 0x29),
	READ_DATA_BY_PERIODIC_IDENTIFIER((byte) 0x2A),
	DYNAMICALLY_DEFINE_DATA_IDENTIFIER((byte) 0x2C),
	WRITE_DATA_BY_IDENTIFIER((byte) 0x2E),
	INPUT_OUTPUT_CONTROL_BY_IDENTIFIER((byte) 0x2F),
	ROUTINE_CONTROL((byte) 0x31),
	REQUEST_DOWNLOAD((byte) 0x34),
	REQUEST_UPLOAD((byte) 0x35),
	TRANSFER_DATA((byte) 0x36),
	REQUEST_TRANSFER_EXIT((byte) 0x37),
	REQUEST_FILE_TRANSFER((byte) 0x38),
	WRITE_MEMORY_BY_ADDRESS((byte) 0x3D),
	TESTER_PRESENT((byte) 0x3E),
	SECURED_DATA_TRANSMISSION((byte) 0x84),
	CONTROL_DTC_SETTING((byte) 0x85),
	RESPONSE_ON_EVENT((byte) 0x86),
	LINK_CONTROL((byte) 0x87);

	private final byte code;

	// Constructor
	ServiceID(byte code) {
		this.code = code;
	}

	// Getter to retrieve the byte value
	public byte getCode() {
		return code;
	}

	/*
	 * Converts a byte value into the corresponding ServiceID enum constant.
	 *
	 * @param code Byte value to convert.
	 * @return Corresponding ServiceID enum constant.
	 * @throws IllegalArgumentException if the byte value is invalid.
	 */
	public static ServiceID fromValue(byte code) {
		for (ServiceID service : ServiceID.values()) {
			if (service.code == code) {
				return service;
			}
		}
		return(null);
//		throw new IllegalArgumentException("Invalid ServiceID value: " + String.format("0x%02X", code));
	}


}

