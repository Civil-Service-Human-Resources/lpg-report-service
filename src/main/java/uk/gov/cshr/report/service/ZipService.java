package uk.gov.cshr.report.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ZipService {
    public void createZipFile(String zipFileName, String[] containingFileNames) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(zipFileName);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

        for(String containingFileName: containingFileNames) {
            zipOutputStream.putNextEntry(new ZipEntry(new File(containingFileName).getName()));

            FileInputStream fileInputStream = new FileInputStream(containingFileName);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while((bytesRead = fileInputStream.read(buffer)) != -1){
                zipOutputStream.write(buffer, 0, bytesRead);
            }

            zipOutputStream.closeEntry();
        }
        zipOutputStream.close();
    }

    public void createZipFile(String zipFileName, String containingFileName) throws IOException {
        createZipFile(zipFileName, new String[]{containingFileName});
    }
}
