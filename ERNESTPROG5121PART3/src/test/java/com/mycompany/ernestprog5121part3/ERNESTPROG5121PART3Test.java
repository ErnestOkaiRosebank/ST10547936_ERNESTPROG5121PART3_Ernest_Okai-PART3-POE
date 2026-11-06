package com.mycompany.ernestprog5121part3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ERNESTPROG5121PART3Test {

    @BeforeEach
    public void setUp() {
        // reset shared static state before every test so tests don't interfere with each other
        ERNESTPROG5121PART3.resetAllForTesting();
    }

    // ==================== Part 1: Registration/Login tests ====================

    // --- Username tests ---

    @Test
    public void testUsername_CorrectlyFormatted() {
        // Test Data: "kyl_1"
        assertTrue(ERNESTPROG5121PART3.checkUserName("kyl_1"));
    }

    @Test
    public void testUsername_IncorrectlyFormatted() {
        // Test Data: "kyleeeeeeee" (no underscore, too long)
        assertFalse(ERNESTPROG5121PART3.checkUserName("kyleeeeeeee"));
    }

    // --- Password tests ---

    @Test
    public void testPassword_MeetsComplexityRequirements() {
        // Test Data: "Ch&&sec@1a99"
        assertTrue(ERNESTPROG5121PART3.checkPasswordComplexity("Ch&&sec@1a99"));
    }

    @Test
    public void testPassword_DoesNotMeetComplexityRequirements() {
        // Test Data: "password" (no capital, no number, no special char)
        assertFalse(ERNESTPROG5121PART3.checkPasswordComplexity("password"));
    }

    // --- Cell phone number tests ---

    @Test
    public void testCellNumber_CorrectlyFormatted() {
        // Test Data: "+27830968976"
        assertTrue(ERNESTPROG5121PART3.checkCellPhoneNumber("+27830968976"));
    }

    @Test
    public void testCellNumber_IncorrectlyFormatted() {
        // Test Data: "0830968976" (missing international code)
        assertFalse(ERNESTPROG5121PART3.checkCellPhoneNumber("0830968976"));
    }

    // --- Registration message tests ---

    @Test
    public void testRegisterUser_UsernameIncorrectlyFormatted() {
        String result = ERNESTPROG5121PART3.registerUser("kyleeeeeeee", "Ch&&sec@1a99");
        assertEquals("Username is not correct. Must have _ and max 5 chars.", result);
    }

    @Test
    public void testRegisterUser_PasswordDoesNotMeetComplexity() {
        String result = ERNESTPROG5121PART3.registerUser("kyl_1", "password");
        assertEquals("Password is not correct. Must have 8+ chars, capital letter, number, and special char.", result);
    }

    @Test
    public void testRegisterUser_Success() {
        String result = ERNESTPROG5121PART3.registerUser("kyl_1", "Ch&&sec@1a99");
        assertEquals("Username and password successfully captured. User registered!", result);
    }

    // --- Login tests ---

    @Test
    public void testLogin_Successful() {
        assertTrue(ERNESTPROG5121PART3.loginUser("kyl_1", "Ch&&sec@1a99", "kyl_1", "Ch&&sec@1a99"));
    }

    @Test
    public void testLogin_Failed() {
        assertFalse(ERNESTPROG5121PART3.loginUser("kyl_1", "wrongPassword1!", "kyl_1", "Ch&&sec@1a99"));
    }

    @Test
    public void testReturnLoginStatus_Success() {
        assertEquals("Login successful! Welcome back!", ERNESTPROG5121PART3.returnLoginStatus(true));
    }

    @Test
    public void testReturnLoginStatus_Failure() {
        assertEquals("Username or password incorrect, please try again.", ERNESTPROG5121PART3.returnLoginStatus(false));
    }

    // ==================== Part 2: Sending Messages tests ====================

    // ---- checkMessageID ----

    @Test
    public void testCheckMessageID_ValidLength() {
        assertTrue(ERNESTPROG5121PART3.checkMessageID("0012345678")); // 10 chars
    }

    @Test
    public void testCheckMessageID_TooLong() {
        assertFalse(ERNESTPROG5121PART3.checkMessageID("00123456789")); // 11 chars
    }

    // ---- checkRecipientCell ----

    @Test
    public void testCheckRecipientCell_Success() {
        // Test Data: "+27718693002"
        String result = ERNESTPROG5121PART3.checkRecipientCell("+27718693002");
        assertEquals("Cell phone number successfully captured.", result);
    }

    @Test
    public void testCheckRecipientCell_Failure() {
        // Test Data: "08575975089" (missing international code)
        String result = ERNESTPROG5121PART3.checkRecipientCell("08575975089");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", result);
    }

    // ---- checkMessageLength ----

    @Test
    public void testCheckMessageLength_Success() {
        String msg = "Hi Mike, can you join me for dinner tonight?";
        assertEquals("Message ready to send.", ERNESTPROG5121PART3.checkMessageLength(msg));
    }

    @Test
    public void testCheckMessageLength_Failure() {
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 260) {
            sb.append("a");
        }
        String msg = sb.toString();
        int expectedOver = msg.length() - 250;
        String result = ERNESTPROG5121PART3.checkMessageLength(msg);
        assertEquals("Message exceeds 250 characters by " + expectedOver + "; please reduce the size.", result);
    }

    // ---- createMessageHash ----

    @Test
    public void testCreateMessageHash_MatchesBriefExample() {
        // Brief example: ID "0012345678", message number 0,
        // "Hi Mike, can you join me for dinner tonight?" -> "00:0:HITONIGHT"
        String hash = ERNESTPROG5121PART3.createMessageHash("0012345678", 0, "Hi Mike, can you join me for dinner tonight?");
        assertEquals("00:0:HITONIGHT", hash);
    }

    @Test
    public void testCreateMessageHash_SecondExample() {
        // "Hi Keegan, did you receive the payment?" with ID "0112345678", number 1
        String hash = ERNESTPROG5121PART3.createMessageHash("0112345678", 1, "Hi Keegan, did you receive the payment?");
        assertEquals("01:1:HIPAYMENT", hash);
    }

    // ---- sentMessage ----

    @Test
    public void testSentMessage_Send() {
        String result = ERNESTPROG5121PART3.sentMessage(1, "0012345678", "00:0:HITONIGHT", "+27718693002", "Hi Mike, can you join me for dinner tonight?");
        assertEquals("Message successfully sent.", result);
        assertEquals(1, ERNESTPROG5121PART3.returnTotalMessages());
    }

    @Test
    public void testSentMessage_Disregard() {
        String result = ERNESTPROG5121PART3.sentMessage(2, "0012345678", "00:0:HITONIGHT", "+27718693002", "Hi Mike, can you join me for dinner tonight?");
        assertEquals("Press 0 to delete the message.", result);
        assertEquals(0, ERNESTPROG5121PART3.returnTotalMessages());
    }

    @Test
    public void testSentMessage_Store() {
        String result = ERNESTPROG5121PART3.sentMessage(3, "0012345678", "00:0:HITONIGHT", "+27718693002", "Hi Mike, can you join me for dinner tonight?");
        assertEquals("Message successfully stored.", result);
        assertEquals(1, ERNESTPROG5121PART3.storedMessages.size());
        assertEquals(0, ERNESTPROG5121PART3.returnTotalMessages()); // not counted as "sent"
    }

    // ---- returnTotalMessages ----

    @Test
    public void testReturnTotalMessages_OnlyCountsSent() {
        ERNESTPROG5121PART3.sentMessage(1, "0012345678", "00:0:HITONIGHT", "+27718693002", "Hi Mike, can you join me for dinner tonight?");
        ERNESTPROG5121PART3.sentMessage(2, "0112345678", "01:1:HIPAYMENT", "+27718693002", "Hi Keegan, did you receive the payment?");
        ERNESTPROG5121PART3.sentMessage(3, "0212345678", "02:2:HITEST", "+27718693002", "Hi this is a test");

        assertEquals(1, ERNESTPROG5121PART3.returnTotalMessages());
    }

    // ---- integration: full flow ----

    @Test
    public void testFullMessageFlow_SendAndPrint() {
        String id = "0012345678";
        String hash = ERNESTPROG5121PART3.createMessageHash(id, ERNESTPROG5121PART3.messageCounter, "Hi Mike, can you join me for dinner tonight?");
        String result = ERNESTPROG5121PART3.sentMessage(1, id, hash, "+27718693002", "Hi Mike, can you join me for dinner tonight?");

        assertEquals("Message successfully sent.", result);
        assertTrue(ERNESTPROG5121PART3.printMessages().contains("Hi Mike, can you join me for dinner tonight?"));
        assertTrue(ERNESTPROG5121PART3.printMessages().contains(hash));
    }

    // ==================== Part 3: Store Data and Display Task Report tests ====================
    //
    // Uses the brief's own five test-data messages:
    // 1: +27834557896 / "Did you get the cake?"                                   / Sent
    // 2: +27838884567 / "Where are you? You are late! I have asked you to be on time." / Stored
    // 3: +27834484567 / "Yohoooo, I am at your gate."                             / Disregard
    // 4: 0838884567    / "It is dinner time !"                                     / Sent  (Message ID forced to "0838884567" per the brief's search test)
    // 5: +27838884567 / "Ok, I am leaving without you."                           / Stored

    private void loadBriefTestMessages1to4() {
        ERNESTPROG5121PART3.addTestMessage("0001234567", "+27834557896", "Did you get the cake?", "Sent");
        ERNESTPROG5121PART3.addTestMessage("0002234567", "+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored");
        ERNESTPROG5121PART3.addTestMessage("0003234567", "+27834484567", "Yohoooo, I am at your gate.", "Disregarded");
        ERNESTPROG5121PART3.addTestMessage("0838884567", "0838884567", "It is dinner time !", "Sent");
    }

    @Test
    public void testSentMessagesArray_CorrectlyPopulated() {
        loadBriefTestMessages1to4();

        assertTrue(ERNESTPROG5121PART3.sentMessageTexts.contains("Did you get the cake?"));
        assertTrue(ERNESTPROG5121PART3.sentMessageTexts.contains("It is dinner time !"));
        assertEquals(2, ERNESTPROG5121PART3.sentMessageTexts.size());
    }

    @Test
    public void testDisplayLongestMessage() {
        loadBriefTestMessages1to4();

        String longest = ERNESTPROG5121PART3.displayLongestMessage();
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest);
    }

    @Test
    public void testSearchForMessageID() {
        loadBriefTestMessages1to4();

        String result = ERNESTPROG5121PART3.searchByMessageID("0838884567");
        assertEquals("It is dinner time !", result);
    }

    @Test
    public void testSearchAllMessagesForRecipient() {
        loadBriefTestMessages1to4();
        ERNESTPROG5121PART3.addTestMessage("0005234567", "+27838884567", "Ok, I am leaving without you.", "Stored");

        String result = ERNESTPROG5121PART3.searchByRecipient("+27838884567");
        assertEquals("\"Where are you? You are late! I have asked you to be on time.\" \"Ok, I am leaving without you.\"", result);
    }

    @Test
    public void testDeleteMessageUsingHash() {
        ERNESTPROG5121PART3.MessageRecord stored = ERNESTPROG5121PART3.addTestMessage(
                "0002234567", "+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored");

        String result = ERNESTPROG5121PART3.deleteMessageByHash(stored.hash);
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.", result);
    }

    @Test
    public void testDisplayReport() {
        loadBriefTestMessages1to4();

        String report = ERNESTPROG5121PART3.displayReport();
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("It is dinner time !"));
        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient:"));
    }
}