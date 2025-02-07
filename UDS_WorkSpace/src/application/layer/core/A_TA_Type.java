package application.layer.core;

public enum A_TA_Type {
	PHYSICAL  ((byte) 0x00),
	FUNCTIONAL((byte) 0x01);

    private final byte value;

    A_TA_Type(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static A_TA_Type fromValue(byte value) {
        for (A_TA_Type type : A_TA_Type.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid A_TA_Type value: " + value);
    }
}
