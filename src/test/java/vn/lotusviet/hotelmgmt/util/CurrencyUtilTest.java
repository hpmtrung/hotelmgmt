package vn.lotusviet.hotelmgmt.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static vn.lotusviet.hotelmgmt.util.CurrencyUtil.convertToReadableText;

class CurrencyUtilTest {

  @Test
  void whenConvertToReadableText_ThenResultIsCorrect() {
    assertThat(convertToReadableText(0)).isEqualTo("Không đồng");
    assertThat(convertToReadableText(1)).isEqualTo("Một đồng");
    assertThat(convertToReadableText(2)).isEqualTo("Hai đồng");
    assertThat(convertToReadableText(5)).isEqualTo("Năm đồng");
    //
    assertThat(convertToReadableText(10)).isEqualTo("Mười đồng");
    assertThat(convertToReadableText(11)).isEqualTo("Mười một đồng");
    assertThat(convertToReadableText(12)).isEqualTo("Mười hai đồng");
    assertThat(convertToReadableText(15)).isEqualTo("Mười lăm đồng");
    assertThat(convertToReadableText(20)).isEqualTo("Hai mươi đồng");
    assertThat(convertToReadableText(21)).isEqualTo("Hai mươi mốt đồng");
    assertThat(convertToReadableText(22)).isEqualTo("Hai mươi hai đồng");
    assertThat(convertToReadableText(25)).isEqualTo("Hai mươi lăm đồng");
    assertThat(convertToReadableText(50)).isEqualTo("Năm mươi đồng");
    assertThat(convertToReadableText(55)).isEqualTo("Năm mươi lăm đồng");
    //
    assertThat(convertToReadableText(100)).isEqualTo("Một trăm đồng");
    assertThat(convertToReadableText(101)).isEqualTo("Một trăm lẻ một đồng");
    assertThat(convertToReadableText(102)).isEqualTo("Một trăm lẻ hai đồng");
    assertThat(convertToReadableText(105)).isEqualTo("Một trăm lẻ năm đồng");
    assertThat(convertToReadableText(110)).isEqualTo("Một trăm mười đồng");
    assertThat(convertToReadableText(111)).isEqualTo("Một trăm mười một đồng");
    assertThat(convertToReadableText(112)).isEqualTo("Một trăm mười hai đồng");
    assertThat(convertToReadableText(115)).isEqualTo("Một trăm mười lăm đồng");
    assertThat(convertToReadableText(120)).isEqualTo("Một trăm hai mươi đồng");
    assertThat(convertToReadableText(121)).isEqualTo("Một trăm hai mươi mốt đồng");
    assertThat(convertToReadableText(125)).isEqualTo("Một trăm hai mươi lăm đồng");
    assertThat(convertToReadableText(130)).isEqualTo("Một trăm ba mươi đồng");
    assertThat(convertToReadableText(150)).isEqualTo("Một trăm năm mươi đồng");
    assertThat(convertToReadableText(155)).isEqualTo("Một trăm năm mươi lăm đồng");
    assertThat(convertToReadableText(200)).isEqualTo("Hai trăm đồng");
    assertThat(convertToReadableText(201)).isEqualTo("Hai trăm lẻ một đồng");
    assertThat(convertToReadableText(202)).isEqualTo("Hai trăm lẻ hai đồng");
    assertThat(convertToReadableText(205)).isEqualTo("Hai trăm lẻ năm đồng");
    assertThat(convertToReadableText(210)).isEqualTo("Hai trăm mười đồng");
    assertThat(convertToReadableText(211)).isEqualTo("Hai trăm mười một đồng");
    assertThat(convertToReadableText(212)).isEqualTo("Hai trăm mười hai đồng");
    assertThat(convertToReadableText(215)).isEqualTo("Hai trăm mười lăm đồng");
    assertThat(convertToReadableText(220)).isEqualTo("Hai trăm hai mươi đồng");
    assertThat(convertToReadableText(221)).isEqualTo("Hai trăm hai mươi mốt đồng");
    assertThat(convertToReadableText(222)).isEqualTo("Hai trăm hai mươi hai đồng");
    assertThat(convertToReadableText(225)).isEqualTo("Hai trăm hai mươi lăm đồng");
    assertThat(convertToReadableText(250)).isEqualTo("Hai trăm năm mươi đồng");
    assertThat(convertToReadableText(251)).isEqualTo("Hai trăm năm mươi mốt đồng");
    assertThat(convertToReadableText(252)).isEqualTo("Hai trăm năm mươi hai đồng");
    assertThat(convertToReadableText(255)).isEqualTo("Hai trăm năm mươi lăm đồng");
    //
    assertThat(convertToReadableText(500)).isEqualTo("Năm trăm đồng");
    assertThat(convertToReadableText(1000)).isEqualTo("Một nghìn đồng");
    assertThat(convertToReadableText(1500)).isEqualTo("Một nghìn năm trăm đồng");
    assertThat(convertToReadableText(2000)).isEqualTo("Hai nghìn đồng");
    assertThat(convertToReadableText(5000)).isEqualTo("Năm nghìn đồng");
    //
    assertThat(convertToReadableText(10000)).isEqualTo("Mười nghìn đồng");
    assertThat(convertToReadableText(11000)).isEqualTo("Mười một nghìn đồng");
    assertThat(convertToReadableText(12000)).isEqualTo("Mười hai nghìn đồng");
    assertThat(convertToReadableText(15000)).isEqualTo("Mười lăm nghìn đồng");
    assertThat(convertToReadableText(21000)).isEqualTo("Hai mươi mốt nghìn đồng");
    assertThat(convertToReadableText(22000)).isEqualTo("Hai mươi hai nghìn đồng");
    assertThat(convertToReadableText(25000)).isEqualTo("Hai mươi lăm nghìn đồng");
    assertThat(convertToReadableText(50000)).isEqualTo("Năm mươi nghìn đồng");
    //
    assertThat(convertToReadableText(100000)).isEqualTo("Một trăm nghìn đồng");
    assertThat(convertToReadableText(101000)).isEqualTo("Một trăm lẻ một nghìn đồng");
    assertThat(convertToReadableText(102000)).isEqualTo("Một trăm lẻ hai nghìn đồng");
    assertThat(convertToReadableText(105000)).isEqualTo("Một trăm lẻ năm nghìn đồng");
    assertThat(convertToReadableText(110000)).isEqualTo("Một trăm mười nghìn đồng");
    assertThat(convertToReadableText(111000)).isEqualTo("Một trăm mười một nghìn đồng");
    assertThat(convertToReadableText(112000)).isEqualTo("Một trăm mười hai nghìn đồng");
    assertThat(convertToReadableText(115000)).isEqualTo("Một trăm mười lăm nghìn đồng");
    assertThat(convertToReadableText(120000)).isEqualTo("Một trăm hai mươi nghìn đồng");
    assertThat(convertToReadableText(121000)).isEqualTo("Một trăm hai mươi mốt nghìn đồng");
    assertThat(convertToReadableText(122000)).isEqualTo("Một trăm hai mươi hai nghìn đồng");
    assertThat(convertToReadableText(125000)).isEqualTo("Một trăm hai mươi lăm nghìn đồng");
    assertThat(convertToReadableText(150000)).isEqualTo("Một trăm năm mươi nghìn đồng");
    assertThat(convertToReadableText(151000)).isEqualTo("Một trăm năm mươi mốt nghìn đồng");
    assertThat(convertToReadableText(152000)).isEqualTo("Một trăm năm mươi hai nghìn đồng");
    assertThat(convertToReadableText(155000)).isEqualTo("Một trăm năm mươi lăm nghìn đồng");
    assertThat(convertToReadableText(201000)).isEqualTo("Hai trăm lẻ một nghìn đồng");
    assertThat(convertToReadableText(202000)).isEqualTo("Hai trăm lẻ hai nghìn đồng");
    assertThat(convertToReadableText(205000)).isEqualTo("Hai trăm lẻ năm nghìn đồng");
    assertThat(convertToReadableText(210000)).isEqualTo("Hai trăm mười nghìn đồng");
    assertThat(convertToReadableText(211000)).isEqualTo("Hai trăm mười một nghìn đồng");
    assertThat(convertToReadableText(212000)).isEqualTo("Hai trăm mười hai nghìn đồng");
    assertThat(convertToReadableText(215000)).isEqualTo("Hai trăm mười lăm nghìn đồng");
    assertThat(convertToReadableText(220000)).isEqualTo("Hai trăm hai mươi nghìn đồng");
    assertThat(convertToReadableText(221000)).isEqualTo("Hai trăm hai mươi mốt nghìn đồng");
    assertThat(convertToReadableText(222000)).isEqualTo("Hai trăm hai mươi hai nghìn đồng");
    assertThat(convertToReadableText(225000)).isEqualTo("Hai trăm hai mươi lăm nghìn đồng");
    assertThat(convertToReadableText(500000)).isEqualTo("Năm trăm nghìn đồng");
    //
    assertThat(convertToReadableText(1001000)).isEqualTo("Một triệu lẻ một nghìn đồng");
    assertThat(convertToReadableText(1002000)).isEqualTo("Một triệu lẻ hai nghìn đồng");
    assertThat(convertToReadableText(1005000)).isEqualTo("Một triệu lẻ năm nghìn đồng");
    assertThat(convertToReadableText(1011000)).isEqualTo("Một triệu không trăm mười một nghìn đồng");
    assertThat(convertToReadableText(1012000)).isEqualTo("Một triệu không trăm mười hai nghìn đồng");
    assertThat(convertToReadableText(1015000)).isEqualTo("Một triệu không trăm mười lăm nghìn đồng");
    assertThat(convertToReadableText(1020000)).isEqualTo("Một triệu không trăm hai mươi nghìn đồng");
    assertThat(convertToReadableText(1021000)).isEqualTo("Một triệu không trăm hai mươi mốt nghìn đồng");
    assertThat(convertToReadableText(1022000)).isEqualTo("Một triệu không trăm hai mươi hai nghìn đồng");
    assertThat(convertToReadableText(1025000)).isEqualTo("Một triệu không trăm hai mươi lăm nghìn đồng");
    assertThat(convertToReadableText(1050000)).isEqualTo("Một triệu không trăm năm mươi nghìn đồng");
    assertThat(convertToReadableText(1100000)).isEqualTo("Một triệu một trăm nghìn đồng");
    assertThat(convertToReadableText(1200000)).isEqualTo("Một triệu hai trăm nghìn đồng");
    assertThat(convertToReadableText(1500000)).isEqualTo("Một triệu năm trăm nghìn đồng");
    assertThat(convertToReadableText(2000000)).isEqualTo("Hai triệu đồng");
    assertThat(convertToReadableText(5000000)).isEqualTo("Năm triệu đồng");
    //
    assertThat(convertToReadableText(10000000)).isEqualTo("Mười triệu đồng");
    assertThat(convertToReadableText(10001000)).isEqualTo("Mười triệu lẻ một nghìn đồng");
    assertThat(convertToReadableText(10002000)).isEqualTo("Mười triệu lẻ hai nghìn đồng");
    assertThat(convertToReadableText(10005000)).isEqualTo("Mười triệu lẻ năm nghìn đồng");
    assertThat(convertToReadableText(10010000)).isEqualTo("Mười triệu không trăm mười nghìn đồng");
    assertThat(convertToReadableText(10011000)).isEqualTo("Mười triệu không trăm mười một nghìn đồng");
    assertThat(convertToReadableText(10012000)).isEqualTo("Mười triệu không trăm mười hai nghìn đồng");
    assertThat(convertToReadableText(10015000)).isEqualTo("Mười triệu không trăm mười lăm nghìn đồng");
    assertThat(convertToReadableText(10020000)).isEqualTo("Mười triệu không trăm hai mươi nghìn đồng");
    assertThat(convertToReadableText(10021000)).isEqualTo("Mười triệu không trăm hai mươi mốt nghìn đồng");
    assertThat(convertToReadableText(10022000)).isEqualTo("Mười triệu không trăm hai mươi hai nghìn đồng");
    assertThat(convertToReadableText(10025000)).isEqualTo("Mười triệu không trăm hai mươi lăm nghìn đồng");
    assertThat(convertToReadableText(10100000)).isEqualTo("Mười triệu một trăm nghìn đồng");
    assertThat(convertToReadableText(10101000)).isEqualTo("Mười triệu một trăm lẻ một nghìn đồng");
    assertThat(convertToReadableText(10110000)).isEqualTo("Mười triệu một trăm mười nghìn đồng");
    assertThat(convertToReadableText(10111000)).isEqualTo("Mười triệu một trăm mười một nghìn đồng");
    assertThat(convertToReadableText(10121000)).isEqualTo("Mười triệu một trăm hai mươi mốt nghìn đồng");
    assertThat(convertToReadableText(11000000)).isEqualTo("Mười một triệu đồng");
    assertThat(convertToReadableText(20000000)).isEqualTo("Hai mươi triệu đồng");
    assertThat(convertToReadableText(50000000)).isEqualTo("Năm mươi triệu đồng");


  }
}