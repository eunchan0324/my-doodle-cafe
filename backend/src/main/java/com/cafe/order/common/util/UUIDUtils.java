package com.cafe.order.common.util;

import java.util.UUID;

public class UUIDUtils {
    // byte[] -> UUID 변환 헬퍼 (읽기용)
    public static UUID convertBytesToUUID(byte[] bytes) {
        if (bytes == null || bytes.length != 16) {
            return null;
        }

        long mostSigBits = 0;
        long leastSigBits = 0;

        for (int i = 0; i < 8; i++) {
            mostSigBits = (mostSigBits << 8) | (bytes[i] & 0xff);
        }

        for (int i = 8; i < 16; i++) {
            leastSigBits = (leastSigBits << 8) | (bytes[i] & 0xff);
        }

        return new UUID(mostSigBits, leastSigBits);
    }

    // UUID -> byte[] 변환 헬퍼 (쓰기용)
    public static byte[] convertUUIDToBytes(UUID uuid) {
        if (uuid == null) {
            return null;
        }

        byte[] bytes = new byte[16];
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();

        // mostSigBits를 앞 8바이트에
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (mostSigBits >> (8 * (7 - i)));
        }

        for (int i = 0; i < 8; i++) {
            bytes[8 + i] = (byte) (leastSigBits >> (8 * (7 - i)));
        }

        return bytes;
    }

    private UUIDUtils() {
    }
}
