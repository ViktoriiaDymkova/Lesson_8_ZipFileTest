package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipFileTest1 {

    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    void zipFileTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/files/ziptest2.zip"));
        ZipInputStream is = new ZipInputStream(classLoader.getResourceAsStream("files/ziptest2.zip"));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null) {
            if (entry.getName().equals("etoPdf.pdf")) {
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    PDF pdf = new PDF(inputStream);
                    assertThat(entry.getName()).isEqualTo("etoPdf.pdf");
                }
            } else if (entry.getName().equals("etoCsv.csv")) {
                try (InputStream inputStream = zf.getInputStream(entry);
                     CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
                    List<String[]> content = reader.readAll();
                    org.assertj.core.api.Assertions.assertThat(content.get(2)).
                            contains("BDCQ.SF1AA2CA", "Business Data Collection - BDC", "Industry by financial variable", "Current prices", "Unadjusted");

                }
            } else if (entry.getName().equals("etoExel.xls")) {
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    XLS xls = new XLS(inputStream);
                    assertThat(xls.excel
                            .getSheetAt(0)
                            .getRow(3)
                            .getCell(1)
                            .getStringCellValue()).contains("How have the original Henry Hornbostel buildings influenced campus architecture and design in the last 30 years?");
                }
            }
        }
    }
}