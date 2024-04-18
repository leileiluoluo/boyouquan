window.addEventListener('load', function () {
    if (window.Notification && Notification.permission !== "granted") {
        Notification.requestPermission(function (status) {
            if (Notification.permission !== status) {
                Notification.permission = status;
            }
        });
    }

    if (window.Notification && Notification.permission === "granted") {
        connect();
    } else if (window.Notification && Notification.permission !== "denied") {
        Notification.requestPermission(function (status) {
            if (Notification.permission !== status) {
                Notification.permission = status;
            }

            if (status === "granted") {
                connect();
            }
        });
    }
})

function connect() {
    const stompClient = new StompJs.Client({
        brokerURL: 'wss://www.boyouquan.com/websocket'
    });

    stompClient.onConnect = (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/broadcasts', (message) => {
            var message = JSON.parse(message.body);
            notify(message.message, message.gotoUrl);
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

function notify(message, gotoUrl) {
    var notify = new Notification(
        "博友圈通知",
        {
            dir: 'auto',
            lang: 'zh-CN',
            icon: '/assets/images/sites/logo/logo-small.png',
            body: message
        }
    );

    notify.onclick = function () {
        window.focus();
        window.open(gotoUrl);
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