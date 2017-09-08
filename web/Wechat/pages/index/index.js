//index.js
//获取应用实例
var app = getApp()

var appid = 'wx29602978ed48fcf4'
var secret = '0bd7e3aa68811109cf25275bf6d6df6f'

var ctx = wx.createCanvasContext('canvasCircle');
var interval;
var varName;

//var WXBizDataCrypt = require('../../utils/RdWXBizDataCrypt.js');

var radius = 40;

Page({
  data: {
    motto: 'Hello World',
    userInfo: {},
    code: "",
    encryptedData:"",
    iv: "",
    session_key: "",
    openid:"",
    todayStep: ""
  },

  drawBgCircle: function(){
    //创建并返回绘图上下文context对象。
    var cxt_arc = wx.createCanvasContext('canvasCircle');
    cxt_arc.setLineWidth(16);
    cxt_arc.setStrokeStyle('#000');
    cxt_arc.setLineCap('round');
    cxt_arc.beginPath();
    cxt_arc.arc(100, 100, radius, 0, 2 * Math.PI, false);
    cxt_arc.stroke();
    cxt_arc.draw();
  },

  drawCircle: function () {
    clearInterval(varName);
    function drawArc(s, e) {
      //ctx.setFillStyle('white');
      //ctx.clearRect(0, 0, 200, 200);
      ctx.draw();
      var x = 100, y = 100;
      ctx.setLineWidth(5);
      ctx.setStrokeStyle('#d81e06');
      ctx.setLineCap('round');
      ctx.beginPath();
      ctx.arc(x, y, radius, s, e, false);
      ctx.stroke()
      ctx.draw()
    }
    var step = 1, startAngle = 1.5 * Math.PI, endAngle = 0;
    var animation_interval = 10, n = 10;
    var animation = function () {
      if (step <= n) {
        endAngle = step * 2 * Math.PI / n + 1.5 * Math.PI;
        drawArc(startAngle, endAngle);
        step++;
      } else {
        clearInterval(varName);
      }
    };
    svarName = setInterval(animation, animation_interval);
  },

  get3rdSession: function () {
    let that = this
    wx.request({
      url: 'http://lh.2donghua.com/miniApps/convert.php',
      data: {
        code: that.data.code,
        encryptedData: that.data.encryptedData,
        iv: that.data.iv,
        appid: appid,
        secret: secret
      },
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      method: 'POST',
      success: function (res) {
        let response = res.data.substring(3)
        that.stepInfoList = JSON.parse(response).stepInfoList;
        console.log(that.stepInfoList)
        that.todayStep = that.stepInfoList[that.stepInfoList.length-2].step+"";
        console.log(that.todayStep)
        that.setData({
          todayStep: that.todayStep
        })
        
      }
    })
  },

  onLoad: function () {
    console.log('onLoad')
    var that = this
    //调用应用实例的方法获取全局数据
    app.getUserInfo(function(userInfo){
      //更新数据
      that.setData({
        userInfo:userInfo
      })
    })

    //that.drawBgCircle();


    wx.login({
      success: function (res) {
        if (res.code) {
          //发起网络请求
          console.log('获取用户登录成功！' + res.code)
          that.setData({code: res.code})


          if (wx.getWeRunData) {

            wx.getWeRunData({
              success(res) {
                console.log('获取计步接口成功！' + res.errMsg)
                const encryptedData = res.encryptedData
                that.setData({iv:res.iv})
                that.setData({encryptedData: encryptedData})
                that.get3rdSession();
                that.drawCircle();

              },
              fail(error) {
                console.log(error);
              }
            })

          } else {
            console.log("不支持计步");
          }


        } else {
          console.log('获取用户登录态失败！' + res.errMsg)
        }
      }
    });

  },


  onReady: function () {
    
    // 页面渲染完成  
    var cxt_arc = wx.createCanvasContext('canvasCircle');//创建并返回绘图上下文context对象。  
    cxt_arc.setLineWidth(6);
    cxt_arc.setStrokeStyle('#d2d2d2');
    cxt_arc.setLineCap('round')
    cxt_arc.beginPath();//开始一个新的路径  
    cxt_arc.arc(106, 106, 40, 0, 2 * Math.PI, false);//设置一个原点(106,106)，半径为100的圆的路径到当前路径  
    cxt_arc.stroke();//对当前路径进行描边  

    cxt_arc.draw(); 

  }


})
