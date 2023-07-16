document.onkeydown = function() {
    var evt = window.event || arguments[0];
    if (evt && evt.keyCode == 13) {
        var input = document.getElementById('searchInput').value;
        window.location = '/blogs?keyword=' + encodeURIComponent(input);
    }
}

var query = window.location.href.split('?');
if (query.length > 1) {
    var urlStr = query[1];
    var params = new URLSearchParams(urlStr);
    var keyword = params.get('keyword');
    document.getElementById('searchInput').value = keyword;
}
