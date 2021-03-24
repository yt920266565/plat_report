package com.yanmade.plat.report.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 类说明
 */
public class CsvUtil {
    private static final Logger logger = LoggerFactory.getLogger(CsvUtil.class);
    private static String error = "异常错误：";
    private static final String CHARSET = "GBK";

    private CsvUtil() {

    }

    /**
     * 
     * @Description: 读取csv的最后一行
     * @param csvPath
     *            csv路径
     * @return String 最后一行数据
     * @throws IOException
     */
    public static String getCsvData(String csvPath) {
        StringBuilder result = new StringBuilder();
        int column;// 列数目
        try (Reader in = new InputStreamReader(new FileInputStream(csvPath), CHARSET);) {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
            Iterator<CSVRecord> iterator = records.iterator();
            while (iterator.hasNext()) {
                CSVRecord record = iterator.next();
                column = record.size();
                if (!iterator.hasNext()) {
                    for (int i = 0; i < column; i++) {
                        if (i == column - 1) {
                            result.append(record.get(i));
                        } else {
                            result.append(record.get(i) + ",");
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error(error, e);
        }

        return result.toString();
    }

    /**
     * 
     * @Description: 读取csv的某一行
     * @param csvPath
     *            csv路径
     * @param index
     *            要读取第几行，从1开始
     * @return String 读取的数据
     * @throws IOException
     */
    public static String getCsvDataByIndex(String csvPath, int index) {
        StringBuilder result = new StringBuilder();
        int row = 1;// 行索引
        int column;// 列数目
        try (Reader in = new InputStreamReader(new FileInputStream(csvPath), CHARSET);) {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
            Iterator<CSVRecord> iterator = records.iterator();
            while (iterator.hasNext()) {
                CSVRecord record = iterator.next();// 到下一行索引，默认行索引从1开始
                if (index == row) {
                    column = record.size();
                    for (int i = 0; i < column; i++) {
                        if (i == column - 1) {
                            result.append(record.get(i));
                        } else {
                            result.append(record.get(i) + ",");
                        }
                    }
                    break;
                }
                row++;
            }
        } catch (IOException e) {
            logger.error(error, e);
        }

        return result.toString();
    }

    public static List<String> getCsvDataListByIndex(String csvPath, int index) {
        List<String> csvList = new ArrayList<>();
        int row = 1;// 行索引
        int column;// 列数目
        try (Reader in = new InputStreamReader(new FileInputStream(csvPath), CHARSET);) {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
            Iterator<CSVRecord> iterator = records.iterator();
            while (iterator.hasNext()) {
                CSVRecord record = iterator.next();// 到下一行索引，默认行索引从1开始
                if (index == row) {
                    column = record.size();
                    for (int i = 0; i < column; i++) {
                        csvList.add(record.get(i));
                    }
                    break;
                }
                row++;
            }
        } catch (IOException e) {
            logger.error(error, e);
        }

        return csvList;
    }

    /**
     * 读取全部csv
     * @param csvPath
     * @return
     */
    public static List<List<String>> getCsvDataList(String csvPath) {
        List<List<String>> csvList = new ArrayList<>();
        // 列数目
        int column;
        try (Reader in = new InputStreamReader(new FileInputStream(csvPath), CHARSET);) {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
            Iterator<CSVRecord> iterator = records.iterator();
            while (iterator.hasNext()) {
                // 到下一行索引，默认行索引从1开始
                CSVRecord record = iterator.next();
                column = record.size();
                List<String> recordList = new ArrayList<>();
                for (int i = 0; i < column; i++) {
                    recordList.add(record.get(i));
                }
                csvList.add(recordList);
            }
        } catch (IOException e) {
            logger.error(error, e);
        }

        return csvList;
    }

    /**
     * 
     * @Description: 读取整个csv文件
     * @param filePath
     *            csv路径
     * @return List<String>
     * @throws IOException
     */
    public static List<String> getCsvDataAll(String filePath) {
        List<String> listResult = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        int column;// 列数目
        try (Reader in = new InputStreamReader(new FileInputStream(filePath), CHARSET);) {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
            for (CSVRecord record : records) {
                column = record.size();
                for (int i = 0; i < column; i++) {
                    if (i == column - 1) {
                        result.append(record.get(i));
                    } else {
                        result.append(record.get(i) + ",");
                    }
                }
                listResult.add(result.toString());
                result = new StringBuilder();
            }
        } catch (IOException e) {
            logger.error(error, e);
            return new ArrayList<>();
        }
        return listResult;
    }

    /**
     * 
     * @Description: 写单行csv, 会覆盖之前所有的csv数据
     * @param csvInfo
     *            待写入的数据
     * @param csvFile
     *            csv路径
     * @param csvHead
     *            csv头信息
     * @return boolean
     */
    public static boolean writeCoverCsvData(String csvInfo, String csvFile, String csvHead) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");
        boolean bRet = false;

        File file = new File(csvFile);
        if (file.exists()) {
            try (PrintWriter printWriter = new PrintWriter(new File(csvFile), CHARSET);
                    CSVPrinter csvPrinter = new CSVPrinter(printWriter, csvFormat)) {
                csvPrinterWrite(csvPrinter, csvHead);
                csvPrinterWrite(csvPrinter, csvInfo);
                bRet = true;
            } catch (IOException e) {
                logger.error(error, e);
            }
        } else {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                if (!file.createNewFile()) {
                    return bRet;
                }
            } catch (IOException e1) {
                logger.error(error, e1);
            }
            try (PrintWriter printWriter = new PrintWriter(new File(csvFile), CHARSET);
                    CSVPrinter csvPrinter = new CSVPrinter(printWriter, csvFormat)) {
                csvPrinterWrite(csvPrinter, csvHead);
                csvPrinterWrite(csvPrinter, csvInfo);
                bRet = true;
            } catch (IOException e) {
                logger.error(error, e);
            }
        }
        return true;
    }

    /**
     * 
     * @Description: 写多行csv, 会覆盖之前所有的csv数据
     * @param csvInfo
     *            待写入的数据
     * @param csvFile
     *            csv路径
     * @param csvHead
     *            csv头信息
     * @return boolean
     */
    public static boolean writeCoverCsvDatas(List<String[]> csvInfo, String csvFile,
            String[] csvHead) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");
        boolean bRet = false;

        File file = new File(csvFile);
        if (file.exists()) {
            try (PrintWriter printWriter = new PrintWriter(new File(csvFile), CHARSET);
                    CSVPrinter csvPrinter = new CSVPrinter(printWriter, csvFormat)) {
                csvPrinterWrites(csvPrinter, csvHead);
                csvPrinterWrites(csvPrinter, csvInfo);
                bRet = true;
            } catch (IOException e) {
                logger.error(error, e);
            }
        } else {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                if (!file.createNewFile()) {
                    return bRet;
                }
            } catch (IOException e1) {
                logger.error(error, e1);
            }
            try (PrintWriter printWriter = new PrintWriter(new File(csvFile), CHARSET);
                    CSVPrinter csvPrinter = new CSVPrinter(printWriter, csvFormat)) {
                csvPrinterWrites(csvPrinter, csvHead);
                csvPrinterWrites(csvPrinter, csvInfo);
                bRet = true;
            } catch (IOException e) {
                logger.error(error, e);
            }
        }
        return true;
    }

