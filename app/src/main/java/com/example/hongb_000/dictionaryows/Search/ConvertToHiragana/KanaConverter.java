package com.example.hongb_000.dictionaryows.Search.ConvertToHiragana;

import java.util.HashMap;

/**
 * Created by hongb_000 on 7/27/2015.
 */
public class KanaConverter {

    private static HashMap<String, String> map;
    private static HashMap<String, String> maps;

    public static String toHiragana(String x) {

        if (map == null) {
            map = new HashMap<>();
            map.put("a", "あ");
            map.put("i", "い");
            map.put("u", "う");
            map.put("e", "え");
            map.put("o", "お");
            map.put("yi", "い");
            map.put("wu", "う");
            map.put("whu", "う");
            map.put("xa", "ぁ");
            map.put("xi", "ぃ");
            map.put("xu", "ぅ");
            map.put("xe", "ぇ");
            map.put("xo", "ぉ");
            map.put("xyi", "ぃ");
            map.put("xye", "ぇ");
            map.put("ye", "いぇ");
            map.put("wha", "うぁ");
            map.put("whi", "うぃ");
            map.put("whe", "うぇ");
            map.put("who", "うぉ");
            map.put("wi", "うぃ");
            map.put("we", "うぇ");
            map.put("va", "ゔぁ");
            map.put("vi", "ゔぃ");
            map.put("vu", "ゔ");
            map.put("ve", "ゔぇ");
            map.put("vo", "ゔぉ");
            map.put("vya", "ゔゃ");
            map.put("vyi", "ゔぃ");
            map.put("vyu", "ゔゅ");
            map.put("vye", "ゔぇ");
            map.put("vyo", "ゔょ");
            map.put("ka", "か");
            map.put("ki", "き");
            map.put("ku", "く");
            map.put("ke", "け");
            map.put("ko", "こ");
            map.put("lka", "ヵ");
            map.put("lke", "ヶ");
            map.put("xka", "ヵ");
            map.put("xke", "ヶ");
            map.put("kya", "きゃ");
            map.put("kyi", "きぃ");
            map.put("kyu", "きゅ");
            map.put("kye", "きぇ");
            map.put("kyo", "きょ");
            map.put("ca", "か");
            map.put("ci", "き");
            map.put("cu", "く");
            map.put("ce", "け");
            map.put("co", "こ");
            map.put("lca", "ヵ");
            map.put("lce", "ヶ");
            map.put("xca", "ヵ");
            map.put("xce", "ヶ");
            map.put("qya", "くゃ");
            map.put("qyu", "くゅ");
            map.put("qyo", "くょ");
            map.put("qwa", "くぁ");
            map.put("qwi", "くぃ");
            map.put("qwu", "くぅ");
            map.put("qwe", "くぇ");
            map.put("qwo", "くぉ");
            map.put("qa", "くぁ");
            map.put("qi", "くぃ");
            map.put("qe", "くぇ");
            map.put("qo", "くぉ");
            map.put("kwa", "くぁ");
            map.put("qyi", "くぃ");
            map.put("qye", "くぇ");
            map.put("ga", "が");
            map.put("gi", "ぎ");
            map.put("gu", "ぐ");
            map.put("ge", "げ");
            map.put("go", "ご");
            map.put("gya", "ぎゃ");
            map.put("gyi", "ぎぃ");
            map.put("gyu", "ぎゅ");
            map.put("gye", "ぎぇ");
            map.put("gyo", "ぎょ");
            map.put("gwa", "ぐぁ");
            map.put("gwi", "ぐぃ");
            map.put("gwu", "ぐぅ");
            map.put("gwe", "ぐぇ");
            map.put("gwo", "ぐぉ");
            map.put("sa", "さ");
            map.put("si", "し");
            map.put("shi", "し");
            map.put("su", "す");
            map.put("se", "せ");
            map.put("so", "そ");
            map.put("za", "ざ");
            map.put("zi", "じ");
            map.put("zu", "ず");
            map.put("ze", "ぜ");
            map.put("zo", "ぞ");
            map.put("ji", "じ");
            map.put("sya", "しゃ");
            map.put("syi", "しぃ");
            map.put("syu", "しゅ");
            map.put("sye", "しぇ");
            map.put("syo", "しょ");
            map.put("sha", "しゃ");
            map.put("shu", "しゅ");
            map.put("she", "しぇ");
            map.put("sho", "しょ");
            map.put("shya", "しゃ");
            map.put("shyu", "しゅ");
            map.put("shye", "しぇ");
            map.put("shyo", "しょ");
            map.put("swa", "すぁ");
            map.put("swi", "すぃ");
            map.put("swu", "すぅ");
            map.put("swe", "すぇ");
            map.put("swo", "すぉ");
            map.put("zya", "じゃ");
            map.put("zyi", "じぃ");
            map.put("zyu", "じゅ");
            map.put("zye", "じぇ");
            map.put("zyo", "じょ");
            map.put("ja", "じゃ");
            map.put("ju", "じゅ");
            map.put("je", "じぇ");
            map.put("jo", "じょ");
            map.put("jya", "じゃ");
            map.put("jyi", "じぃ");
            map.put("jyu", "じゅ");
            map.put("jye", "じぇ");
            map.put("jyo", "じょ");
            map.put("ta", "た");
            map.put("ti", "ち");
            map.put("tu", "つ");
            map.put("te", "て");
            map.put("to", "と");
            map.put("chi", "ち");
            map.put("tsu", "つ");
            map.put("ltu", "っ");
            map.put("xtu", "っ");
            map.put("tya", "ちゃ");
            map.put("tyi", "ちぃ");
            map.put("tyu", "ちゅ");
            map.put("tye", "ちぇ");
            map.put("tyo", "ちょ");
            map.put("cha", "ちゃ");
            map.put("chu", "ちゅ");
            map.put("che", "ちぇ");
            map.put("cho", "ちょ");
            map.put("cya", "ちゃ");
            map.put("cyi", "ちぃ");
            map.put("cyu", "ちゅ");
            map.put("cye", "ちぇ");
            map.put("cyo", "ちょ");
            map.put("chya", "ちゃ");
            map.put("chyu", "ちゅ");
            map.put("chye", "ちぇ");
            map.put("chyo", "ちょ");
            map.put("tsa", "つぁ");
            map.put("tsi", "つぃ");
            map.put("tse", "つぇ");
            map.put("tso", "つぉ");
            map.put("tha", "てゃ");
            map.put("thi", "てぃ");
            map.put("thu", "てゅ");
            map.put("the", "てぇ");
            map.put("tho", "てょ");
            map.put("twa", "とぁ");
            map.put("twi", "とぃ");
            map.put("twu", "とぅ");
            map.put("twe", "とぇ");
            map.put("two", "とぉ");
            map.put("da", "だ");
            map.put("di", "ぢ");
            map.put("du", "づ");
            map.put("de", "で");
            map.put("do", "ど");
            map.put("dya", "ぢゃ");
            map.put("dyi", "ぢぃ");
            map.put("dyu", "ぢゅ");
            map.put("dye", "ぢぇ");
            map.put("dyo", "ぢょ");
            map.put("dha", "でゃ");
            map.put("dhi", "でぃ");
            map.put("dhu", "でゅ");
            map.put("dhe", "でぇ");
            map.put("dho", "でょ");
            map.put("dwa", "どぁ");
            map.put("dwi", "どぃ");
            map.put("dwu", "どぅ");
            map.put("dwe", "どぇ");
            map.put("dwo", "どぉ");
            map.put("na", "な");
            map.put("ni", "に");
            map.put("nu", "ぬ");
            map.put("ne", "ね");
            map.put("no", "の");
            map.put("nya", "にゃ");
            map.put("nyi", "にぃ");
            map.put("nyu", "にゅ");
            map.put("nye", "にぇ");
            map.put("nyo", "にょ");
            map.put("ha", "は");
            map.put("hi", "ひ");
            map.put("hu", "ふ");
            map.put("he", "へ");
            map.put("ho", "ほ");
            map.put("fu", "ふ");
            map.put("hya", "ひゃ");
            map.put("hyi", "ひぃ");
            map.put("hyu", "ひゅ");
            map.put("hye", "ひぇ");
            map.put("hyo", "ひょ");
            map.put("fya", "ふゃ");
            map.put("fyu", "ふゅ");
            map.put("fyo", "ふょ");
            map.put("fwa", "ふぁ");
            map.put("fwi", "ふぃ");
            map.put("fwu", "ふぅ");
            map.put("fwe", "ふぇ");
            map.put("fwo", "ふぉ");
            map.put("fa", "ふぁ");
            map.put("fi", "ふぃ");
            map.put("fe", "ふぇ");
            map.put("fo", "ふぉ");
            map.put("fyi", "ふぃ");
            map.put("fye", "ふぇ");
            map.put("ba", "ば");
            map.put("bi", "び");
            map.put("bu", "ぶ");
            map.put("be", "べ");
            map.put("bo", "ぼ");
            map.put("bya", "びゃ");
            map.put("byi", "びぃ");
            map.put("byu", "びゅ");
            map.put("bye", "びぇ");
            map.put("byo", "びょ");
            map.put("pa", "ぱ");
            map.put("pi", "ぴ");
            map.put("pu", "ぷ");
            map.put("pe", "ぺ");
            map.put("po", "ぽ");
            map.put("pya", "ぴゃ");
            map.put("pyi", "ぴぃ");
            map.put("pyu", "ぴゅ");
            map.put("pye", "ぴぇ");
            map.put("pyo", "ぴょ");
            map.put("ma", "ま");
            map.put("mi", "み");
            map.put("mu", "む");
            map.put("me", "め");
            map.put("mo", "も");
            map.put("mya", "みゃ");
            map.put("myi", "みぃ");
            map.put("myu", "みゅ");
            map.put("mye", "みぇ");
            map.put("myo", "みょ");
            map.put("ya", "や");
            map.put("yu", "ゆ");
            map.put("yo", "よ");
            map.put("xya", "ゃ");
            map.put("xyu", "ゅ");
            map.put("xyo", "ょ");
            map.put("ra", "ら");
            map.put("ri", "り");
            map.put("ru", "る");
            map.put("re", "れ");
            map.put("ro", "ろ");
            map.put("rya", "りゃ");
            map.put("ryi", "りぃ");
            map.put("ryu", "りゅ");
            map.put("rye", "りぇ");
            map.put("ryo", "りょ");
            map.put("la", "ら");
            map.put("li", "り");
            map.put("lu", "る");
            map.put("le", "れ");
            map.put("lo", "ろ");
            map.put("lya", "りゃ");
            map.put("lyi", "りぃ");
            map.put("lyu", "りゅ");
            map.put("lye", "りぇ");
            map.put("lyo", "りょ");
            map.put("wa", "わ");
            map.put("wo", "を");
            map.put("lwe", "ゎ");
            map.put("xwa", "ゎ");
            map.put("n", "ん");
            map.put("n ", "ん");
            map.put("xn", "ん");
            map.put("ltsu", "っ");
        }

        x = x.toLowerCase();
        char[] ch = x.toCharArray();
        String str;
        String output = "";
        for (int i = 0; i < x.length(); i++) {
            str = "" + ch[i];
            if (ch[i] == 'n') {
                if ((i < x.length() - 1) && (ch[i + 1] == 'a' || ch[i + 1] == 'i' || ch[i + 1] == 'u' || ch[i + 1] == 'e' || ch[i + 1] == 'o' || ch[i + 1] == ' ')) {
                    str = str + ch[i + 1];
                    i = i + 1;
                }
                if ((i < x.length() - 2) && (ch[i + 1] == 'y')) {
                    if (ch[i + 2] == 'â' || ch[i + 2] == 'i' || ch[i + 2] == 'u' || ch[i + 2] == 'e' || ch[i + 2] == 'o') {
                        str = str + ch[i + 1] + ch[i + 2];
                        i = i + 2;
                    }
                }
            }
            if (i != x.length() - 1 && isCompound(ch[i], ch[i + 1])) {
                output = output + "っ";
                i++;
            }
            if (map.get(str) != null) {
                output = output + map.get(str);
            } else {
                if (i < x.length() - 1) {
                    str = str + ch[i + 1];
                    if (map.get(str) != null) {
                        output = output + map.get(str);
                        i++;
                    } else {
                        if (i < x.length() - 2) {
                            str = str + ch[i + 2];
                            if (map.get(str) != null) {
                                output = output + map.get(str);
                                i = i + 2;
                            } else {
                                if (i < x.length() - 3) {
                                    str = str + ch[i + 3];
                                    if (map.get(str) != null) {
                                        output = output + map.get(str);
                                        i = i + 3;
                                    } else {
                                        output = output + ch[i];
                                    }
                                } else {
                                    output = output + ch[i];
                                }
                            }
                        } else {
                            output = output + ch[i];
                        }
                    }
                } else {
                    output = output + ch[i];
                }
            }
        }

        return output.trim();
    }

