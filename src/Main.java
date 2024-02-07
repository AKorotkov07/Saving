import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
	public static void main(String[] args) {
		GameProgress progress1 = new GameProgress(100, 3, 5, 150.5);
		GameProgress progress2 = new GameProgress(90, 2, 3, 100.0);
		GameProgress progress3 = new GameProgress(80, 1, 1, 50.0);

		saveGame("C:/Users/Alex/Desktop/Games2/savegames/save1.dat", progress1);
		saveGame("C:/Users/Alex/Desktop/Games2/savegames/save2.dat", progress2);
		saveGame("C:/Users/Alex/Desktop/Games2/savegames/save3.dat", progress3);

		List<String> files = new ArrayList<>();
		files.add("C:/Users/Alex/Desktop/Games2/savegames/save1.dat");
		files.add("C:/Users/Alex/Desktop/Games2/savegames/save2.dat");
		files.add("C:/Users/Alex/Desktop/Games2/savegames/save3.dat");

		zipFiles("C:/Users/Alex/Desktop/Games2/savegames/zip.zip", files);
		deleteFiles(files);

	}

	public static void saveGame(String filePath, GameProgress progress) {
		try (FileOutputStream fos = new FileOutputStream(filePath);
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(progress);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void zipFiles(String zipFilePath, List<String> files) {
		try (FileOutputStream fos = new FileOutputStream(zipFilePath);
			 ZipOutputStream zos = new ZipOutputStream(fos)) {
			for (String filePath : files) {
				try (FileInputStream fis = new FileInputStream(filePath)) {
					ZipEntry zipEntry = new ZipEntry(filePath.substring(filePath.lastIndexOf('/') + 1));
					zos.putNextEntry(zipEntry);
					byte[] buffer = new byte[fis.available()];
					fis.read(buffer);
					zos.write(buffer);
					zos.closeEntry();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			zos.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void extractFilesFromArchive(String zipPath, String extractPath) {
		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath))) {
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				String filePath = extractPath + File.separator + entry.getName();
				try (FileOutputStream fos = new FileOutputStream(filePath)) {
					for (int c = zis.read(); c != -1; c = zis.read()) {
						fos.write(c);
					}
					fos.flush();
				}
				zis.closeEntry();
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	public static void deleteFiles(List<String> files) {
		for (String filePath : files) {
			try {
				Files.delete(Paths.get(filePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}