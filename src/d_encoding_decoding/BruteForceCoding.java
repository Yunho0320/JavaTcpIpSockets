package d_encoding_decoding;

/*
This class demonstrates shows how to manually encode and decode primitive data types (byte, short, int, long)
into a byte array, using big-endian byte order.
 */
public class BruteForceCoding {
    // Four values to be encoded into bytes
    private static byte byteVal = 101; // one hundred and one
    private static short shortVal = 10001; // ten thousand and one
    private static int intVal = 100000001; // one hundred million and one
    private static long longVal = 1000000000001L; // one trillion and one

    private final static int BSIZE = Byte.SIZE / Byte.SIZE; //1
    private final static int SSIZE = Short.SIZE / Byte.SIZE; //2
    private final static int ISIZE = Integer.SIZE / Byte.SIZE; //4
    private final static int LSIZE = Long.SIZE / Byte.SIZE; //8

    /*
    What is Bytemask for?
    See EncodingPrerequisiteOne and EncodingPrerequisiteTwo
     */
    private final static int BYTEMASK = 0xFF; // 11111111 in bits and 255 in decimal

    public static String byteArrayToDecimalString(byte[] bArray) {
        StringBuilder rtn = new StringBuilder();
        for (byte b : bArray) {
            rtn.append(b & BYTEMASK).append(" ");
        }
        return rtn.toString();
    }

    // Warning: Untested preconditions (e.g., 0 <= size <= 8)
    public static int encodeIntBigEndian(byte[] dst, long val, int offset, int size) {
        for (int i = 0; i < size; i++) {
            dst[offset++] = (byte) (val >> ((size - i - 1) * Byte.SIZE));
        }
        return offset;
    }

    // Warning: Untested preconditions (e.g., 0 <= size <= 8)
    public static long decodeIntBigEndian(byte[] val, int offset, int size) {
        long rtn = 0;
        for (int i = 0; i < size; i++) {
            rtn = (rtn << Byte.SIZE) | ((long) val[offset + i] & BYTEMASK);
        }
        return rtn;
    }

    public static void main(String[] args) {
        // Creates a byte array that can hold all the encoded values (1 + 2 + 4 + 8 = 15 bytes).
        byte[] message = new byte[BSIZE + SSIZE + ISIZE + LSIZE];

        // Encode the fields in the target byte array
        int offset = encodeIntBigEndian(message, byteVal, 0, BSIZE);
        offset = encodeIntBigEndian(message, shortVal, offset, SSIZE);
        offset = encodeIntBigEndian(message, intVal, offset, ISIZE);
        encodeIntBigEndian(message, longVal, offset, LSIZE);
        System.out.println("Encoded message: " + byteArrayToDecimalString(message));

        // Decode several fields
        long value = decodeIntBigEndian(message, BSIZE, SSIZE);
        System.out.println("Decoded short = " + value);
        value = decodeIntBigEndian(message, BSIZE + SSIZE + ISIZE, LSIZE);
        System.out.println("Decoded long = " + value);

        // Demonstrate dangers of conversion
        offset = 4;
        value = decodeIntBigEndian(message, offset, BSIZE);
        System.out.println("Decoded value (offset " + offset + ", size " + BSIZE + ") = " + value);
        byte bVal = (byte) decodeIntBigEndian(message, offset, BSIZE);
        System.out.println("Same value as byte = " + bVal);
    }
}
