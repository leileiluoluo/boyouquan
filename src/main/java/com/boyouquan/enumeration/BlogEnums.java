package com.boyouquan.enumeration;

import com.boyouquan.util.CommonUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum BlogEnums {

    LEILEILUOLUO("leileiluoluo@leileiluoluo.com", "https://leileiluoluo.com/index.xml", "记录日常工作中整理的技术知识和日常生活中的点滴感想。"),
    XIAOWULEYI("0471666@gmail.com", "https://www.xiaowuleyi.com/feed", "记录生活，记录人生感悟。"),
    WEIYEXING("weiyexing1@gmail.com", "https://aaronnick.github.io/rss.xml", "记录所思所想，所见所闻。因此，博客的第一读者首先是自己，然后是其他对此感兴趣的人。"),
    TIANXIANZI("mimismeoww@gmail.com", "https://tianxianzi.me/atom.xml", "这是我的个人主页，你可以叫我秦海碗。本站站名『天仙子』(Song of Hyoscyamus)，一作药引，一作词牌。"),
    MACIN("hi@macin.org", "https://www.macin.org/atom.xml", "诸位少侠，在下是曾让江湖人闻风丧胆的筷子小手。退隐以后，我开始关注活色生香的生活。酝酿与众不同的视角，企图让更多人了解腥风血雨以外的江湖，从此沦为吃喝玩乐的顽徒。"),
    RUSHIHU("notfound@rushihu.com", "http://rushihu.com/feed", "原本想要安心写博客，坚持了七八年，在今年的三月份将纬八路售出，申请了如是乎，重新备案。"),
    ATPX("hello@atpx.com", "https://atpx.com/feed.xml", "关于的话，向来是不想写的，一来没人看，二来是三两句也说不清，硬要写的话，大概是今天天气不错。"),
    TYPEMYLIFE("zhang@typemylife.com", "https://www.typemylife.com/feed/", "产品经理（偏中后台，目前带团队为主，曾经历过支付、物流/货运、在线教育、家政O2O等行业），连续经历行业动荡、公司倒闭后，目前在一家小公司苟着。"),
    OWLSWIMS("heyday.misses_0l@icloud.com", "https://owlswims.com/feed/", "A Touch and yet not a touch."),
    SI_ON("me@si-on.top", "https://blog.si-on.top/atom.xml", "阁主姓田，名欣洋，无字，自号子虚栈主。『尔雅』曰：欣，乐也；洋，多也，因之此名可解作欢乐之多者，亦可释为好多者。"),
    QNCD("376977443@qq.com", "http://www.qncd.com/?feed=rss2", "没有什么特别的头衔，也没被归为某一类别，除了不劳而获的姓名、性别和年龄，还真是一片空白。我所知的，是我无限的无知。"),
    CHANGHAI("lu_changhai@yahoo.com", "https://www.changhai.org/feed.xml", "爱好：看书、 看电影、 听音乐、 听相声、 散步、 远足。濒临灭绝的爱好：中国象棋 (好久没下了)、 乒乓球 (好久没打了)、 游泳 (好久没游了)"),
    MMBKZ("notfound@mmbkz.cn", "https://www.mmbkz.cn/feed/", "火喵是个好名字，既简单又可爱，当然也很好记就是有些绕口，所以来访单朋友称呼我『大帅比』就可以啦~本喵不会在意的啦~"),
    MUXER("i@lms.im", "https://muxer.cn/feed", "83 年的老大叔。博客还存在除了自娱自乐，就是放了这么多年的那点舍不得了。"),
    STEPHENLENG("lengge86@gmail.com", "https://stephenleng.com/feed/", "我叫冷金乘，中文网名是浮云笑此生。浙江大学博士研究生，主攻思想史、情绪史、历史哲学、人文主义哲学和心理学。现在也是弗吉尼亚大学访问博士生/访问学者。"),
    FENG("mypen@163.com", "https://feng.pub/feed", "你好，欢迎来到关于页面倍感荣幸。可以叫我阿锋，1988年冬月的龙，出生在七八线小城市河南·义马。"),
    SUBNOOC("nooc@nooc.ink", "https://subnooc.com/rss.xml", "这里是主观世界。 这是一个由 Nooc 打理的博客。 不定期更新，主要分享作者的所思所想、读书笔记、日常牢骚，但也可能会出现其他任何内容。"),
    JINGFENGSHUO("i@jingfengshuo.com", "https://www.jingfengshuo.com/feed", "静风说，是一个生活类博客，记录日常生活和所思所想，包括读过的书、看过的电影、去过的地方、学到的知识，有时也会关注互联网、软件、WordPress。"),
    ORDCHAOS("orderchaos@ordchaos.com", "https://www.ordchaos.com/atom.xml", "如你所见，正在备战中考，我会继续努力的！"),
    LILLIANWHO("ciwiehenfan@sina.com", "https://lillianwho.com/atom.xml", "我是莉莉蒙（Lillian who），大家叫我莉莉。这是博主的个人博客，分享喜欢的软件、在读的好书，生活记录，以及思考。可以说是我的线上 commonplace book。"),
    ZUOFEI("huhexian0206gmail.com", "https://zuofei.net/feed", "02年，大三在读学生，主修人力资源管理专业。崇尚朴素生活，高尚思考。"),
    ONOJYUN("notfound@notfound.com", "https://onojyun.com/feed/", "博客取名莫比乌斯 / MOBIUS，是取自只有一个平面略带哲学意味的「莫比乌斯带」。写作也好、人生也好，兜兜转转也像是一个莫比乌斯带。"),
    SKYUE("skyue.hu@gmail.com", "https://www.skyue.com/feed/", "我叫「拾月」，英文 ID「SKYue」，在互联网行业做产品类工作。日常关注互联网、软件应用居多，也喜欢股票投资、社科历史，执着于掌控自己的数据，self-host 爱好者。"),
    JUSTGOIDEA("notfound@notfound.com", "https://www.justgoidea.com/feed", "槿呈Goidea，读书｜新知｜生活禅。"),
    OWENYOUNG("owen@owenyoung.com", "https://www.owenyoung.com/atom.xml", "我比较喜欢用明文纯文本记录东西，这是最通用的格式，不会被运营商锁定，在所有平台都能开箱即用的直接编辑。"),
    SOGOLA("high@sogola.com", "https://sogola.com/index.xml", "這個站點是王小嗨的個人部落格，由Hugo生成。"),
    OURAI("ourairyu@gmail.com", "https://ourai.ws/rss.xml", "本站的内容主要是生活、思想和技术类，并按照我的「多元性」将文章等信息做了聚合。"),
    SUIYAN("285911@gmail.com", "https://suiyan.cc/rss.xml", "碎言博客的名字，取自于碎言片语。很早就建立过很多杂七杂八的网站，具体页面底下有详细的博客更新日志，关于suiyan.cc是今年初新注册的域名，但是写博客是始于2017年。"),
    CATCODING("notfound@notfound.com", "https://catcoding.me/atom.xml", "Yukang，热爱技术，兴趣广泛，并长期参与开源。 在持续为 Rust compiler 做贡献，Member of Compiler team contributors。 写作是我的乐趣之一，写作 12 年，我的经验和技巧。"),
    HANYU("hi@hanyu.life", "https://hanyu.life/blog/index.xml", "我的职业是一名后端研发工程师，在北京工作，主要使用Java/Go, 毕业已经10年，大中小公司都经历过，同时也做过团队Leader、业务负责人。"),
    TIANHEG("me@tianhegao.com", "https://tianheg.xyz/atom.xml", "我先是一个人，然后是一个想认识自己的人，最后是一个对科技（科学与技艺）好奇的人。"),
    YESHU("xdlrt0111@163.com", "https://yeshu.cloud/rss.xml", "我是小树，前端工程师，在做协同表格。公众号「一颗小树」作者。"),
    ELIZEN("notfound@notfound.com", "https://elizen.me/index.xml", "There is only one thing in the world worse than being talked about, and that is not being talked about."),
    YIPAI("notfound@notfound.com", "https://yipai.me/feed", "胡一派的个人博客。"),
    KAIX("dimlau@kaix.in", "https://kaix.in/subscribe/", "写作作为一种思考。"),
    SHIYU("im@shiyu.dev", "https://shiyu.dev/feed/", "一个学生，一个科技爱好者，一个喜欢追求最新技术的人。"),
    IMMMMM("notfound@notfound.com", "https://immmmm.com/atom.xml", "不问明天，悠然浪费～"),
    ANOTHERDAYU("notfound@notfound.com", "https://anotherdayu.com/feed/", "Dayu（大宇）是我的小名，也是我的英文名。 网络上的我是碎片化的，有多少个平台，就有多少个我。"),
    LAOZHANG("notfound@notfound.com", "https://laozhang.org/feed/", "老张，80年出生的纯爷们，小学教师，爱好电脑与网络。各种东西都会点，但是都不精通。是技术方面的“杂家”而非专家。"),
    TORTORSE("hello@tortorse.com", "https://www.tortorse.com/atom.xml", "网名「愆伏」(音 qiān fú)，另有网名 tortorse，二十世纪八零后生人。"),
    IZZNAN("2461306899@qq.com", "https://blog.izznan.cn/feed", "愿小仙女能被这世界温柔以待。"),
    BKRYOFU("unscientific_kappa@outlook.com", "https://bkryofu.xyz/atom.xml", "绝望之于虚妄，正与希望相同。"),
    HIN("admin@hin.cool", "https://hin.cool/atom.xml", "国家一级退堂鼓表演者。有过真真切切的温暖，和无比热爱这个世界的渴望。"),
    IXIQIN("bestony@linux.com", "https://www.ixiqin.com/feed/", "Hi，你好，我是白宦成(@bestony)，欢迎来到我的博客。这里记录了我在过去的一段时间里的所思、所想、所学、所用，分享我的各项研究给你。"),
    LYCHEE("notfound@notfound.com", "https://lychee.love/feed/", "全职家里蹲，吃喝睡，遛刺猬。"),
    JAVIS("notfound@notfound.com", "https://www.javis.me/feed", "ENFP-T，爱好读书观影、自由搏击、偶尔弹吉他。本博客用来记录日常、分享体验，经验与思考。"),
    SUITHINK("suithink.su@gmail.com", "https://suithink.me/feed/", "hi，我是苏志斌，一个家里有动物园的设计师奶爸，从事工业设计及相关咨询，同时也是一名写作者和视频作者。"),
    ZHHEO("notfound@notfound.com", "https://blog.zhheo.com/rss.xml", "你好，很高兴认识你。我叫张洪Heo，是一名设计师、产品经理、独立开发者、博主。"),
    NUOEA("notfound@notfound.com", "https://nuoea.com/atom.xml", "归臧，来自湖北武汉。UI & UX Design，Blender & Eevee，前端开发 & 小程序开发，Unity3D & Technical Art，不折腾不舒服司机。"),
    OLDJ("hi@oldj.net", "https://oldj.net/feed/", "主要专长于 Web 端技术，也对用户体验、算法、数据分析、AI 等领域有着浓厚的兴趣。爱编程，也爱涂鸦；爱生活，也爱折腾。"),
    EASYF12("notfound@notfound.com", "https://easyf12.top/atom.xml", "竹杖芒鞋轻胜马，谁怕？一蓑烟雨任平生。这里是一蓑烟雨的博客。我是一蓑烟雨。建立这个小站，我的初衷是发表自己的文章，留下我的声音。"),
    LUOLEI("i@luolei.org", "https://luolei.org/feed/", "我叫罗磊，来自中国深圳，程序员，说不定你还使用过我参与开发的产品。"),
    PYTHONCAT("chinesehuazhou@gmail.com", "https://pythoncat.top/rss.xml", "这个博客里大部分的文章都是豌豆花下猫创作的， 主题集中在 Python、编程思想、技术翻译、技术杂谈。"),
    WATCH_LIFE("iamxjb@gmail.com", "https://www.watch-life.net/feed", "70后中年大叔，写程序，读书，写字，跑步，关注每一天的生活和成长。"),
    TUMUTANZI("contact@tumutanzi.com", "https://tumutanzi.com/feed", "博主从事领域为土木工程行业，钻研经年累月。未获高深学问，略懂营造法式皮毛。综合博主姓氏与所从事专业名称，自造名号：“土木坛子”，以此立于网络虚拟空间。"),
    BIGWHITE("bigwhite.cn@gmail.com", "https://tonybai.com/feed/", "Go程序员，C程序员，技术架构师，技术总监，技术讲师/培训师，技术撰稿人。先后供职于国内某大型软件公司和某创业型数据与基础设施服务公司。"),
    CODINGNOW("cloudwu@gmail.com", "https://blog.codingnow.com/atom.xml", "思绪来得快去得也快，偶尔会在这里停留。"),
    FLYSNOW("ls8707@gmail.com", "https://www.flysnow.org/index.xml", "专注于Android、Java、Go语言(golang)、移动互联网、项目管理、软件架构。"),
    IEEVEE("hubottle@gmail.com", "https://ieevee.com/feed.xml", "已然三十而立，希望四十不惑。"),
    ONEVCAT("onev@onevcat.com", "https://onevcat.com/feed.xml", "上善若水，人淡如菊。这里是王巍 (onevcat) 的博客，用来记录一些技术和想法，主要专注于 Swift 和 iOS 开发。"),
    DRAVENESS("i@draveness.me", "https://draveness.me/feed.xml", "面向信仰编程，《Go 语言设计与实现》作者。"),
    KQH("me@kqh.me", "https://kqh.me/index.xml", "本站是柯棋瀚的微型門戶網站，他希望將本站打造成文史學子共同維護的小苗圃。我們誠邀作者（並不太誠）！"),
    WUSE("me@wuse.ink", "https://wuse.ink/feed.php", "对，我就是城南牧野，也可以叫我老曹或是大宇。但我还是喜欢城南牧野这个名字，虽然听起来有点像日本名。"),
    MAGUANG("ma@maguang.net", "https://www.maguang.net/feed", "马光，暂无字无号。嗜读书，爱骑行与跑步，喜爱宁静大自然。不吸烟，偶小酌，乐于箪食瓢饮。"),
    YILANJU("me@yilanju.com", "https://www.yilanju.com/feed/", "这个网站虽小也简陋，却也满载着我的希望。这是我休憩之地，云乐之土，也是我灵魂的小屋，这里充满我的欢笑，也收容我的哀伤。"),
    ROWLING("ling.rowling.luo@gmail.com", "https://rowl1ng.com/feed.xml", "PhD Student in SketchX Lab, CVSSP, University of Surrey."),
    XIATIAN("me@xiatian.name", "https://www.xiatian.name/feed", "某科研院，博士后。主要研究方向为，镍基粉末高温合金、先进粉末冶金技术、超细结构金属基纳米复合材料等。"),
    SHANGLEI("eflyingxp@gmail.com", "https://shanglei.net/feed/", "历史文献专业硕士，曾在济南从事出版编辑相关工作，现从事公考培训相关工作，关注出版、历史、文化、博客、互联网等领域，希望通过这个博客积累和分享生活和工作中的点滴发现，结识更多朋友，互通有无。"),
    LIUQIRAN("liuqiran@protonmail.com", "https://www.liuqiran.com/zh/feed/", "Writing is supposed to be an essential part of life."),
    KOSTYA("hello@kostya.club", "https://kostya.club/feed.xml", "西兰花的第不知道多少个站点，写得下去写不下去也不知道，反正就先试试看。"),
    WANGYURUI("feng.pupu@qq.com", "https://wangyurui.com/feed.xml", "95后，男喜读书，RSS支持者，“Logseq+Notion+Heptabase”三神器护法喜欢独立思考，喜欢“孤独”，对新事物抱有强烈的好奇心和探索欲，什么都会点但又都不够精通的“杂人”。"),
    HZ_CLUB("1078503@gmail.com", "https://blog.9hz.club/feed", "顾名思义，我就是一个喜欢逛的人，说不定什么时候就逛到你家了哟。主站作为博客以后可能就分享优秀的博文加上自己的理解，算是发散思维吧！偶尔写一点自己的随笔对某些事某些物说三道四一下！"),
    BXAW("amaru@bxaw.name", "https://blog.bxaw.name/feed/", "十多年ACG圈，半个兽圈，没有兽设，只有名字。梦想是成为绘画大触，然而只是做白日梦。喜欢自己研究医学、物理学、哲学、神学，喜欢探讨法律。");

    private final String email;
    private final String feedAddress;
    private final String description;

    BlogEnums(String email, String feedAddress, String description) {
        this.email = email;
        this.feedAddress = feedAddress;
        this.description = description;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFeedAddress() {
        return this.feedAddress;
    }

    public String getDescription() {
        return this.description;
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

    public static String getDescriptionByBlogAddress(String blogAddress) {
        String address = CommonUtils.trimHttpScheme(blogAddress);

        for (BlogEnums e : BlogEnums.values()) {
            if (e.getFeedAddress().contains(address)) {
                return e.getDescription();
            }
        }
        return "";
    }

}
