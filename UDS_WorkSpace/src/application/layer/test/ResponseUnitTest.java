package application.layer.test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import application.layer.core.NRC;
import application.layer.core.Response;
import application.layer.core.ResponseStatus;

class ResponseUnitTest {

	@Test
	void testPositiveResponseParsing() {
		byte[] payload = {
				0x01, 0x12, 0x34, 0x56, 0x78, 0x00, // messageType, A_SA, A_TA, targetAddressType
				0x10, 0x03,                         // SID = 0x10, SF = 0x03
				0x41, 0x42, 0x43,                   // A_Data = "ABC"
				0x00, 0x00, 0x00, 0x06              // A_Length = 6
		};

		Response response = new Response(payload);

		assertEquals(ResponseStatus.POSTIVE_RESPONSE, response.getResponseStatus());
		assertEquals((byte) 0x10, response.getServiceId()); // SID + 0x40
		assertEquals((byte) 0x03, response.getSubFunction());
		assertArrayEquals(new byte[]{65, 66, 67}, response.getData());
		assertEquals(6, response.getDataLength());
	}

	@Test
	void testNegativeResponseParsing() {
		byte[] payload = {
				0x01, 0x12, 0x34, 0x56, 0x78, 0x00, // messageType, A_SA, A_TA, targetAddressType
				0x7F, 0x10, 0x22,                   // Negative response for service 0x10, NRC = 0x22 (Conditions Not Correct)
				0x00, 0x00, 0x00, 0x03              // A_Length = 3
		};

		Response response = new Response(payload);

		assertEquals(ResponseStatus.NEGATIVE_RESPONSE, response.getResponseStatus());
		assertEquals((byte) 0x10, response.getServiceId()); // Original service requested
		assertEquals(NRC.CONDITIONS_NOT_CORRECT, response.getRecivedNRC());
		assertEquals(3, response.getDataLength());
	}

	@Test
	void testInvalidPayloadTooShort() {
		byte[] invalidPayload = {0x01, 0x12}; // Too short to be valid
		Response response = new Response(invalidPayload);
		
		assertEquals(ResponseStatus.INVALID_RESPONSE, response.getResponseStatus());
	}

	@Test
	void testEmptyDataHandling() {
		byte[] payload = {
				0x01, 0x12, 0x34, 0x56, 0x78, 0x00, // messageType, A_SA, A_TA, targetAddressType
				0x10, 0x03,                         // SID = 0x10, SF = 0x03
				0x00, 0x00, 0x00, 0x02              // A_Length = 2 (No additional data)
		};

		Response response = new Response(payload);

		assertNull(response.getData()); // Should return null since no data exists
	}
}
