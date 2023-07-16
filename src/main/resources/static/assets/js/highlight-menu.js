var currentURL = window.location.href;
var currentPath = currentURL.split('?')[0];
var menuItems = document.getElementById('menu').getElementsByTagName('a');
for (var i = 0; i < menuItems.length; i++) {
    var item = menuItems[i];
    if (currentPath == item.href || currentPath.startsWith(item.href + '/page/')) {
        item.classList.add("active");
    }
}