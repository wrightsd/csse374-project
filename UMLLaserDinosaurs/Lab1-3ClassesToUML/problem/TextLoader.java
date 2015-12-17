package problem;

import java.io.IOException;
import java.nio.file.Path;

public class TextLoader implements Observer {

	@Override
	public void update(AppLauncher launch) {

		String eventName = launch.getCurrentEventType();

		if (!eventName.equals("ENTRY_CREATE"))
			return;

		String fileName = launch.getCurrentFilePathString();

		if (fileName.endsWith(".txt")) {
			
			ProcessBuilder processBuilder = null;
			String command = null;
			String arg = null;

			command = "Notepad";
			arg = fileName;

			// Run the application if support is available
			try {
				System.out.format("Launching %s ...%n", command);
				processBuilder = new ProcessBuilder(command, arg);

				// Start and add the process to the processes list
				Process process = processBuilder.start();
				launch.addProcesses(process);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return;
		}

	}

}
