package helpers;

import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class DateTimeHelper {
    public static List<String> getTimeAsList(ResultSet resultSet) {
        List<String> listToReturn = new ArrayList<>();
        String timePattern = "HH:mm";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(timePattern);

        try {
            while (resultSet.next()) {
                listToReturn.add(" " + dateFormatter.format(resultSet.getTime(1).toLocalTime()));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return listToReturn;
    }
}