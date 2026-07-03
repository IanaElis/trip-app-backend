package travel.util;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class CountryLoader {

    public Map<String, String> loadCountries(){
        Map<String, String> countries = new HashMap<>();
        try (
                InputStream is = getClass()
                        .getResourceAsStream("/data/countries.csv");
                Reader reader = new InputStreamReader(is);

                CSVParser parser = CSVFormat.DEFAULT
                        .builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .get().parse(reader);
        ) {
            for (CSVRecord record : parser) {
                countries.put(
                        record.get("code"),
                        record.get("name")
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return countries;

    }

}
