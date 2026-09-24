package com.devflow;

import com.devflow.util.DateUtil;
import com.devflow.util.PasswordUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

public class DevFlowTest {

    @Test
    @DisplayName("Test BCrypt Password Hashing and Verification")
    public void testPasswordHashing() {
        String plainPassword = "password123";
        String hash = PasswordUtil.hashPassword(plainPassword);
        System.out.println("BCRYPT_PASSWORD_HASH=" + hash);

        assertNotNull(hash);
        assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$") || hash.startsWith("$2y$"));
        assertTrue(PasswordUtil.checkPassword(plainPassword, hash));
        assertFalse(PasswordUtil.checkPassword("wrongPassword", hash));
    }

    @Test
    @DisplayName("Test Date and Timestamp Parsing & Formatting")
    public void testDateFormatting() {
        Date parsedDate = DateUtil.parseDate("2026-09-19");
        assertNotNull(parsedDate);
        assertEquals("2026-09-19", DateUtil.formatIsoDate(parsedDate));

        Timestamp now = new Timestamp(System.currentTimeMillis());
        String formattedTs = DateUtil.formatTimestamp(now);
        assertNotNull(formattedTs);
        assertFalse(formattedTs.equals("-"));
    }
}
