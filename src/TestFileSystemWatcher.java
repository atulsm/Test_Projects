import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class TestFileSystemWatcher implements Runnable {

	public static void main(String[] args) {
		TestFileSystemWatcher test = new TestFileSystemWatcher(new File("./test.txt"));
		Thread testThread = new Thread(test);
		testThread.start();
	}

	private final File watchedFile;
	private long lastModified = 0;

	public TestFileSystemWatcher(File whiteListFile) {
		this.watchedFile = whiteListFile;
	}

	public void reload() {
		System.out.println(watchedFile + " is modified");
	}

	@Override
	public void run() {
		try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
			Path whiteListDir = watchedFile.toPath().getParent();
			whiteListDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

			while (!Thread.currentThread().isInterrupted()) {
				System.out.println("In loop");
				WatchKey key;
				try {
					key = watchService.take();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return;
				}

				if (key == null) {
					continue;
				}

				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> eventKind = event.kind();

					@SuppressWarnings("unchecked")
					WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
					Path modifiedFileName = pathEvent.context();

					if (eventKind == StandardWatchEventKinds.OVERFLOW) {
						continue;
					} else if (eventKind == java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY
							&& modifiedFileName.toString().equals(watchedFile.getName())
							&& watchedFile.lastModified() - lastModified > 100) {
						lastModified = watchedFile.lastModified();
						reload();
					}
				}
				
				boolean valid = key.reset();
				if (!valid) {
					System.out.println("Not valid");
					break;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
