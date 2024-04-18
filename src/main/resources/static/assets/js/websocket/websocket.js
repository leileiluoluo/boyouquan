

function showGreeting(message) {
    console.log(message);
}


window.addEventListener('load', function () {
        // 首先，让我们检查我们是否有权限发出通知
        // 如果没有，我们就请求获得权限
        if (window.Notification && Notification.permission !== "granted") {
            Notification.requestPermission(function (status) {
                if (Notification.permission !== status) {
                    Notification.permission = status;
                }
            });
        }

        if (window.Notification && Notification.permission === "granted") {

        const stompClient = new StompJs.Client({
            brokerURL: 'wss://www.boyouquan.com/websocket'
        });

        stompClient.onConnect = (frame) => {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/broadcasts', (message) => {
                var message = JSON.parse(message.body);
                webnotify(message.message, message.gotoUrl);
            });
        };

        stompClient.onWebSocketError = (error) => {
            console.error('Error with websocket', error);
        };

        stompClient.onStompError = (frame) => {
            console.error('Broker reported error: ' + frame.headers['message']);
            console.error('Additional details: ' + frame.body);
        };

        stompClient.activate();



                }

                // 如果用户没有选择是否显示通知
                // 注：因为在 Chrome 中我们无法确定 permission 属性是否有值，因此
                // 检查该属性的值是否是 "default" 是不安全的。
                else if (window.Notification && Notification.permission !== "denied") {
                    Notification.requestPermission(function (status) {
                        if (Notification.permission !== status) {
                            Notification.permission = status;
                        }
                        // 如果用户同意了
                        if (status === "granted") {
                            var message = JSON.parse(message.body);
                            webnotify(message.message, message.gotoUrl);
                        }

                    });
                }


    })


function webnotify(message, gotoUrl){
        var notify = new Notification(
            "博友圈通知",
            {
                dir: 'auto',
                lang: 'zh-CN',
                icon: '/assets/images/sites/logo/logo-small.png',//通知的缩略图,//icon 支持ico、png、jpg、jpeg格式
                body: message //通知的具体内容
            }
        );
        notify.onclick = function () {
            //如果通知消息被点击,通知窗口将被激活
            window.focus();
            window.open("http://?");//打开指定url
            notify.close();
        },
            notify.onerror = function (e) {
                console.log(e);
                console.log("HTML5桌面消息出错！！！");
            };
        notify.onshow = function () {
            setTimeout(function () {
                notify.close();
            }, 8000)
        };
        notify.onclose = function () {
            console.log("HTML5桌面消息关闭！！！");
        };
    }