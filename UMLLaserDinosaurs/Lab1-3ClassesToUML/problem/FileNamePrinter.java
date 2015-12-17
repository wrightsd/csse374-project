package problem;

import java.io.IOException;

public class FileNamePrinter implements Observer {

	@Override
	public void update(AppLauncher launch) {

		String eventName = launch.getCurrentEventType();

		if (!eventName.equals("ENTRY_CREATE"))
			return;

		String fileName = launch.getCurrentFilePathString();

		System.out.println("New File Added: " + fileName);

	}

}
