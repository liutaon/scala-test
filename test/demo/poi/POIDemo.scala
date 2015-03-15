package demo.poi

import java.io.FileOutputStream
import java.io.OutputStream
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import org.apache.poi.ss.usermodel.CellStyle
import java.util.Calendar
import java.util.Date
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.util.CellRangeAddress

object POIDemo extends App {

  def out(file: String)(f: OutputStream => Unit) = {
    val out = new FileOutputStream(new File("target", file))
    f(out)
    out.close()
  }

  def simple() = out("simple.xls") { out =>
    val wb = new HSSFWorkbook()
    wb.write(out)
  }

  def simpleSheet() = out("simple-sheet.xls") { out =>
    val wb = new HSSFWorkbook()
    val sheet1 = wb.createSheet("营业收据")
    val sheet2 = wb.createSheet("营业单据")
    wb.write(out)
  }

  def simpleCell() = out("cell.xls") { out =>
    val wb = new HSSFWorkbook();
    val createHelper = wb.getCreationHelper();
    val sheet = wb.createSheet("new sheet");

    // Create a row and put some cells in it. Rows are 0 based.
    val row = sheet.createRow(0);
    // Create a cell and put a value in it.
    val cell = row.createCell(0);
    cell.setCellValue(1);

    // Or do it on one line.
    row.createCell(1).setCellValue(1.2);
    row.createCell(2).setCellValue(
      createHelper.createRichTextString("This is a string"));
    row.createCell(3).setCellValue(true);
    wb.write(out);
  }

  def dateCell() = out("date-cell.xls") { out =>
    val wb = new HSSFWorkbook();
    //Workbook wb = new XSSFWorkbook();
    val createHelper = wb.getCreationHelper();
    val sheet = wb.createSheet("new sheet");

    // Create a row and put some cells in it. Rows are 0 based.
    val row = sheet.createRow(0);

    // Create a cell and put a date value in it.  The first cell is not styled
    // as a date.
    var cell = row.createCell(0);
    cell.setCellValue(new Date());

    // we style the second cell as a date (and time).  It is important to
    // create a new cell style from the workbook otherwise you can end up
    // modifying the built in style and effecting not only this cell but other cells.
    val cellStyle = wb.createCellStyle();
    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
    cell = row.createCell(1);
    cell.setCellValue(new Date());
    cell.setCellStyle(cellStyle);

    //you can also set date as java.util.Calendar
    cell = row.createCell(2);
    cell.setCellValue(Calendar.getInstance());
    cell.setCellStyle(cellStyle);

    wb.write(out);

  }

  def typeCell() = out("type-cell.xls") { out =>
    val wb = new HSSFWorkbook();
    val sheet = wb.createSheet("new sheet");
    val row = sheet.createRow(2);
    row.createCell(0).setCellValue(1.1);
    row.createCell(1).setCellValue(new Date());
    row.createCell(2).setCellValue(Calendar.getInstance());
    row.createCell(3).setCellValue("a string");
    row.createCell(4).setCellValue(true);
    row.createCell(5).setCellType(Cell.CELL_TYPE_ERROR);
    wb.write(out);
  }

  def align() = out("align.xls") { out =>
    val wb = new HSSFWorkbook(); //or new HSSFWorkbook();

    val sheet = wb.createSheet();
    val row = sheet.createRow(2);
    row.setHeightInPoints(30);

    createCell(wb, row, 0, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_BOTTOM);
    createCell(wb, row, 1, CellStyle.ALIGN_CENTER_SELECTION, CellStyle.VERTICAL_BOTTOM);
    createCell(wb, row, 2, CellStyle.ALIGN_FILL, CellStyle.VERTICAL_CENTER);
    createCell(wb, row, 3, CellStyle.ALIGN_GENERAL, CellStyle.VERTICAL_CENTER);
    createCell(wb, row, 4, CellStyle.ALIGN_JUSTIFY, CellStyle.VERTICAL_JUSTIFY);
    createCell(wb, row, 5, CellStyle.ALIGN_LEFT, CellStyle.VERTICAL_TOP);
    createCell(wb, row, 6, CellStyle.ALIGN_RIGHT, CellStyle.VERTICAL_TOP);
    wb.write(out);
  }

