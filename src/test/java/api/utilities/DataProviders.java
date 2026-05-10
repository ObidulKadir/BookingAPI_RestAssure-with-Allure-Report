package api.utilities;

import java.io.IOException;
import org.testng.annotations.DataProvider;

public class DataProviders {

    // 🔹 Get all data (2D array)
    @DataProvider(name = "AuthData")
    public String[][] getAllData() throws IOException {

        String path = System.getProperty("user.dir") + "//TestData//Auth.xlsx";
        XLUtility xl = new XLUtility(path);

        int rownum = xl.getRowCount("Sheet1");
        int colcount = xl.getCellCount("Sheet1", 1);

        String apidata[][] = new String[rownum][colcount];

        for (int i = 1; i <= rownum; i++) { // skip header
            for (int j = 0; j < colcount; j++) {
                apidata[i - 1][j] = xl.getCellData("Sheet1", i, j);
            }
        }
        return apidata;
    }
    
    // 🔹 Get all data (2D array)
    @DataProvider(name = "BookingData")
    public String[][] getAllBookingData() throws IOException {

        String path = System.getProperty("user.dir") + "//TestData//BookingAPI.xlsx";
        XLUtility xl = new XLUtility(path);

        int rownum = xl.getRowCount("Sheet1");
        int colcount = xl.getCellCount("Sheet1", 1);

        String apidata[][] = new String[rownum][colcount];

        for (int i = 1; i <= rownum; i++) { // skip header
            for (int j = 0; j < colcount; j++) {
                apidata[i - 1][j] = xl.getCellData("Sheet1", i, j);
            }
        }
        return apidata;
    }

//    // 🔹 Get only usernames (1D array)
//    @DataProvider(name = "UserNames")
//    public String[] getUserNames() throws IOException {
//
//    	String path = System.getProperty("user.dir") + "//TestData//Auth.xlsx";
//        XLUtility xl = new XLUtility(path);
//
//        int rownum = xl.getRowCount("Sheet1");
//
//        String usernames[] = new String[rownum];
//
//        for (int i = 1; i <= rownum; i++) {
//            usernames[i - 1] = xl.getCellData("Sheet1", i, 1); // column 1 = username
//        }
//
//        return usernames;
//    }
}