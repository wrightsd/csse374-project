package problem;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class EncryptionOutputStream extends FilterOutputStream {
	private IEncryption encryptor;

	public EncryptionOutputStream(OutputStream out, IEncryption encryptor) {
		super(out);
		this.encryptor = encryptor;
	}

	@Override
	public void write(int b) throws IOException {
		char cipher = this.encryptor.encrypt((char)b);
		super.write(cipher);
	}
}
