package vn.lotusviet.hotelmgmt.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CurrencyUtil {

  private static final String KHONG = "không";
  private static final String MOT = "một";
  private static final String HAI = "hai";
  private static final String BA = "ba";
  private static final String BON = "bốn";
  private static final String NAM = "năm";
  private static final String SAU = "sáu";
  private static final String BAY = "bảy";
  private static final String TAM = "tám";
  private static final String CHIN = "chín";
  private static final String LAM = "lăm";
  private static final String LE = "lẻ";
  private static final String MUOI = "mươi";
  private static final String MUOIF = "mười";
  private static final String MOTS = "mốt";
  private static final String TRAM = "trăm";
  private static final String NGHIN = "nghìn";
  private static final String TRIEU = "triệu";

  private static final String[] NUMBER_TEXT = {KHONG, MOT, HAI, BA, BON, NAM, SAU, BAY, TAM, CHIN};

  private static final int CHUNK_SIZE = 3;

  private CurrencyUtil() {
    throw new AssertionError();
  }

  public static <T extends Number> String convertToReadableText(T money) {
    final String str = String.valueOf(money);
    return convertToReadableText(str);
  }

  private static String convertToReadableText(String str) {

    if (!containsOnlyDigit(str)) {
      throw new IllegalArgumentException("Money should be parsable");
    }

    List<String> words = new ArrayList<>();
    ArrayList<String> chunks = splitChunks(str);

    while (!chunks.isEmpty()) {
      switch (chunks.size() % 3) {
        case 1:
          words.addAll(parseChunk(chunks.get(0)));
          break;
        case 2:
          List<String> thousandChunk = parseChunk(chunks.get(0));
          if (!thousandChunk.isEmpty()) {
            words.addAll(thousandChunk);
            words.add(NGHIN);
          }
          break;
        case 0:
          List<String> millionChunk = parseChunk(chunks.get(0));
          if (!millionChunk.isEmpty()) {
            words.addAll(millionChunk);
            words.add(TRIEU);
          }
          break;
      }

      chunks.remove(0);
    }

    if (words.isEmpty()) {
      words.add(KHONG);
    }

    final StringBuilder sb = new StringBuilder();
    words.forEach(word -> sb.append(word).append(" "));
    final String result = sb.toString();
    return result.substring(0, 1).toUpperCase() + result.substring(1) + "đồng";
  }

  private static List<String> parseChunk(String chunk) {
    final ArrayList<String> result = new ArrayList<>();

    int hundred = parseNumber(chunk, 0, 1);
    int dozen = parseNumber(chunk, 1, 2);
    int unit = parseNumber(chunk, 2, 3);

    if (hundred > 0 || (hundred == 0 && dozen > 0)) {
      result.add(NUMBER_TEXT[hundred]);
      result.add(TRAM);
    }

    switch (dozen) {
      case -1:
        break;
      case 1:
        result.add(MUOIF);
        break;
      case 0:
        if (unit != 0) {
          result.add(LE);
        }
        break;
      default:
        result.add(NUMBER_TEXT[dozen]);
        result.add(MUOI);
        break;
    }

    switch (unit) {
      case -1:
      case 0:
        break;
      case 1:
        if ((dozen != 0) && (dozen != 1) && (dozen != -1)) {
          result.add(MOTS);
        } else {
          result.add(MOT);
        }
        break;
      case 5:
        if ((dozen != 0) && (dozen != -1)) {
          result.add(LAM);
        } else {
          result.add(NAM);
        }
        break;
      default:
        result.add(NUMBER_TEXT[unit]);
        break;
    }

    return result;
  }

  private static ArrayList<String> splitChunks(String str) {
    if (str.isEmpty()) return new ArrayList<>(Collections.emptyList());

    for (int remaining = CHUNK_SIZE - str.length() % CHUNK_SIZE; remaining > 0; remaining--) {
      str = "#" + str;
    }

    int chunkNum = (int) Math.ceil(str.length() / (double) CHUNK_SIZE);
    final ArrayList<String> result = new ArrayList<>();
    int start = 0;
    for (int i = 0; i < chunkNum - 1; i++) {
      result.add(str.substring(start, start + CHUNK_SIZE));
      start += CHUNK_SIZE;
    }
    result.add(str.substring(start));

    return result;
  }

  private static int parseNumber(String str, int startIndex, int endIndex) {
    try {
      return Integer.parseInt(str.substring(startIndex, endIndex));
    } catch (Exception ex) {
      return -1;
    }
  }

  private static boolean containsOnlyDigit(String str) {
    for (char ch : str.toCharArray()) {
      if (!Character.isDigit(ch)) {
        return false;
      }
    }
    return true;
  }
}