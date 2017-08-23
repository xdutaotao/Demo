/**
 * Created by jufengyd on 2017/2/14.
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
                setTimeout(function () {
                    getdata();
                },1000);
            }
        }

    },1000);
});

function getdata() {
    $.ajax({
        type:"post",
        url:"http://www.xgjsk5.com/alldata",
        async:true,
        success:function (d) {
            updata(d);
        }
    });
}










function updata(data) {
    $('.nums_table tbody tr').remove();
    for(var v=0;v<49;v++){
        var html= "<tr><td>" + data[v].g_qishu + "</td>";
        html += "<td><span><img src='images/dice_" + data[v].g_ball_1 + ".png' width='50px' height='50px'></span>" +
            " <span><img src='images/dice_" + data[v].g_ball_2 + ".png' width='50px' height='50px'></span> " +
            "<span><img src='images/dice_" + data[v].g_ball_3 + ".png' width='50px' height='50px'></span> </td>";
        var arr = [(data[v].g_ball_1 - 0), (data[v].g_ball_2 - 0), (data[v].g_ball_3 - 0)];

        for(var j=1;j<=6;j++){
            var count=0;
            for(var i=0;i<arr.length;i++){
                if(arr[i]==j){
                    count+=1;
                }
            }
            if (count>0){
                if(count>1){
                    html+="<td> " +
                        "<div class='xiaoresult'><img src='images/big_"+j+".png' width='50px;' height='50px;'></div> " +
                        "<div class='zsttext'>"+count+"</div> " +
                        "</td>";
                }else {
                    html+="<td><div class='xiaoresult'><img src='images/big_"+j+".png' width='50px;' height='50px;'></div></td>";
                }
            }else {
                html+="<td class='zsttdwidth'>"+j+"</td>";
            }
        }
        html+="</tr>";
        $('.nums_table tbody').append(html);
    }

}