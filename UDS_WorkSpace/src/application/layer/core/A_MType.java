package application.layer.core;

public enum A_MType {
	DIAGNOSTICS		  ((byte) 0x00),
	REMOTE_DIAGNOSTICS((byte) 0x01);

	private final byte value;

	// Constructor to assign byte values
	A_MType(byte value) {
		this.value = value;
	}

	// Getter to retrieve the byte value
	public byte getValue() {
		return value;
	}

	// Method to convert a byte value into the corresponding enum constant
	public static A_MType fromValue(byte value) {
		for (A_MType type : A_MType.values()) {
			if (type.value == value) {
				return type;
			}
		}
		throw new IllegalArgumentException("Invalid A_MType value: " + value);
	}
}
