var COOKIE_TOKEN = "ADMIN_TOKEN";
var COOKIE_NAME = "ADMIN_NAME";


var cookie = {};
cookie.setToken = function (value) {
    $.cookie(COOKIE_TOKEN, value, {expires: 7, path: '/'});
}
cookie.setName = function (value) {
    $.cookie(COOKIE_NAME, value, {expires: 7, path: '/'});
}

cookie.getToken = function () {
    return $.cookie(COOKIE_TOKEN);
}
cookie.getName = function () {
    return $.cookie(COOKIE_NAME);
}

cookie.clear = function () {
    $.cookie(COOKIE_TOKEN, null, {expires: 0, path: '/'});
    $.cookie(COOKIE_NAME, null, {expires: 0, path: '/'});
}