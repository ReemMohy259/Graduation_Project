package application.layer.core;

public enum NRC {

    GENERAL_REJECT((byte) 0x10),
    SERVICE_NOT_SUPPORTED((byte) 0x11),
    SUBFUNCTION_NOT_SUPPORTED((byte) 0x12),
    INCORRECT_MESSAGE_LENGTH_OR_FORMAT((byte) 0x13),
    RESPONSE_TOO_BUSY((byte) 0x21),
    CONDITIONS_NOT_CORRECT((byte) 0x22),
    REQUEST_SEQUENCE_ERROR((byte) 0x24),
    NO_RESPONSE_FROM_SUBFUNCTION((byte) 0x25),
    FAILURE_PREVENTS_EXECUTION((byte) 0x26),
    REQUEST_OUT_OF_RANGE((byte) 0x31),
    SECURITY_ACCESS_DENIED((byte) 0x33),
    INVALID_KEY((byte) 0x35),
    EXCEEDED_NUMBER_OF_ATTEMPTS((byte) 0x36),
    REQUIRED_TIME_DELAY_NOT_EXPIRED((byte) 0x37),
    UPLOAD_DOWNLOAD_NOT_ACCEPTED((byte) 0x70),
    TRANSFER_DATA_SUSPENDED((byte) 0x71),
    GENERAL_PROGRAMMING_FAILURE((byte) 0x72),
    WRONG_BLOCK_SEQUENCE_COUNTER((byte) 0x73),
    RESPONSE_PENDING((byte) 0x78),
    SUBFUNCTION_NOT_SUPPORTED_IN_ACTIVE_SESSION((byte) 0x7E),
    SERVICE_NOT_SUPPORTED_IN_ACTIVE_SESSION((byte) 0x7F),
    RPM_TOO_HIGH((byte) 0x81),
    RPM_TOO_LOW((byte) 0x82),
    ENGINE_RUNNING((byte) 0x83),
    ENGINE_NOT_RUNNING((byte) 0x84),
    ENGINE_RUNTIME_TOO_LOW((byte) 0x85),
    TEMPERATURE_TOO_HIGH((byte) 0x86),
    TEMPERATURE_TOO_LOW((byte) 0x87),
    VEHICLE_SPEED_TOO_HIGH((byte) 0x88),
    VEHICLE_SPEED_TOO_LOW((byte) 0x89),
    THROTTLE_PEDAL_TOO_HIGH((byte) 0x8A),
    THROTTLE_PEDAL_TOO_LOW((byte) 0x8B),
    DIAGNOSTIC_SESSION_NOT_ACTIVE((byte) 0x8C),
    VOLTAGE_TOO_HIGH((byte) 0x92),
    VOLTAGE_TOO_LOW((byte) 0x93);

    private final byte code;

    // Constructor
    NRC(byte code) {
        this.code = code;
    }

    // Getter to retrieve the byte value
    public byte getCode() {
        return code;
    }

    
    /*
     * Converts a byte value into the corresponding NRC enum constant.
     * @param  Code Byte value to convert.
     * @return Corresponding NRC enum constant.
     * @throws IllegalArgumentException if the byte value is invalid.
     */
    public static NRC fromValue(byte code) {
        for (NRC nrc : NRC.values()) {
            if (nrc.code == code) {
                return nrc;
            }
        }
        return(null);
//        throw new IllegalArgumentException("Invalid NRC value: " + String.format("0x%02X", code));
    }
}