  private def createCell(wb: Workbook, row: Row, column: Short, halign: Short, valign: Short) = {
    val cell = row.createCell(column);
    cell.setCellValue("Align It");
    val cellStyle = wb.createCellStyle();
    cellStyle.setAlignment(halign);
    cellStyle.setVerticalAlignment(valign);
    cell.setCellStyle(cellStyle);
  }

  def border() = out("border.xls") { out =>
    val wb = new HSSFWorkbook();
    val sheet = wb.createSheet("new sheet");

    // Create a row and put some cells in it. Rows are 0 based.
    val row = sheet.createRow(1);

    // Create a cell and put a value in it.
    val cell = row.createCell(1);
    cell.setCellValue(4);

    // Style the cell with borders all around.
    val style = wb.createCellStyle();
    style.setBorderBottom(CellStyle.BORDER_THIN);
    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderLeft(CellStyle.BORDER_THIN);
    style.setLeftBorderColor(IndexedColors.GREEN.getIndex());
    style.setBorderRight(CellStyle.BORDER_THIN);
    style.setRightBorderColor(IndexedColors.BLUE.getIndex());
    style.setBorderTop(CellStyle.BORDER_MEDIUM_DASHED);
    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
    cell.setCellStyle(style);
    wb.write(out);
  }

  def color() = out("color.xls") { out =>
    val wb = new HSSFWorkbook();
    val sheet = wb.createSheet("new sheet");

    // Create a row and put some cells in it. Rows are 0 based.
    val row = sheet.createRow(1);

    // Aqua background
    var style = wb.createCellStyle();
    style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
    style.setFillPattern(CellStyle.BIG_SPOTS);
    var cell = row.createCell(1);
    cell.setCellValue("X");
    cell.setCellStyle(style);

    // Orange "foreground", foreground being the fill foreground not the font color.
    style = wb.createCellStyle();
    style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
    cell = row.createCell(2);
    cell.setCellValue("X");
    cell.setCellStyle(style);

    wb.write(out);
  }

  def merge() = out("merge.xls") { fileOut =>
    val wb = new HSSFWorkbook();
    val sheet = wb.createSheet("new sheet");

    val row = sheet.createRow(1);
    val cell = row.createCell(1);
    cell.setCellValue("This is a test of merging");

    sheet.addMergedRegion(new CellRangeAddress(
      1, //first row (0-based)
      1, //last row  (0-based)
      1, //first column (0-based)
      2 //last column  (0-based)
      ));

    wb.write(fileOut);
  }

  def font() = out("font.xls") { fileOut =>
    val wb = new HSSFWorkbook();
    val sheet = wb.createSheet("new sheet");

    // Create a row and put some cells in it. Rows are 0 based.
    val row = sheet.createRow(1);

    // Create a new font and alter it.
    val font = wb.createFont();
    font.setFontHeightInPoints(24);
    font.setFontName("Courier New");
    font.setItalic(true);
    font.setStrikeout(true);

    // Fonts are set into a style so create a new one to use.
    val style = wb.createCellStyle();
    style.setFont(font);

    // Create a cell and put a value in it.
    val cell = row.createCell(1);
    cell.setCellValue("This is a test of fonts");
    cell.setCellStyle(style);

    wb.write(fileOut);
  }

  def repeat() = out("repeat.xls") { fileOut =>
    val wb = new HSSFWorkbook(); // or new XSSFWorkbook();
    val sheet1 = wb.createSheet("Sheet1");
    val sheet2 = wb.createSheet("Sheet2");

    // Set the rows to repeat from row 4 to 5 on the first sheet.
    sheet1.setRepeatingRows(CellRangeAddress.valueOf("4:5"));
    // Set the columns to repeat from column A to C on the second sheet
    sheet2.setRepeatingColumns(CellRangeAddress.valueOf("A:C"));

    wb.write(fileOut);
  }

  def header() = out("header.xls") { fileOut =>
    val wb = new HSSFWorkbook();
    val sheet = wb.createSheet("new sheet");

    val header = sheet.getHeader();
    header.setCenter("Center Header");
    header.setLeft("Left Header");
    //    header.setRight(HSSFHeader.font("Stencil-Normal", "Italic") + HSSFHeader.fontSize(16) + "Right w/ Stencil-Normal Italic font and size 16");
    header.setRight("Right Header")

    wb.write(fileOut);
  }

  simple()
  simpleSheet()
  simpleCell()
  dateCell()
  typeCell()
  align()
  border()
  color()
  merge()
  font()
  repeat()
  header()
}
