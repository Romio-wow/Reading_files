package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ParsingZipFiles {
    ClassLoader classLoader = getClass().getClassLoader();


    @Test
    @DisplayName("Проверка данных из zip файла")
    void parseZipTests() throws Exception {
        try (InputStream is = classLoader.getResourceAsStream("files/Archive.zip");
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("example_PDF.pdf")) {
                    {
                        PDF pdf = new PDF(zis);
                        assertThat(pdf.text).contains("PDF");
                    }
                } else if (entry.getName().equals("TestXLSX.xlsx")) {
                    {
                        XLS xls = new XLS(zis);
                        String stringCellValue = xls.excel.getSheetAt(0).getRow(28).getCell(4).getStringCellValue();
                        assertThat(stringCellValue).contains("France");
                    }

                } else if (entry.getName().equals("teachers.csv")) {
                    CSVReader reader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> content = reader.readAll();
                    assertThat(content.get(0)).contains("Name", "Surname");

                }


            }
        }
    }
}