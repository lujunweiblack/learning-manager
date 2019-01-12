package web.file.common;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.ResourceUtils;
import web.file.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ExportExcelUtil {

    public static void exportExcel(String excelName, List<?> exportDate, HttpServletResponse response) throws Exception {//根据指定的excel模板导出数据

        String srcFilePath = "classpath:template/userManagerTemplates.xlsx";
        FileInputStream fis = new FileInputStream(ResourceUtils.getFile(srcFilePath));//创建Excel文件的输入流对象
        XSSFWorkbook workBook = new XSSFWorkbook(fis);//根据模板创建excel工作簿

        FileOutputStream fos = new FileOutputStream("D:"+System.currentTimeMillis() + ".xlsx"); //创建Excel文件输出流对象
        XSSFSheet sheet = workBook.getSheetAt(0);//获取创建的工作簿第一页
        sheet.autoSizeColumn(1, true);

        workBook.setSheetName(0, "管理平台人员数据");//给指定的sheet命名

        int newRowIndex = sheet.getLastRowNum();  //最后行数 //获取当前sheet最后一行数据对应的行索引
        int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells(); //总列数

        CellStyle startStyle = getStartStyle(workBook);//第0个单元格样式
        CellStyle dateStartStyle = getDataStartStyle(workBook);//后面的每个单元格样式

        for (int i = 0; i < exportDate.size(); i++) {
            XSSFRow newRow = sheet.createRow(newRowIndex + i);
            newRow.setHeight((short) (22.5 * 22.5)); //设置行高
            List<Object> coloumList = getObjValue(exportDate.get(i)); //反射得出对象值的List
            List<XSSFCell> xssfCells = new ArrayList<>();
            for (int s = 0; s < coloumNum; s++) { //根据列数创建单元格对象
                xssfCells.add(newRow.createCell(s, CellType.STRING));
            }
            if (i == 0) {
                XSSFCell title = newRow.createCell(i == 0 ? 0 : 1, CellType.STRING);
                title.setCellValue("此行开始写入数据");
                title.setCellStyle(startStyle);
            }
            for (int j = 1; j < xssfCells.size(); j++) {//创建每一个单元格，设置其内的数据格式为字符串，并填充内容，其余单元格类同
                xssfCells.get(j).setCellValue(coloumList.get(j - 1) != null ? coloumList.get(j - 1).toString() : "");//设置对应的列数据
                xssfCells.get(j).setCellStyle(dateStartStyle);
            }
        }
        workBook.write(fos); //下载到指定路径
        fis.close();
        fos.flush();
        fos.close();
//        response.setContentType("APPLICATION/OCTET-STREAM");
//        response.setHeader("Content-disposition", "attachment;filename="+new String(excelName.getBytes("gbk"), "iso8859-1")+".xls");
//        response.setCharacterEncoding("utf-8");
//        OutputStream out = response.getOutputStream();
//        workBook.write(out);
        System.out.println("=======导出成功=====");
    }

    public static XSSFFont getFontStyle(XSSFWorkbook workBook) {
        XSSFFont font = workBook.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeight(12);
        return font;
    }

    public static CellStyle getStartStyle(XSSFWorkbook workBook) {
        CellStyle style = workBook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREEN.getIndex());//第一个单元格的背景色
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);//设置底边框;
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());//设置底边框颜色;
        style.setBorderLeft(BorderStyle.THIN);//设置左边框;
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());//设置左边框颜色;
        style.setBorderRight(BorderStyle.THIN);//设置右边框;
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());//设置右边框颜色;
        style.setBorderTop(BorderStyle.THIN);//;设置顶边框
        style.setTopBorderColor(IndexedColors.BLACK.getIndex()); //设置顶边框颜色;
        style.setFont(getFontStyle(workBook));
        return style;
    }

    public static CellStyle getDataStartStyle(XSSFWorkbook workBook) {
        CellStyle style = workBook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);//设置底边框;
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());//设置底边框颜色;
        style.setBorderLeft(BorderStyle.THIN); //设置左边框;
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex()); //设置左边框颜色;
        style.setBorderRight(BorderStyle.THIN);//设置右边框;
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());//设置右边框颜色;
        style.setBorderTop(BorderStyle.THIN); //;设置顶边框
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());//设置顶边框颜色;
        style.setFont(getFontStyle(workBook));
        return style;
    }

    public static List<Object> getObjValue(Object obj) {
        List<Object> list = new ArrayList<>();
        Class userCla = obj.getClass();// 得到类对象
        Field[] fs = userCla.getDeclaredFields();//得到类中的所有属性集合
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true); // 设置些属性是可以访问的
            Object val = new Object();
            try {
                val = f.get(obj);// 得到此属性的值
                list.add(val);// 设置键值
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static void main(String[] args) throws Exception {
        List<User> exportDate = new ArrayList<>();

        for (int i =0;i<10;i++){
            User user = new User();
            user.setUuid("xhc456fec");
            user.setPersonName("李建");
            user.setLinkId("frhv546");
            user.setGoId("frhv546");
            user.setInterId("frhv546");
            user.setGroupIdLink("26");
            user.setGroupIdGo("464");
            user.setGroupIdInter("436");
            user.setCreatTime("2015-02-30 12:12:12");
            user.setUpdateTime("2015-02-30 12:12:12");
            user.setAvatar("http//dsfasf");
            user.setBirthday("1995-02-02");
            user.setIdType("身份证");
            user.setIdNumber("341224911658");
            user.setSex("男");
            user.setVehicle("火车");
            exportDate.add(user);
        }

        exportExcel("管理平台人员数据" + String.valueOf(System.currentTimeMillis()).substring(4, 13), exportDate, null);
        System.exit(0);
    }

}
