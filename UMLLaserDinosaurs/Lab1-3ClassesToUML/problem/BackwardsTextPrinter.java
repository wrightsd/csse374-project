package problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BackwardsTextPrinter implements Observer {

	@Override
	public void update(AppLauncher launch) {
		
		String eventName = launch.getCurrentEventType();

		if (!eventName.equals("ENTRY_MODIFY"))
			return;

		String fileName = launch.getCurrentFilePathString();

		if (fileName.endsWith(".txt")) {
			
			Charset charset = Charset.forName("US-ASCII");
			try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName), charset)) {

				StringBuffer buffer = new StringBuffer();
				String line = null;

				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				
				String textString = buffer.toString();
				
				char[] reverseString = new char[textString.length()];
				int j = 0;
				
				for(int i = textString.length()-1; i >= 0; i--){
					reverseString[j] = textString.charAt(i);
					j++;
				}
				System.out.println(reverseString);
				
			} catch (IOException x) {
			    System.err.format("IOException: %s%n", x);
			}	
		} else {
			return;
		}

	}

}