    private static void csvPrinterWrite(CSVPrinter csvPrinter, String str) throws IOException {
        if (!"".equals(str)) {
            // 去掉换行符,避免切割时写入换行符
            if (str.endsWith("\n")) {
                str = str.replace("\n", "");
            } else if (str.endsWith("\r\n")) {
                str = str.replace("\r\n", "");
            } else if (str.endsWith("\r")) {
                str = str.replace("\r", "");
            }
            Object[] data = str.split(",");
            csvPrinter.printRecord(data);
        }
    }

    private static void csvPrinterWrites(CSVPrinter csvPrinter, Object obj) throws IOException {
        if (obj instanceof String[]) {
            Object[] tmp = (Object[]) obj;
            csvPrinter.printRecord(tmp);
        }
        if (obj instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<Object[]> tmp = (List<Object[]>) obj;
            for (Object[] str : tmp) {
                csvPrinter.printRecord(str);
            }
        }
    }

    /**
     * 写csv 如果csv存在则在后面追加
     * 
     * @param csvInfo
     *            csv数据信息
     * @param csvFile
     *            csv路径（文件名）
     * @param csvHead
     *            csv头信息
     * @return
     */
    public static boolean writeCsvData(String csvInfo, String csvFile, String csvHead) {
        List<String> arrayList = new ArrayList<>();
        arrayList.add(csvInfo);

        return writeCsvDatas(arrayList, csvFile, csvHead);
    }

    /**
     * 写csv 如果csv存在则在后面追加
     * 
     * @param csvInfo
     *            csv数据信息
     * @param csvFile
     *            csv路径（文件名）
     * @param csvHead
     *            csv头信息
     * @return
     */
    public static boolean writeCsvDatas(List<String> csvInfo, String csvFile, String csvHead) {
        boolean bRet = false;
        File file = new File(csvFile);

        if (file.exists()) {
            try (FileOutputStream out = new FileOutputStream(file, true);
                    OutputStreamWriter osw = new OutputStreamWriter(out, CHARSET);
                    BufferedWriter bw = new BufferedWriter(osw)) {
                write(csvInfo, bw);
                bRet = true;
                bw.flush();
            } catch (IOException e) {
                logger.error(error, e);
            }
        } else {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                if (!file.createNewFile()) {
                    return bRet;
                }
            } catch (IOException e1) {
                logger.error(error, e1);
            }
            try (FileOutputStream out = new FileOutputStream(file, true);
                    OutputStreamWriter osw = new OutputStreamWriter(out, CHARSET);
                    BufferedWriter bw = new BufferedWriter(osw)) {
                List<String> arrayList = new ArrayList<>();
                arrayList.add(csvHead);
                write(arrayList, bw);
                write(csvInfo, bw);
                bRet = true;
                bw.flush();
            } catch (IOException e) {
                logger.error(error, e);
            }
        }

        return bRet;
    }

    private static void write(List<String> strList, BufferedWriter bw) throws IOException {
        for (String str : strList) {
            if (!"".equals(str)) {
                bw.append(str);
                if (!(str.endsWith("\n") || str.endsWith("\r\n") || str.endsWith("\r"))) {
                    bw.append("\r\n");
                }
            }
        }

    }
    
    /**
     * 如果有回车换行符需要去掉
     * 如果message中有逗号需要双引号括起来。如果中间有双引号需要用两个双引号替代单个双引号
     * @param message
     * @return
     */
    public static String formateMessage(String message) {
        if (message == null) {
            return "null";
        }
        
        String localMessage = message.replace("\r", "").replace("\n", "");
        
        if (localMessage.indexOf(',') < 0)  {
            return localMessage;
        }
        
        StringBuilder formatMessage = new StringBuilder();
        formatMessage.append('"').append(localMessage.replace("\"", "\"\"")).append('"');
        return formatMessage.toString();
    }

}
