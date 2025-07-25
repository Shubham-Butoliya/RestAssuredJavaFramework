package com.api.framework.utils;

import com.poiji.annotation.ExcelRow;
import com.poiji.bind.Poiji;
import com.poiji.option.PoijiOptions;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelUtils {

    public static List<LinkedHashMap<String,String>> getExcelDataAsListOfMap(String excelFileName, String sheetName) throws IOException {
        List<LinkedHashMap<String,String>> dataFromExcel = new ArrayList<>();
//        Workbook workbook = WorkbookFactory.create(new File("src/test/resources/testdata/" + excelFileName + ".xlsx"));

        try (FileInputStream fis = new FileInputStream("src/test/resources/testdata/" + excelFileName + ".xlsx");
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);

            int totalRows = sheet.getPhysicalNumberOfRows();
            LinkedHashMap<String, String> mapData;
            List<String> allKeys = new ArrayList<>();
            DataFormatter dataFormatter = new DataFormatter();
            for (int i = 0; i < totalRows; i++) {
                mapData = new LinkedHashMap<>();
                if (i == 0) {
                    int totalCols = sheet.getRow(0).getPhysicalNumberOfCells();
                    for (int j = 0; j < totalCols; j++) {
                        allKeys.add(sheet.getRow(0).getCell(j).getStringCellValue());
                    }
                } else {
                    int totalCols = sheet.getRow(i).getPhysicalNumberOfCells();
                    for (int j = 0; j < totalCols; j++) {
                        String cellValue = dataFormatter.formatCellValue(sheet.getRow(i).getCell(j));
                        int size = 6;
                        if (cellValue.contains("RandomNumber")) {
                            // With size
                            if (cellValue.contains("_")) {
                                size = Integer.parseInt((cellValue.split("_"))[1]);
                            }
                            cellValue = RandomDataGenerator.getRandomNumber(size);
                        }
                        mapData.put(allKeys.get(j), cellValue);
                    }
                    dataFromExcel.add(mapData);
                }
            }
            dataFromExcel.stream()
                    .filter(keyValuePair -> {
                        String enabled = keyValuePair.get("Enabled");
                        return enabled != null && enabled.equalsIgnoreCase("Y");
                    })
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return dataFromExcel;
    }

}
