/**
 * Created by jufengyd on 2017/2/13.
 */
$(document).ready(function () {
    getdata();
    setInterval(function () {
        $.ajax({
            type:"post",
            url:"http://www.xgjsk5.com/servertime",
            async:true,
            success:function (d) {
                var nowtime=d.servertime;
                cc(nowtime);
            }
        });
        function cc(nowTime) {
            nowTime=new Date(nowTime);
            if(nowTime.getMinutes()%5==0 && nowTime.getSeconds()==30){

                    getdata();

            }
        }

   },1000);

});

function getdata() {
    $.ajax({
        type: "post",
        url: "http://www.xgjsk5.com/alldata",
        async: true,
        success: function (data) {
            updata(data);
        }
    });
}


function updata(data) {
    $('.nums_table tbody tr').remove();
    for (var j=0;j<49;j++){
        var timer = new Date(data[j].g_time * 1000);
        var mon = timer.getMonth() + 1 < 10 ? '0' + (timer.getMonth() + 1) : timer.getMonth() + 1;
        var date = timer.getDate() < 10 ? '0' + timer.getDate() : timer.getDate();
        var hou = timer.getHours() < 10 ? '0' + timer.getHours() : timer.getHours();
        var min = timer.getMinutes() < 10 ? '0' + timer.getMinutes() : timer.getMinutes();
        var day = null;
        if (timer.getDay() == 0) {
            day = "周日";
        }
        if (timer.getDay() == 1) {
            day = "周一";
        }
        if (timer.getDay() == 2) {
            day = "周二";
        }
        if (timer.getDay() == 3) {
            day = "周三";
        }
        if (timer.getDay() == 4) {
            day = "周四";
        }
        if (timer.getDay() == 5) {
            day = "周五";
        }
        if (timer.getDay() == 6) {
            day = "周六";
        }
        var zonghe = (data[j].g_ball_1 - 0) + (data[j].g_ball_2 - 0) + (data[j].g_ball_3 - 0);
        var arr = [(data[j].g_ball_1 - 0), (data[j].g_ball_2 - 0), (data[j].g_ball_3 - 0)].sort();
        var arr2 = [];
        var html = "<tr><td class='tdone'>" + mon + "-" + date + " [" + day + "] " + hou + ":" + min + "</td>";
        html += "<td>" + data[j].g_qishu + "</td>";
        html += "<td style='width:273px'><span><img src='images/dice_" + data[j].g_ball_1 + ".png' width='70px' height='70px'></span> <span><img src='images/dice_" + data[j].g_ball_2 + ".png' width='70px' height='70px'></span> <span><img src='images/dice_" + data[j].g_ball_3 + ".png' width='70px' height='70px'></span> </td>";
        html += "<td class='zonghe_td'>" + zonghe + "</td>";
        if (arr[0] + arr[1] + arr[2] > 10) {
            arr2.push("大");
        } else {
            arr2.push("小");
        }
        if ((arr[0] + arr[1] + arr[2]) % 2 == 0) {
            arr2.push("双");
        } else {
            arr2.push("单");
        }
        if (arr[0] == arr[1] && arr[1]==arr[2]) {
            arr2.push("豹");
        }
        if (arr[0] + 1 == arr[1] && arr[1] + 1 == arr[2]) {
            arr2.push("顺");
        }
        if(checkarr(arr2,"大")==true){
            html+="<td> <div class='daresult'>大</div> </td><td></td>";
        }else {
            html+="<td></td><td> <div class='xiaoresult'>小</div> </td>";
        }
        if(checkarr(arr2,"单")==true){
            html+="<td> <div class='danresult'>单</div> </td><td></td>";
        }else {
            html+="<td></td><td> <div class='shuangresult'>双</div> </td>";
        }
        if(checkarr(arr2,"豹")==true){
            html+="<td> <div class='daresult'>豹</div> </td>";
        }else {
            html+="<td class='tdwidth'></td>";
        }
        if(checkarr(arr2,"顺")==true){
            html+="<td> <div class='daresult'>顺</div> </td></tr>";
        }else {
            html+="<td class='tdwidth'></td></tr>";
        }

        $('.nums_table tbody').append(html);
    }



}

function checkarr(arr2,yuansu) {
    for (var i = 0; i < arr2.length; i++) {
        if(arr2[i]==yuansu){
            return true;
        }
    }
    return false;
}