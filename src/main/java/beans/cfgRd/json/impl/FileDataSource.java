package beans.cfgRd.json.impl;

import beans.cfgRd.json.interfaces.DataSource;

public class FileDataSource implements DataSource {
    @Override
    public void readData() {
        // Simulate reading data from a file.
        System.out.println("Reading data from file.");
    }
}
