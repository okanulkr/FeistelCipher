import java.util.Arrays;
import java.util.Random;

public class FeistelCipher {
	
	private static String PLAINTEXT = "CENG474";
	private static final int ROUNDS = 2;

	private static int KEY_LEN = (int) Math.ceil(PLAINTEXT.length() / 2.0);
	private static byte[][] KEYS = new byte[ROUNDS][KEY_LEN];
	
	public static void main(String[] args) {
		checkArguments(args);
		
		System.out.println(String.format("\nThe plaintext is \t%s", PLAINTEXT));
		
		byte[] plain = PLAINTEXT.getBytes();
		byte[] left = Arrays.copyOfRange(plain, 0, KEY_LEN);
		byte[] right = Arrays.copyOfRange(plain, KEY_LEN, plain.length);
		byte[] cipher = encrypt(left, right);
		
		System.out.println(String.format("\nThe ciphertext is \t%s", new String(cipher)));

		left = Arrays.copyOfRange(cipher, 0, KEY_LEN);
		right = Arrays.copyOfRange(cipher, KEY_LEN, cipher.length);
		plain = decrypt(left, right);
		
		System.out.println(String.format("\nDecrypted plaintext is \t%s", new String(plain) + "\n"));
	}

	private static byte[] encrypt(byte[] left, byte[] right) {
		for (int i = 0; i < ROUNDS; i++) {
			byte[] key = getRandomKey(KEY_LEN);
			KEYS[i] = key;
			
			byte[] temp = right.clone();
			right = xor(left,  xor(right, key));
			left = temp.clone();
		}
		
		return concat(left, right);
	}
	
	private static byte[] decrypt(byte[] left, byte[] right) {
		for (int i = 1; i > -1; i--) {
			byte[] key = KEYS[i];
			
			byte[] temp = left.clone();
			left = xor(right, xor(left, key));
			right = temp.clone();
		}
		
		return concat(left, right);
	}

	private static byte[] concat(byte[] first, byte[] second) {
		byte[] concat = new byte[first.length + second.length];
		System.arraycopy(first, 0, concat, 0, first.length);
		System.arraycopy(second, 0, concat, first.length, second.length);
		return concat;
	}

	private static byte[] xor(byte[] first, byte[] second) {
		int len = first.length;
		if (len < second.length)
			len = second.length;
		
		byte[] xor = new byte[len];
		
		for(int i = 0; i < len; i++) {
			long temp;
			
			try {
				temp = first[i] ^ second[i];
				
			} catch (Exception e) {
				if (first.length > second.length)
					temp = first[i];
				else
					temp = second[i];
			}
			
			xor[i] = (byte) temp;
		}
		
		return xor;
	}

	private static byte[] getRandomKey(int length) {
		byte[] key = new byte[length];
		new Random().nextBytes(key);
		return key;
	}
	
	private static void checkArguments(String[] args) {
		if (args.length > 0 && args[0] != null && !args[0].isEmpty()) {
			PLAINTEXT = args[0];
			KEY_LEN = (int) Math.ceil(PLAINTEXT.length() / 2.0);
			KEYS = new byte[ROUNDS][KEY_LEN];
		}
	}
}
