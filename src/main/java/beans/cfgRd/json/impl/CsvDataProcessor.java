package beans.cfgRd.json.impl;

import beans.cfgRd.json.interfaces.DataProcessor;
import beans.cfgRd.json.interfaces.DataSource;

public class CsvDataProcessor implements DataProcessor {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        System.out.println("CsvDataProcessor.setDataSource() called with: " + dataSource);
        this.dataSource = dataSource;
    }

    @Override
    public void processData() {
        System.out.println("Processing CSV data using: ");
        dataSource.readData();
    }
}