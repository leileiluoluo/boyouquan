package com.boyouquan.enumeration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum BlogEnums {

    LEILEILUOLUO("leileiluoluo@leileiluoluo.com", "https://leileiluoluo.com/index.xml"),
    XIAOWULEYI("0471666@gmail.com", "https://www.xiaowuleyi.com/feed"),
    WEIYEXING("weiyexing1@gmail.com", "https://aaronnick.github.io/rss.xml"),
    TIANXIANZI("mimismeoww@gmail.com", "https://tianxianzi.me/atom.xml"),
    MACIN("hi@macin.org", "https://www.macin.org/atom.xml"),
    RUSHIHU("notfound@rushihu.com", "http://rushihu.com/feed"),
    ATPX("hello@atpx.com", "https://atpx.com/feed.xml"),
    TYPEMYLIFE("zhang@typemylife.com", "https://www.typemylife.com/feed/"),
    OWLSWIMS("heyday.misses_0l@icloud.com", "https://owlswims.com/feed/"),
    SI_ON("me@si-on.top", "https://blog.si-on.top/atom.xml"),
    QNCD("376977443@qq.com", "http://www.qncd.com/?feed=rss2"),
    CHANGHAI("lu_changhai@yahoo.com", "https://www.changhai.org/feed.xml"),
    MMBKZ("notfound@mmbkz.cn", "https://www.mmbkz.cn/feed/"),
    MUXER("i@lms.im", "https://muxer.cn/feed"),
    STEPHENLENG("lengge86@gmail.com", "https://stephenleng.com/feed/"),
    FENG("mypen@163.com", "https://feng.pub/feed"),
    SUBNOOC("nooc@nooc.ink", "https://subnooc.com/rss.xml"),
    JINGFENGSHUO("i@jingfengshuo.com", "https://www.jingfengshuo.com/feed"),
    ORDCHAOS("orderchaos@ordchaos.com", "https://www.ordchaos.com/atom.xml"),
    LILLIANWHO("ciwiehenfan@sina.com", "https://lillianwho.com/atom.xml"),
    ZUOFEI("huhexian0206gmail.com", "https://zuofei.net/feed"),
    ONOJYUN("notfound@notfound.com", "https://onojyun.com/feed/"),
    SKYUE("skyue.hu@gmail.com", "https://www.skyue.com/feed/"),
    JUSTGOIDEA("notfound@notfound.com", "https://www.justgoidea.com/feed"),
    OWENYOUNG("owen@owenyoung.com", "https://www.owenyoung.com/atom.xml"),
    SOGOLA("high@sogola.com", "https://sogola.com/index.xml"),
    OURAI("ourairyu@gmail.com", "https://ourai.ws/rss.xml"),
    SUIYAN("285911@gmail.com", "https://suiyan.cc/rss.xml"),
    CATCODING("notfound@notfound.com", "https://catcoding.me/atom.xml"),
    HANYU("hi@hanyu.life", "https://hanyu.life/blog/index.xml"),
    TIANHEG("me@tianhegao.com", "https://tianheg.xyz/atom.xml"),
    YESHU("xdlrt0111@163.com", "https://yeshu.cloud/rss.xml"),
    ELIZEN("notfound@notfound.com", "https://elizen.me/index.xml"),
    YIPAI("notfound@notfound.com", "https://yipai.me/feed"),
    KAIX("dimlau@kaix.in", "https://kaix.in/subscribe/"),
    SHIYU("im@shiyu.dev", "https://shiyu.dev/feed/"),
    IMMMMM("notfound@notfound.com", "https://immmmm.com/atom.xml"),
    ANOTHERDAYU("notfound@notfound.com", "https://anotherdayu.com/feed/"),
    LAOZHANG("notfound@notfound.com", "https://laozhang.org/feed/"),
    TORTORSE("hello@tortorse.com", "https://www.tortorse.com/atom.xml"),
    IZZNAN("2461306899@qq.com", "https://blog.izznan.cn/feed"),
    BKRYOFU("unscientific_kappa@outlook.com", "https://bkryofu.xyz/atom.xml"),
    HIN("892599923@qq.com", "https://hin.cool/atom.xml"),
    IXIQIN("bestony@linux.com", "https://www.ixiqin.com/feed/"),
    LYCHEE("notfound@notfound.com", "https://lychee.love/feed/"),
    JAVIS("notfound@notfound.com", "https://www.javis.me/feed"),
    SUITHINK("suithink.su@gmail.com", "https://suithink.me/feed/"),
    ZHHEO("notfound@notfound.com", "https://blog.zhheo.com/rss.xml"),
    NUOEA("notfound@notfound.com", "https://nuoea.com/atom.xml"),
    OLDJ("hi@oldj.net", "https://oldj.net/feed/"),
    EASYF12("notfound@notfound.com", "https://easyf12.top/atom.xml"),
    LUOLEI("i@luolei.org", "https://luolei.org/feed/"),
    PYTHONCAT("chinesehuazhou@gmail.com", "https://pythoncat.top/rss.xml");

    private final String email;
    private final String feedAddress;

    BlogEnums(String email, String feedAddress) {
        this.email = email;
        this.feedAddress = feedAddress;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFeedAddress() {
        return this.feedAddress;
    }

    public static List<String> getAllFeedAddresses() {
        return Arrays.stream(BlogEnums.values()).map(BlogEnums::getFeedAddress).collect(Collectors.toList());
    }

    public static String getEmailByBlogAddress(String blogAddress) {
        for (BlogEnums e : BlogEnums.values()) {
            if (e.getFeedAddress().contains(blogAddress)) {
                return e.getEmail();
            }
        }
        return "notfound@notfound.com";
    }

}