    public static boolean isCompound(char i, char j) {
        String test = "qwrtypsdfghjklzxcvbm";
        return i == j && test.contains("" + i);
    }

    public static String toKatakana(String x) {

        if (maps == null) {
            maps = new HashMap<>();
            maps.put("a", "ア");
            maps.put("i", "イ");
            maps.put("u", "ウ");
            maps.put("e", "エ");
            maps.put("o", "オ");
            maps.put("yi", "イ");
            maps.put("wu", "ウ");
            maps.put("whu", "ウ");
            maps.put("xa", "ァ");
            maps.put("xi", "ィ");
            maps.put("xu", "ゥ");
            maps.put("xe", "ェ");
            maps.put("xo", "ォ");
            maps.put("xyi", "ィ");
            maps.put("xye", "ェ");
            maps.put("ye", "イェ");
            maps.put("wha", "ウァ");
            maps.put("whi", "ウィ");
            maps.put("whe", "ウェ");
            maps.put("who", "ウォ");
            maps.put("wi", "ウィ");
            maps.put("we", "ウェ");
            maps.put("va", "ヴァ");
            maps.put("vi", "ヴィ");
            maps.put("vu", "ヴ");
            maps.put("ve", "ヴェ");
            maps.put("vo", "ヴォ");
            maps.put("vya", "ヴャ");
            maps.put("vyi", "ヴィ");
            maps.put("vyu", "ヴュ");
            maps.put("vye", "ヴェ");
            maps.put("vyo", "ヴョ");
            maps.put("ka", "カ");
            maps.put("ki", "キ");
            maps.put("ku", "ク");
            maps.put("ke", "ケ");
            maps.put("ko", "コ");
            maps.put("lka", "ヵ");
            maps.put("lke", "ヶ");
            maps.put("xka", "ヵ");
            maps.put("xke", "ヶ");
            maps.put("kya", "キャ");
            maps.put("kyi", "キィ");
            maps.put("kyu", "キュ");
            maps.put("kye", "キェ");
            maps.put("kyo", "キョ");
            maps.put("ca", "カ");
            maps.put("ci", "シ");
            maps.put("cu", "ク");
            maps.put("ce", "セ");
            maps.put("co", "コ");
            maps.put("lca", "ｌカ");
            maps.put("lce", "ｌセ");
            maps.put("xca", "ｘカ");
            maps.put("xce", "ｘセ");
            maps.put("qya", "クャ");
            maps.put("qyu", "クュ");
            maps.put("qyo", "クョ");
            maps.put("qwa", "クァ");
            maps.put("qwi", "クィ");
            maps.put("qwu", "クゥ");
            maps.put("qwe", "クェ");
            maps.put("qwo", "クォ");
            maps.put("qa", "クァ");
            maps.put("qi", "クィ");
            maps.put("qe", "クェ");
            maps.put("qo", "クォ");
            maps.put("kwa", "クァ");
            maps.put("qyi", "クィ");
            maps.put("qye", "クェ");
            maps.put("ga", "ガ");
            maps.put("gi", "ギ");
            maps.put("gu", "グ");
            maps.put("ge", "ゲ");
            maps.put("go", "ゴ");
            maps.put("gya", "ギャ");
            maps.put("gyi", "ギィ");
            maps.put("gyu", "ギュ");
            maps.put("gye", "ギェ");
            maps.put("gyo", "ギョ");
            maps.put("gwa", "グァ");
            maps.put("gwi", "グィ");
            maps.put("gwu", "グゥ");
            maps.put("gwe", "グェ");
            maps.put("gwo", "グォ");
            maps.put("sa", "サ");
            maps.put("si", "シ");
            maps.put("shi", "シ");
            maps.put("su", "ス");
            maps.put("se", "セ");
            maps.put("so", "ソ");
            maps.put("za", "ザ");
            maps.put("zi", "ジ");
            maps.put("zu", "ズ");
            maps.put("ze", "ゼ");
            maps.put("zo", "ゾ");
            maps.put("ji", "ジ");
            maps.put("sya", "シャ");
            maps.put("syi", "シィ");
            maps.put("syu", "シュ");
            maps.put("sye", "シェ");
            maps.put("syo", "ショ");
            maps.put("sha", "シャ");
            maps.put("shu", "シュ");
            maps.put("she", "シェ");
            maps.put("sho", "ショ");
            maps.put("shya", "ｓヒャ");
            maps.put("shyu", "ｓヒュ");
            maps.put("shye", "ｓヒェ");
            maps.put("shyo", "ｓヒョ");
            maps.put("swa", "スァ");
            maps.put("swi", "スィ");
            maps.put("swu", "スゥ");
            maps.put("swe", "スェ");
            maps.put("swo", "スォ");
            maps.put("zya", "ジャ");
            maps.put("zyi", "ジィ");
            maps.put("zyu", "ジュ");
            maps.put("zye", "ジェ");
            maps.put("zyo", "ジョ");
            maps.put("ja", "ジャ");
            maps.put("ju", "ジュ");
            maps.put("je", "ジェ");
            maps.put("jo", "ジョ");
            maps.put("jya", "ジャ");
            maps.put("jyi", "ジィ");
            maps.put("jyu", "ジュ");
            maps.put("jye", "ジェ");
            maps.put("jyo", "ジョ");
            maps.put("ta", "タ");
            maps.put("ti", "チ");
            maps.put("tu", "ツ");
            maps.put("te", "テ");
            maps.put("to", "ト");
            maps.put("chi", "チ");
            maps.put("tsu", "ツ");
            maps.put("ltu", "ッ");
            maps.put("xtu", "ッ");
            maps.put("tya", "チャ");
            maps.put("tyi", "チィ");
            maps.put("tyu", "チュ");
            maps.put("tye", "チェ");
            maps.put("tyo", "チョ");
            maps.put("cha", "チャ");
            maps.put("chu", "チュ");
            maps.put("che", "チェ");
            maps.put("cho", "チョ");
            maps.put("cya", "チャ");
            maps.put("cyi", "チィ");
            maps.put("cyu", "チュ");
            maps.put("cye", "チェ");
            maps.put("cyo", "チョ");
            maps.put("chya", "ｃヒャ");
            maps.put("chyu", "ｃヒュ");
            maps.put("chye", "ｃヒェ");
            maps.put("chyo", "ｃヒョ");
            maps.put("tsa", "ツァ");
            maps.put("tsi", "ツィ");
            maps.put("tse", "ツェ");
            maps.put("tso", "ツォ");
            maps.put("tha", "テャ");
            maps.put("thi", "ティ");
            maps.put("thu", "テュ");
            maps.put("the", "テェ");
            maps.put("tho", "テョ");
            maps.put("twa", "トァ");
            maps.put("twi", "トィ");
            maps.put("twu", "トゥ");
            maps.put("twe", "トェ");
            maps.put("two", "トォ");
            maps.put("da", "ダ");
            maps.put("di", "ヂ");
            maps.put("du", "ヅ");
            maps.put("de", "デ");
            maps.put("do", "ド");
            maps.put("dya", "ヂャ");
            maps.put("dyi", "ヂィ");
            maps.put("dyu", "ヂュ");
            maps.put("dye", "ヂェ");
            maps.put("dyo", "ヂョ");
            maps.put("dha", "デャ");
            maps.put("dhi", "ディ");
            maps.put("dhu", "デュ");
            maps.put("dhe", "デェ");
            maps.put("dho", "デョ");
            maps.put("dwa", "ドァ");
            maps.put("dwi", "ドィ");
            maps.put("dwu", "ドゥ");
            maps.put("dwe", "ドェ");
            maps.put("dwo", "ドォ");
            maps.put("na", "ナ");
            maps.put("ni", "ニ");
            maps.put("nu", "ヌ");
            maps.put("ne", "ネ");
            maps.put("no", "ノ");
            maps.put("nya", "ニャ");
            maps.put("nyi", "ニィ");
            maps.put("nyu", "ニュ");
            maps.put("nye", "ニェ");
            maps.put("nyo", "ニョ");
            maps.put("ha", "ハ");
            maps.put("hi", "ヒ");
            maps.put("hu", "フ");
            maps.put("he", "ヘ");
            maps.put("ho", "ホ");
            maps.put("fu", "フ");
            maps.put("hya", "ヒャ");
            maps.put("hyi", "ヒィ");
            maps.put("hyu", "ヒュ");
            maps.put("hye", "ヒェ");
            maps.put("hyo", "ヒョ");
            maps.put("fya", "フャ");
            maps.put("fyu", "フュ");
            maps.put("fyo", "フョ");
            maps.put("fwa", "ファ");
            maps.put("fwi", "フィ");
            maps.put("fwu", "フゥ");
            maps.put("fwe", "フェ");
            maps.put("fwo", "フォ");
            maps.put("fa", "ファ");
            maps.put("fi", "フィ");
            maps.put("fe", "フェ");
            maps.put("fo", "フォ");
            maps.put("fyi", "フィ");
            maps.put("fye", "フェ");
            maps.put("ba", "バ");
            maps.put("bi", "ビ");
            maps.put("bu", "ブ");
            maps.put("be", "ベ");
            maps.put("bo", "ボ");
            maps.put("bya", "ビャ");
            maps.put("byi", "ビィ");
            maps.put("byu", "ビュ");
            maps.put("bye", "ビェ");
            maps.put("byo", "ビョ");
            maps.put("pa", "パ");
            maps.put("pi", "ピ");
            maps.put("pu", "プ");
            maps.put("pe", "ペ");
            maps.put("po", "ポ");
            maps.put("pya", "ピャ");
            maps.put("pyi", "ピィ");
            maps.put("pyu", "ピュ");
            maps.put("pye", "ピェ");
            maps.put("pyo", "ピョ");
            maps.put("ma", "マ");
            maps.put("mi", "ミ");
            maps.put("mu", "ム");
            maps.put("me", "メ");
            maps.put("mo", "モ");
            maps.put("mya", "ミャ");
            maps.put("myi", "ミィ");
            maps.put("myu", "ミュ");
            maps.put("mye", "ミェ");
            maps.put("myo", "ミョ");
            maps.put("ya", "ヤ");
            maps.put("yu", "ユ");
            maps.put("yo", "ヨ");
            maps.put("xya", "ャ");
            maps.put("xyu", "ュ");
            maps.put("xyo", "ョ");
            maps.put("ra", "ラ");
            maps.put("ri", "リ");
            maps.put("ru", "ル");
            maps.put("re", "レ");
            maps.put("ro", "ロ");
            maps.put("rya", "リャ");
            maps.put("ryi", "リィ");
            maps.put("ryu", "リュ");
            maps.put("rye", "リェ");
            maps.put("ryo", "リョ");
            maps.put("la", "ラ");
            maps.put("li", "リ");
            maps.put("lu", "ル");
            maps.put("le", "レ");
            maps.put("lo", "ロ");
            maps.put("lya", "リャ");
            maps.put("lyi", "リィ");
            maps.put("lyu", "リュ");
            maps.put("lye", "リェ");
            maps.put("lyo", "リョ");
            maps.put("wa", "ワ");
            maps.put("wo", "ヲ");
            maps.put("lwe", "ｌウェ");
            maps.put("xwa", "ヮ");
            maps.put("n", "ン");
            maps.put("n ", "ン");
            maps.put("xn", "ン");
            maps.put("ltsu", "ッ");
        }

        x = x.toLowerCase();
        char[] ch = x.toCharArray();
        String str;
        String output = "";
        for (int i = 0; i < x.length(); i++) {
            str = "" + ch[i];
            if (ch[i] == 'n') {
                if ((i < x.length() - 1) && (ch[i + 1] == 'a' || ch[i + 1] == 'i' || ch[i + 1] == 'u' || ch[i + 1] == 'e' || ch[i + 1] == 'o' || ch[i + 1] == ' ')) {
                    str = str + ch[i + 1];
                    i = i + 1;
                }
                if ((i < x.length() - 2) && (ch[i + 1] == 'y')) {
                    if (ch[i + 2] == 'â' || ch[i + 2] == 'i' || ch[i + 2] == 'u' || ch[i + 2] == 'e' || ch[i + 2] == 'o') {
                        str = str + ch[i + 1] + ch[i + 2];
                        i = i + 2;
                    }
                }
            }
            if (i != x.length() - 1 && isCompound(ch[i], ch[i + 1])) {
                output = output + "ッ";
                i++;
            }
            if (maps.get(str) != null) {
                output = output + maps.get(str);
            } else {
                if (i < x.length() - 1) {
                    str = str + ch[i + 1];
                    if (maps.get(str) != null) {
                        output = output + maps.get(str);
                        i++;
                    } else {
                        if (i < x.length() - 2) {
                            str = str + ch[i + 2];
                            if (maps.get(str) != null) {
                                output = output + maps.get(str);
                                i = i + 2;
                            } else {
                                if (i < x.length() - 3) {
                                    str = str + ch[i + 3];
                                    if (maps.get(str) != null) {
                                        output = output + maps.get(str);
                                        i = i + 3;
                                    } else {
                                        output = output + ch[i];
                                    }
                                } else {
                                    output = output + ch[i];
                                }
                            }
                        } else {
                            output = output + ch[i];
                        }
                    }
                } else {
                    output = output + ch[i];
                }
            }
        }

        return output.trim();
    }

}
