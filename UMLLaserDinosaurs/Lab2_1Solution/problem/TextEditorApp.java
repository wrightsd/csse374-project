package problem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class TextEditorApp {
	public static void main(String[] args) throws Exception {
		SubstitutionCipher cipher = new SubstitutionCipher();
		InputStream fIn = new FileInputStream("./input_output/out.txt");
		OutputStream fOut = new FileOutputStream("./input_output/out1.txt");		
		InputStream in = new DecryptionInputStream(fIn, cipher);
		OutputStream out = new EncryptionOutputStream(fOut, cipher);
		
		TextEditor editor = new TextEditor(in, out);
		editor.execute();
	}	
}
