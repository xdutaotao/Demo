

var timer=null;
var timer2=null;

$.ajax({
    type:"post",
    url:serverTimeUrl,
    async:true,
    success:function (d) {
        var time=d.servertime;
        bb(time)
    }
});

function bb(time) {
    time=new Date(time);
    get_data();
}




function get_data() {
    $.ajax({
        type:"post",
        url:oneDataUrl,
        async:true,
        success:function (data) {
            newNum(data);
            daojishi(data);
        }
    });
}

function daojishi(data) {

    $.ajax({
        type:"post",
        url:serverTimeUrl,
        async:true,
        success:function (d) {
            var time=d.servertime;
            ll(time)
        }
    });


    function ll(time) {



        var timeStart=new Date(time).Format("yyyy-MM-dd")+" 00:00:25";

        timeStart=new Date(timeStart);


        var timeStartInt=timeStart.getTime();

        var subTime=timeerCount-parseInt((time-timeStartInt)/1000)%timeerCount;


        var min="0"+parseInt(subTime/60);
        var sec=subTime%60;
        if(sec<10)
        {
            sec="0"+sec;
        }

        var djstext=min+":"+sec;
        $('.new_num_right span').html(djstext);

        setInterval(function()
        {

            if(min=="00" && sec=="10"){
                console.log("经来");
                clearInterval(timer2);
                timer2=setInterval(function () {

                    var num=parseInt(Math.random()*6+1);
                    var num2=RandomNumBoth(52,54);
                    var html="<img src='images/dice_"+num+".png' >";
                    html+="<img src='images/dice_"+num+".png' >";
                    html+="<img src='images/dice_"+num+".png' >";
                    $('.result_pic').html(html);
                    $('.result_pic img').css("margin-left",num2);


                },400)
            }



            subTime -= 1;

            min="0"+parseInt(subTime/60);

            sec=subTime%60;
            if(sec<10)
            {
                sec="0"+sec;
            }

            var djstext=min+":"+sec;
            $('.new_num_right span').html(djstext);

            if(subTime<=0)
            {
                clearInterval(timer2);
                getdata();
                subTime=timeerCount;
            }


        },1000);






    }

}



function newNum(data) {
    var html="<img src='images/dice_"+data[0].g_ball_1+".png' >";
    html+="<img src='images/dice_"+data[0].g_ball_2+".png' >";
    html+="<img src='images/dice_"+data[0].g_ball_3+".png' >";
    $('.result_pic').html(html);
    var now_qishu="本期第<span><div>"+data[0].g_qishu+"</div></span>期";
    $('#now_qishu').html(now_qishu);
    var sheng_qishu="当前"+data[0].g_qishu.substr(8,3)+"期,剩"+(288-data[0].g_qishu.substr(8,3))+"期";
    $('#sheng_qishu').html(sheng_qishu);
    var next_qishu="下期【第<div class='nextqi_text'>"+(data[0].g_qishu-0+1)+"</div>期】";
    $('#next_qishu').html(next_qishu);
}



function RandomNumBoth(Min,Max){
    var Range = Max - Min;
    var Rand = Math.random();
    var num2 = Min + Math.round(Rand * Range); //四舍五入
    return num2;
}


Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}