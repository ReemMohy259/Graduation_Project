package application.layer.test;
import application.layer.core.Request;
import application.layer.core.ServiceID;
import application.layer.core.A_MType;
import application.layer.core.A_TA_Type;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RequestUnitTest {

    @BeforeEach
    void setup() {
        // Initialize static members
        Request.setMessageType(A_MType.DIAGNOSTICS);
        Request.setSourceAddress((short) 0x1234);
        Request.setTargetAddress((short) 0x5678);
        Request.setTargetAddressType(A_TA_Type.PHYSICAL);
    }

    @Test
    void testPayload_NoData() {
        Request request = new Request(ServiceID.DIAGNOSTIC_SESSION_CONTROL, (byte) 0x01, false);
        byte[] expectedPayload = {
            0x00, 0x12, 0x34, 0x56, 0x78,0x00,  // Header
            0x10, 0x01,  						// SID, SF
            0x00, 0x00, 0x00, 0x02  			// Length (2 bytes)
        };

        assertArrayEquals(expectedPayload, request.getPayload(), "Payload without data should match expected structure.");
    }

    @Test
    void testPayload_WithData() {
        byte[] testData = {0x11, 0x22, 0x33, 0x44};
        Request request = new Request(ServiceID.ECU_RESET, (byte) 0x02, true, testData);
        byte[] expectedPayload = {
            0x00, 0x12, 0x34, 0x56, 0x78,0x00,  // Header
            0x11, (byte) 0x82,  	 			// SID, SF (with suppression bit)
            0x11, 0x22, 0x33, 0x44,   			// Data
            0x00, 0x00, 0x00, 0x06  			// Length (6 bytes)
        };

        assertArrayEquals(expectedPayload, request.getPayload(), "Payload with data should match expected structure.");
    }

    @Test
    void testPayload_WithRemoteAddress() {
        short[] remoteAddresses = {0x1234, 0x5678};
        Request.setMessageType(A_MType.REMOTE_DIAGNOSTICS);
        Request.setRemoteAddress(remoteAddresses);
        Request request = new Request(ServiceID.REQUEST_UPLOAD, (byte) 0x03, false);
        byte[] expectedPayload = {
            0x01, 0x12, 0x34, 0x56, 0x78,0x00,  // Header
            0x12, 0x34, 0x56, 0x78,  			// Remote Addresses           
            0x35, 0x03,  						// SID, SF
            0x00, 0x00, 0x00, 0x02  			// Length (2 bytes)
        };

        assertArrayEquals(expectedPayload, request.getPayload(), "Payload with remote address should match expected structure.");
    }
    
}
