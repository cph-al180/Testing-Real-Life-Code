package net.sf.javaanpr.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.javaanpr.imageanalysis.CarSnapshot;
import net.sf.javaanpr.intelligence.Intelligence;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

@RunWith(Parameterized.class)
public class RecognitionAllIT {

    private File plateFile;
    private String expectedPlate;
    private CarSnapshot carSnapshot;

    public RecognitionAllIT(File plateFile, String expectedPlate) {
        this.plateFile = plateFile;
        this.expectedPlate = expectedPlate;
    }

    @Before
    public void initialize() throws IOException {
        carSnapshot = new CarSnapshot(new FileInputStream(plateFile));
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testDataCreator() throws FileNotFoundException, IOException {
        String snapshotDirPath = "src/test/resources/snapshots";
        String resultsPath = "src/test/resources/results.properties";

        InputStream resultsStream = new FileInputStream(new File(resultsPath));
        Properties properties = new Properties();
        properties.load(resultsStream);
        File snapshotDir = new File(snapshotDirPath);
        File[] snapshots = snapshotDir.listFiles();
        Collection<Object[]> dataForOneImage = new ArrayList();

        for (File file : snapshots) {
            String name = file.getName();
            String plateExpected = properties.getProperty(name);
            dataForOneImage.add(new Object[]{file, plateExpected});
        }
        return dataForOneImage;
    }

    @Test
    public void testAllCarPlates() throws ParserConfigurationException, SAXException, IOException {
        Intelligence intel = new Intelligence();
        String numberPlate = intel.recognize(carSnapshot, false);
        assertThat(numberPlate, is(expectedPlate));

    }

}
