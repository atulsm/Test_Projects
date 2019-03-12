import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class TestFolderWatcher {

	public static void main(String[] args) throws Exception{
		Path testFolder = Paths.get("test");
		WatchService service = FileSystems.getDefault().newWatchService();
		WatchKey key = testFolder.register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
		
		do {
			List<WatchEvent<?>> data = key.pollEvents();
			if(!data.isEmpty()) {
				for(WatchEvent<?> e : data) {
					System.out.println("Count: " + e.count() + ", Kind: " + e.kind() + ", Context: " + e.context());
				}
			}
		}while(key.reset());
	}
}
