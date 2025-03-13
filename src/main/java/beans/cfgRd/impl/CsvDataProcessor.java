package beans.cfgRd.impl;

import beans.cfgRd.service.DataProcessor;
import beans.cfgRd.service.DataSource;

public class CsvDataProcessor implements DataProcessor {

    private DataSource dataSource;

    // 静态初始化日志
    static {
        System.out.println("CsvDataProcessor static initialization started.");
        // 静态初始化代码
        System.out.println("CsvDataProcessor static initialization finished.");
    }

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