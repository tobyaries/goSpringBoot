package beans.cfgRd.impl;

import beans.cfgRd.service.DataSource;

public class FileDataSource implements DataSource {
    @Override
    public void readData() {
        // Simulate reading data from a file.
        System.out.println("Reading data from file.");
    }
}
