package application.layer.test;

import application.layer.core.A_MType;
import application.layer.core.A_TA_Type;
import application.layer.core.Request;
import application.layer.core.ServiceID;
import application.layer.utils.LoggerUtility;

public class RequestTest {
    public static void main(String[] args) {

        // Set static values
        Request.setMessageType(A_MType.DIAGNOSTICS);
        Request.setSourceAddress((short) 0x1234);
        Request.setTargetAddress((short) 0x5678);
        Request.setTargetAddressType(A_TA_Type.PHYSICAL);

        // Case 1: Request without data
        Request request1 = new Request(ServiceID.DIAGNOSTIC_SESSION_CONTROL, (byte) 0x01, false);
        byte[] payload1 = request1.getPayload();
        System.out.println("Payload (No Data):");
        printBytes(payload1);
 
        LoggerUtility.info(request1.toString());
      
        
        
        // Case 2: Request with data
        byte[] testData = {0x11, 0x22, 0x33, 0x44};
        Request request2 = new Request(ServiceID.ECU_RESET, (byte) 0x02, true, testData);
        byte[] payload2 = request2.getPayload();
        System.out.println("Payload (With Data & suppressPR):");
        printBytes(payload2);
        
        LoggerUtility.info(request2.toString());

        // Case 3: Request with remote address (A_AE)
        short[] remoteAddresses = {0x1234, 0x5678};
        Request.setRemoteAddress(remoteAddresses);
        Request request3 = new Request(ServiceID.READ_DATA_BY_IDENTIFIER, (byte) 0x03, false);
        byte[] payload3 = request3.getPayload();
        System.out.println("Payload (With Remote Address):");
        printBytes(payload3);
        
        LoggerUtility.info(request3.toString());
    }

    // Helper method to print payload in byte format
    private static void printBytes(byte[] payload) {
        for (byte b : payload) {
            System.out.printf("0x%02X ", b); // Print each byte in hexadecimal format
        }
        System.out.println(); // New line for readability
    }
}
