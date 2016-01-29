package problem;

public class SubstitutionCipher implements IEncryption, IDecryption {
	private char[] encryptionKey, decryptionKey;
	
	public SubstitutionCipher() {
		encryptionKey = new char[128];
		decryptionKey = new char[128];
		
		for(int i = 0; i < 128; ++i) {
			encryptionKey[i] = (char)i;
			decryptionKey[i] = (char)i;
		}
		
		for(int i = 65; i < 91; ++i) {
			encryptionKey[i] = (char)(65 + 90 - i);
			decryptionKey[65 + 90 - i] = (char)i;
		}

		for(int i = 97; i < 123; ++i) {
			encryptionKey[i] = (char)(97 + 122 - i);
			decryptionKey[97 + 122 - i] = (char)i;
		}
	}

	@Override
	public char encrypt(char plain) {
		return this.encryptionKey[(int)plain];
	}

	@Override
	public char decrypt(char cipher) {
		return this.decryptionKey[(int)cipher];
	}
}
