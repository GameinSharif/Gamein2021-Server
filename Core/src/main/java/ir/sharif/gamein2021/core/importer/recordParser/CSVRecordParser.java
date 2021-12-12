package ir.sharif.gamein2021.core.importer.recordParser;

import org.apache.commons.csv.CSVRecord;

public interface CSVRecordParser<T> {
    T parse(CSVRecord record);
}
