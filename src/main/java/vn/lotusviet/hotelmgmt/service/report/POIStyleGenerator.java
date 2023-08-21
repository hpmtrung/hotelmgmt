package vn.lotusviet.hotelmgmt.service.report;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class POIStyleGenerator {

  private static final String DEFAULT_FONT_NAME = "Calibri";

  private CellStyle createAlignedStyle(
      Workbook wb, Font font, HorizontalAlignment alignment, boolean hasBorder) {
    final CellStyle style;
    if (hasBorder) {
      style = createBorderedStyle(wb);
    } else {
      style = wb.createCellStyle();
    }
    style.setAlignment(alignment);
    style.setFont(font);
    return style;
  }

  private CellStyle createRightAlignedCurrencyStyle(Workbook wb, Font font, boolean hasBorder) {
    final CellStyle style;
    if (hasBorder) {
      style = createBorderedStyle(wb);
    } else {
      style = wb.createCellStyle();
    }
    style.setFont(font);
    style.setDataFormat((short) 8);
    return style;
  }

  private CellStyle createBorderedStyle(Workbook wb) {
    var thin = BorderStyle.THIN;
    var black = IndexedColors.BLACK.getIndex();
    var style = wb.createCellStyle();
    // right
    style.setBorderRight(thin);
    style.setRightBorderColor(black);
    // bottom
    style.setBorderBottom(thin);
    style.setBottomBorderColor(black);
    // left
    style.setBorderLeft(thin);
    style.setLeftBorderColor(black);
    // top
    style.setBorderTop(thin);
    style.setTopBorderColor(black);
    return style;
  }

  private CellStyle createBlueCenteredWithBorderStyle(Workbook wb, Font font) {
    var style = createBorderedStyle(wb);
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setFont(font);
    style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    return style;
  }

  private CellStyle createDateFormatStyle(Workbook wb, HorizontalAlignment alignment) {
    var style = wb.createCellStyle();
    style.setAlignment(alignment);
    style.setDataFormat((short) 14);
    return style;
  }

  private Font createDefaultFont(Workbook wb, boolean bold, IndexedColors color, short size) {
    var font = wb.createFont();
    font.setFontName(DEFAULT_FONT_NAME);
    font.setBold(bold);
    font.setColor(color.index);
    font.setFontHeightInPoints(size);
    return font;
  }

  public Map<CustomCellStyle, CellStyle> prepareStyles(Workbook wb) {
    final Font normalFont = createDefaultFont(wb, false, IndexedColors.BLACK, (short) 13);
    final Font boldNormalFont = createDefaultFont(wb, true, IndexedColors.BLACK, (short) 13);
    final Font sheetTitleFont = createDefaultFont(wb, true, IndexedColors.WHITE, (short) 18);
    final Font headerTableFont = createDefaultFont(wb, true, IndexedColors.WHITE, (short) 13);
    final EnumMap<CustomCellStyle, CellStyle> map = new EnumMap<>(CustomCellStyle.class);
    map.put(
        CustomCellStyle.RIGHT_ALIGNED,
        createAlignedStyle(wb, normalFont, HorizontalAlignment.RIGHT, false));
    map.put(
        CustomCellStyle.LEFT_ALIGNED,
        createAlignedStyle(wb, normalFont, HorizontalAlignment.LEFT, false));
    map.put(
        CustomCellStyle.CENTER_ALIGNED,
        createAlignedStyle(wb, normalFont, HorizontalAlignment.CENTER, false));
    //
    map.put(
        CustomCellStyle.RIGHT_ALIGNED_WITH_BORDER,
        createAlignedStyle(wb, normalFont, HorizontalAlignment.RIGHT, true));
    map.put(
        CustomCellStyle.LEFT_ALIGNED_WITH_BORDER,
        createAlignedStyle(wb, normalFont, HorizontalAlignment.LEFT, true));
    map.put(
        CustomCellStyle.CENTER_ALIGNED_WITH_BORDER,
        createAlignedStyle(wb, normalFont, HorizontalAlignment.CENTER, true));
    //
    map.put(
        CustomCellStyle.RIGHT_BOLD_ALIGNED,
        createAlignedStyle(wb, normalFont, HorizontalAlignment.RIGHT, false));
    map.put(
        CustomCellStyle.LEFT_BOLD_ALIGNED,
        createAlignedStyle(wb, boldNormalFont, HorizontalAlignment.LEFT, false));
    map.put(
        CustomCellStyle.CENTER_BOLD_ALIGNED,
        createAlignedStyle(wb, boldNormalFont, HorizontalAlignment.CENTER, false));
    map.put(CustomCellStyle.MAIN_TITLE, createBlueCenteredWithBorderStyle(wb, sheetTitleFont));
    map.put(CustomCellStyle.HEADER_TABLE, createBlueCenteredWithBorderStyle(wb, headerTableFont));
    map.put(
        CustomCellStyle.RIGHT_ALIGNED_DATE_FORMAT,
        createDateFormatStyle(wb, HorizontalAlignment.RIGHT));
    map.put(
        CustomCellStyle.LEFT_ALIGNED_DATE_FORMAT,
        createDateFormatStyle(wb, HorizontalAlignment.LEFT));
    map.put(
        CustomCellStyle.RIGHT_ALIGNED_CURRECY_FORMAT,
        createRightAlignedCurrencyStyle(wb, normalFont, false));
    map.put(
        CustomCellStyle.RIGHT_ALIGNED_CURRECY_FORMAT_WITH_BORDER,
        createRightAlignedCurrencyStyle(wb, normalFont, true));
    return map;
  }

  public enum CustomCellStyle {
    RIGHT_ALIGNED,
    LEFT_ALIGNED,
    CENTER_ALIGNED,
    //
    RIGHT_BOLD_ALIGNED,
    LEFT_BOLD_ALIGNED,
    CENTER_BOLD_ALIGNED,
    //
    RIGHT_ALIGNED_WITH_BORDER,
    LEFT_ALIGNED_WITH_BORDER,
    CENTER_ALIGNED_WITH_BORDER,
    //
    MAIN_TITLE,
    HEADER_TABLE,
    //
    RIGHT_ALIGNED_DATE_FORMAT,
    LEFT_ALIGNED_DATE_FORMAT,
    //
    RIGHT_ALIGNED_CURRECY_FORMAT,
    RIGHT_ALIGNED_CURRECY_FORMAT_WITH_BORDER
  }
}