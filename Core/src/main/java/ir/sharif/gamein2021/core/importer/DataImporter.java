package ir.sharif.gamein2021.core.importer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class DataImporter<T> {
    private final CSVHelper<T> csvHelper;
    private final JpaRepository<T, Integer> repository;

    public DataImporter(CSVHelper<T> csvHelper, JpaRepository<T, Integer> repository) {
        this.csvHelper = csvHelper;
        this.repository = repository;
    }

    public void importData(String filePath) throws FileNotFoundException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream(filePath);
//        InputStream inputStream = new FileInputStream("CSVFiles/team.csv");
        List<T> teamList = csvHelper.csvToEntities(inputStream);
        if ((long) repository.findAll().size() == 0) {
            repository.saveAll(teamList);
        }
    }
}
