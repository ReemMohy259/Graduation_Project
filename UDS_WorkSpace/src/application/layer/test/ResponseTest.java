package application.layer.test;

import application.layer.core.Response;
import application.layer.utils.LoggerUtility;

public class ResponseTest {

	public static void main(String[] args) {
		// Example payload for a positive response
		byte[] positivePayload = {
				0x00, 0x12, 0x34, 0x56, 0x78, 0x00, // messageType, A_SA, A_TA, targetAddressType
				0x10, 0x03,                         // SID = 0x10 (Session Control), SF = 0x03 (Extended Session)
				0x41, 0x42, 0x43,                   // A_Data = "ABC"
				0x00, 0x00, 0x00, 0x06              // A_Length = 6
		};

		// Example payload for a negative response
		byte[] negativePayload = {
				0x00, 0x12, 0x34, 0x56, 0x78, 0x00, // messageType, A_SA, A_TA, targetAddressType
				0x7F, 0x10, 0x22,                   // Negative response for service 0x10, NRC = 0x22 (Conditions Not Correct)
				0x00, 0x00, 0x00, 0x03              // A_Length = 3
		};

		// Example payload for a positive response with data and A_AE
		byte[] positivePayload2 = {
				0x01, 0x12, 0x34, 0x56, 0x78, 0x00, // messageType, A_SA, A_TA, targetAddressType
				0x25 , 0x09 , 0x23 , 0x11,			// A_AE = 0x2509 , 0x2311
				0x10, 0x03,                         // SID = 0x10 (Session Control), SF = 0x03 (Extended Session)
				0x41, 0x42, 0x43,                   // A_Data = "ABC"
				0x00, 0x00, 0x00, 0x05              // A_Length = 6
		};

		System.out.println("=== Testing Positive Response ===");
		Response positiveResponse = new Response(positivePayload);

		LoggerUtility.info(positiveResponse.toString());

		System.out.println("\n=== Testing Negative Response ===");
		Response negativeResponse = new Response();		
		negativeResponse.fromPayload(negativePayload);
		LoggerUtility.info(negativeResponse.toString());

		System.out.println("\n=== Testing Positive Response With A_AE ===");
		Response positiveResponse2 = new Response(positivePayload2);
		LoggerUtility.info(positiveResponse2.toString());	
	}
}
