package catmoe.akkariin.akanefield.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    public static File getOrCreateFile(String fileID, AkaneFieldFolder folder) {
        File file = new File(ServerUtil.getDataFolder() + "/" + folder.getFolder(), fileID);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                // Logger.debug("Creating new file at " + file.getPath());
            } catch (Exception e) {
                // Logger.error("&eError during file creation! " + fileID);
            }
        }
        return file;
    }

    public static void writeBase64(String fileID, AkaneFieldFolder folder, Object o) {
        File file = getOrCreateFile(fileID, folder);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(SerializeUtil.serialize(o));
            writer.close();
        } catch (IOException e) {
            // Logger.error("Error during file Writing! " + fileID);
            e.printStackTrace();
        }
    }

    public static String getEncodedBase64(String fileID, AkaneFieldFolder folder) {
        String str = "";
        File file = getOrCreateFile(fileID, folder);
        try {
            FileReader fileReader = new FileReader(file);
            try (BufferedReader reader = new BufferedReader(fileReader)) {
                str = reader.readLine();
            }
        } catch (IOException e) {
            // Logger.error("Error during file Reading! " + fileID);
            e.printStackTrace();
        }
        return str;
    }

    public static String getEncodedBase64(File file) {
        String str = "";
        try {
            FileReader fileReader = new FileReader(file);
            try (BufferedReader reader = new BufferedReader(fileReader)) {
                str = reader.readLine();
            }
        } catch (IOException e) {
            // Logger.error("Error during file Reading! " + fileID);
            e.printStackTrace();
        }
        return str;
    }

    public static File[] getFiles(AkaneFieldFolder folder) {
        File f = new File(ServerUtil.getDataFolder() + "/" + folder.getFolder());
        File[] array = f.listFiles();

        return array == null ? new File[] {} : array;
    }

    public static void writeLine(String fileID, AkaneFieldFolder folder, String line) {
        File file = getOrCreateFile(fileID, folder);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(line);
            writer.close();
        } catch (IOException e) {
            // Logger.error("Error during file Writing! " + fileID);
            e.printStackTrace();
        }
    }

    public enum AkaneFieldFolder {
        BACKUP,
        DATA,
        LOGS;

        public String getFolder() {
            return this.name().toLowerCase();
        }
    }
}
