import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        GameProgress progress1 = new GameProgress(100, 3, 5, 150.5);
        GameProgress progress2 = new GameProgress(90, 2, 3, 100.0);
        GameProgress progress3 = new GameProgress(80, 1, 1, 50.0);

        saveGame("C:\\Users\\ЧПУ Программист\\Desktop\\Games2\\savegames\\save1.dat", progress1);
        saveGame("C:\\Users\\ЧПУ Программист\\Desktop\\Games2\\savegames\\save2.dat", progress2);
        saveGame("C:\\Users\\ЧПУ Программист\\Desktop\\Games2\\savegames\\save3.dat", progress3);

        List<String> files = new ArrayList<>();
        files.add("C:\\Users\\ЧПУ Программист\\Desktop\\Games2\\savegames\\save1.dat");
        files.add("C:\\Users\\ЧПУ Программист\\Desktop\\Games2\\savegames\\save2.dat");
        files.add("C:\\Users\\ЧПУ Программист\\Desktop\\Games2\\savegames\\save3.dat");

        zipFiles("C:\\Users\\ЧПУ Программист\\Desktop\\Games2\\savegames\\zip.zip", files);

        deleteFilesNotInArchive(files);
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
                File file = new File(filePath);
                FileInputStream fis = new FileInputStream(file);
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }

                fis.close();
            }
            zos.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFilesNotInArchive(List<String> files) {
        File[] saveGameFiles = new File("C:\\Users\\ЧПУ Программист\\Desktop\\Games2\\savegames").listFiles();
        if (saveGameFiles != null) {
            for (File file : saveGameFiles) {
                if (!files.contains(file.getAbsolutePath()) && !file.isDirectory()) {
                    file.delete();
                }
            }
        }
    }
}