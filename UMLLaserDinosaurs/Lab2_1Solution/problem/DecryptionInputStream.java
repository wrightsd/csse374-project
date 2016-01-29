package problem;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DecryptionInputStream extends FilterInputStream {
	private IDecryption decryptor;

	public DecryptionInputStream(InputStream in, IDecryption decryptor) {
		super(in);
		this.decryptor = decryptor;
	}

	@Override
	public int read() throws IOException {
		int cipher = super.read();
		if(cipher == -1)
			return cipher;
		return this.decryptor.decrypt((char)cipher);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int totalBytes =  super.read(b, off, len);
		
		for (int i = off; i < off + totalBytes; i++) {
			if(b[i] != -1) {
				System.out.print((char)b[i]);
				b[i] = (byte) this.decryptor.decrypt((char)b[i]);
			}
		}
		
		return totalBytes;
	}
}
