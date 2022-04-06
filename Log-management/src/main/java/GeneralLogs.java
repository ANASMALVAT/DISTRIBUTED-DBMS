import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GeneralLogs {

    public static void main(String[] args) {

        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;

        try {

            fh = new FileHandler("D:/MACS Dalhousie/Data project/Generated Log files/GeneralLogFile.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            logger.info("General logs");

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("Printing General log files");


    }

}
