package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Selenide;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selectors.byText;
import static org.assertj.core.api.Assertions.assertThat;

public class FilesParsingTest {

    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    void parsePdfTests() throws Exception {
        Selenide.open("https://junit.org/junit5/docs/current/user-guide/");
        File pdfDownload = Selenide.$(byText("PDF download")).download();
        PDF pdf = new PDF(pdfDownload);
        assertThat(pdf.author).contains("Stefan Bechtold");
        assertThat(pdf.numberOfPages).isEqualTo(166);
    }

    @Test
    void parseXLsTest() throws Exception {
       Selenide.open("http://romashka2008.ru/price");
       File xlsDownload = Selenide.$(".site-main__inner a[href*='prajs_ot']").download();
       XLS xls = new XLS(xlsDownload);
       assertThat(xls.excel
                .getSheetAt(0)
                .getRow(11)
                .getCell(1)
               .getStringCellValue()).contains("Южно-Сахалинск");
    }

    @Test
    void parseCsvTest() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("files/Machine_readable_file_bdc_sf_2021_q4.csv");
             CSVReader reader = new CSVReader(new InputStreamReader(is))) {
            List<String[]> content = reader.readAll();
            assertThat(content.get(0)).contains("Series_reference", "Period", "Data_value", "Suppressed", "STATUS", "UNITS", "Magnitude", "Subject", "Group", "Series_title_1", "Series_title_2", "Series_title_3", "Series_title_4", "Series_title_5");
        }
    }

    @Test
    void parceZipTest() throws Exception {
        try (InputStream is = classLoader.getResourceAsStream("files/sample-zip-file.zip");
            ZipInputStream zip = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                assertThat(entry.getName()).isEqualTo("sample.txt");
            }
            }
        }
    }


