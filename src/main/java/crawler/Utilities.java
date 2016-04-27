package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Ruixiang on 4/19/2016.
 */
public final class Utilities {

    private Utilities() {
    }

    public static boolean isVIPChapter(Element chapterLink) {
        return chapterLink.attr("href").contains("vip") || chapterLink.hasAttr("rel");
    }

    public static String sanitizeBingLink(String url) {
        return url.replaceFirst("https", "http");
    }

    public static String sanitizeChapterTitle(String title) {
        return title.replaceAll("【.*】|（.*）", "");
    }

    public static String sanitizeChapterContent(String unSafeContent) {
        return Jsoup.clean(unSafeContent, Whitelist.basic());
    }

    public static boolean isOriginalSite(Element eChapter) {
        return eChapter.text().contains("qq.com") || eChapter.text().contains("qidian.com") || eChapter.text().contains("zongheng") || eChapter.text().contains("baidu");
    }

    public static String getCssSelectorBySite(String urlString) {
        String domain = getDomainByURL(urlString);
        switch (domain) {
            //case "dijiuzww.com":
            //    return "#content";
            case "quanxiong.org":
                return "#content";
            case "sqsxs.com":
                return "#BookText";
            case "yunlaige.com":
                return "#content";
            case "kukukanshu.cc":
                return "#BookText";
            case "dashubao.cc":
                return "#content";
            case "fenghuaju.com":
                return "#content";
            case "shenmaxiaoshuo.com":
                return "#htmlContent";
            case "baishuzhai.com":
                return "#TXT";
            case "4xiaoshuo.com":
                return "#booktext";
            case "x69zw.com":
                return "div[class=\"yd_text2\"]";
            case "quledu.com":
                return "#htmlContent";
            case "3zm.net":
                return "#content";
            case "555zw.com":
                return "#content";
            case "d5wx.com":
                return "#content";
            case "zhuishu.com":
                return "#booktext";
            case "piaotian.cc":
                return "div[class=\"novel_content\"]";
            case "yssm.org":
                return "#content";
            case "uukanshu.com":
                return "#contentbox";
            case "luoqiu.com":
                return "#content";
            case "qingkan520.com":
                return "#content";
            case "23xsw.cc":
                return "#contents";
            case "prwx.com":
                return "#content";
            case "2kxs.com":
                return "p[class=\"Text\"]";
            case "lanseshuba.com":
                return "#contents";
            case "wo400.com":
                return "#content";
            case "aiquxs.com":
                return "#booktext";
            case "biquge.la":
                return "#content";
            case "silukee.com":
                return "div[class=\"cont\"]";
            case "wxguan.com":
                return "#content";
            case "fqxsw.com":
                return "#content";
            case "kanshu.la":
                return "#contentTxt";
            case "kanshuge.com":
                return "#BookText";
            case "00ksw.com":
                return "#content";
            case "snwx.com":
                return "#BookText";
            case "aszw520.com":
                return "#contents";
            case "dhzw.com":
                return "#BookText";
            case "23wx.com":
                return "#contents";
            case "zhuzhudao.com":
                return "div[class=\"content\"]";
            case "baoliny.com":
                return "#content";
            case "xiaoshuoku.com":
                return "#text_area";
            case "shushu.com.cn":
                return "#content";
            case "wanantxt.com":
                return "div[class=\"novel_content\"]";
            case "shumilou.co":
                return "#content";
            default:
                return null;
        }
    }

    public static String getDomainByURL(String urlString) {
        if (!urlString.startsWith("http") && !urlString.startsWith("https")) {
            urlString = "http://" + urlString;
        }
        URI uri = null;
        try {
            uri = new URI(urlString);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri.getHost().startsWith("www.") ? uri.getHost().substring(4) : uri.getHost();
    }
}
