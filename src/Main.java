import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Main {
	public static void main(String[] args) {
		GameProgress progress1 = new GameProgress(100, 3, 5, 150.5);
		GameProgress progress2 = new GameProgress(90, 2, 3, 100.0);
		GameProgress progress3 = new GameProgress(80, 1, 1, 50.0);

		saveGame("C:\\Users\\Alex\\Desktop\\Games2\\savegames\\save1.dat", progress1);
		saveGame("C:\\Users\\Alex\\Desktop\\Games2\\savegames\\save2.dat", progress2);
		saveGame("C:\\Users\\Alex\\Desktop\\Games2\\savegames\\save3.dat", progress3);

		List<String> files = new ArrayList<>();
		files.add("C:\\Users\\Alex\\Desktop\\Games2\\savegames\\save1.dat");
		files.add("C:\\Users\\Alex\\Desktop\\Games2\\savegames\\save2.dat");
		files.add("C:\\Users\\Alex\\Desktop\\Games2\\savegames\\save3.dat");

		zipFiles("C:\\Users\\Alex\\Desktop\\Games2\\savegames\\zip.zip", files);
		deleteFiles(files);
		extractFilesFromArchive("C:\\Users\\Alex\\Desktop\\Games2\\savegames\\zip.zip", "C:\\Users\\Alex\\Desktop\\Games2\\savegames\\extracted");

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
					ZipEntry zipEntry = new ZipEntry(filePath);
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

	public static void extractFilesFromArchive(String zipFilePath, String extractPath) {
		try (ZipFile zipFile = new ZipFile(zipFilePath)) {
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				String filePath = extractPath + File.separator + entry.getName();
				try (InputStream is = zipFile.getInputStream(entry);
					 FileOutputStream fos = new FileOutputStream(filePath)) {
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0) {
						fos.write(buffer, 0, length);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
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