function getDate(tm){
    var tt=new Date(parseInt(tm) * 1000).toLocaleString().substr(0,16);
    return tt;
}


function GetQueryString(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}