package problem;

import java.io.IOException;

public class WordLoader implements Observer {

	@Override
	public void update(AppLauncher launch) {

		String eventName = launch.getCurrentEventType();

		if (!eventName.equals("ENTRY_CREATE"))
			return;

		String fileName = launch.getCurrentFilePathString();

		if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
			
			if(fileName.contains("~$"))
				return;

			ProcessBuilder processBuilder = null;
			String command = null;
			String arg = null;
			
			command = "C:/Program Files (x86)/Microsoft Office/Office15/WINWORD.EXE";
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
		} else{
			return;
		}

	}

}
