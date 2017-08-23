$(document).ready(function () {
    get_data_ontime();
        $.ajax({
        type:"post",
        url:"http://www.xgjsk5.com/shifouguanbi",
        async:true,
        success:function (data) {
            $('title').html(data[0].name);  
        }
    });



});

var timer=null;
var timer2=null;

function get_data_ontime() {
    get_data();
    setInterval(function () {
        $.ajax({
            type:"post",
            url:"http://www.xgjsk5.com/servertime",
            async:true,
            success:function (d) {
                var time=d.servertime;
                bb(time)
            }
        });
        function bb(time) {
            time=new Date(time);
            if(time.getMinutes()%5==0 && time.getSeconds()==30){
                clearInterval(timer2);  
                    get_data();


            }
        }

    },1000);


}




function get_data() {
    $.ajax({
        type:"post",
        url:"http://www.xgjsk5.com/onedata",
        async:true,
        success:function (data) {
            newNum(data);
            clearInterval(timer);
            timer=setInterval(function () {
                daojishi(data);
            },1000);
        }
    });
}

function daojishi(data) {

    $.ajax({
        type:"post",
        url:"http://www.xgjsk5.com/servertime",
        async:true,
        success:function (d) {
            var time=d.servertime;
            ll(time)
        }
    });

    function ll(time) {
        var nowtime=new Date(time+1000*30);
        var nexttime=(data[0].next_time*1000)+(1000*30);
        var daojishi=nexttime-nowtime;
        daojishi=new Date(daojishi);
        var min=daojishi.getMinutes()<10 && daojishi.getMinutes()>=0 ?"0"+daojishi.getMinutes():daojishi.getMinutes();
        var sec=daojishi.getSeconds()<10 && daojishi.getSeconds()>=0?"0"+daojishi.getSeconds():daojishi.getSeconds();
        if(min=="00" && sec==10){   
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
        if(parseInt(min)>5){
            min="00";
            sec="00";
        }
        var djstext=min+":"+sec;
        $('.new_num_right span').html(djstext);
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