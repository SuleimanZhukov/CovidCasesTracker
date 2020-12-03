package com.suleiman.covidcases.service;

import com.suleiman.covidcases.model.Stats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CovidCasesService {

    private static final String CASES_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<Stats> stats = new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "* 1 * * * *")
    public void getCovidData() throws IOException, InterruptedException {
        List<Stats> newStats = new ArrayList<>();
        int allCases;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(CASES_DATA_URL)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        StringReader responseReader = new StringReader(response.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(responseReader);
        for (CSVRecord record : records) {
//            boolean doContinue = false;
            String country = record.get("Country/Region");
//            for (int i = 0; i < stats.size(); i++) {
//                if (stats.get(i).getCountry().equals(country)) {
//                    doContinue = true;
//                    break;
//                }
//            }
//            if (doContinue) {
//                break;
//            }
            Stats stats = new Stats();
            stats.setCountry(country);
            stats.setState(record.get("Province/State"));
            int totalCases = Integer.parseInt(record.get(record.size() - 1));
            int totalCasesPrevious = Integer.parseInt(record.get(record.size() - 2));
            int changes = totalCases - totalCasesPrevious;
            stats.setTotalCases(totalCases);
            stats.setChanges(changes);
            newStats.add(stats);
        }
        stats = newStats;
    }

    public List<Stats> getStats() {
        return stats;
    }
}
