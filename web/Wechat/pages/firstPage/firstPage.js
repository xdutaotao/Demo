// firstPage.js
//index.js
//获取应用实例
var app = getApp()

var appid = 'wx29602978ed48fcf4'
var secret = '0bd7e3aa68811109cf25275bf6d6df6f'

var ctx = wx.createCanvasContext('canvasCircle');
var interval;
var svarName;
var allStep = 3;


var radius = 40;

Page({
  data: {
    motto: 'Hello World',
    userInfo: {},
    code: "",
    encryptedData: "",
    iv: "",
    session_key: "",
    openid: "",
    todayStep: ""
  },

  drawBgCircle: function () {
    //创建并返回绘图上下文context对象。
    var cxt_arc = wx.createCanvasContext('canvasBgCircle');
    cxt_arc.setLineWidth(8);
    cxt_arc.setStrokeStyle('#2c4c60');
    cxt_arc.setLineCap('round');
    cxt_arc.beginPath();
    cxt_arc.arc(100, 100, radius, 0, 2 * Math.PI, false);
    cxt_arc.stroke();
    cxt_arc.draw();
  },

  drawCircle: function () {
    clearInterval(svarName);
    //ctx.createLinearGradient(100, 100, )
    function drawArc(s, e) {
      ctx.draw();
      var x = 100, y = 100;
      ctx.setLineWidth(8);
      ctx.setStrokeStyle('#e9890B');
      ctx.setLineCap('round');
      ctx.beginPath();
      ctx.arc(x, y, radius, s, e, false);
      ctx.stroke()
      ctx.draw()
    }
    var step = 1, startAngle = Math.PI, endAngle = 0;
    var animation_interval = 100, n = 10;
    var animation = function () {
      if (step <= allStep) {
        endAngle = step * 2 * Math.PI / n + startAngle;
        console.log(endAngle)
        drawArc(startAngle, endAngle);
        step++;
      } else {
        clearInterval(svarName);
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
        that.todayStep = that.stepInfoList[that.stepInfoList.length - 2].step + "";
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
    app.getUserInfo(function (userInfo) {
      //更新数据
      that.setData({
        userInfo: userInfo
      })
    })


    wx.login({
      success: function (res) {
        if (res.code) {
          //发起网络请求
          console.log('获取用户登录成功！' + res.code)
          that.setData({ code: res.code })


          if (wx.getWeRunData) {

            wx.getWeRunData({
              success(res) {
                console.log('获取计步接口成功！' + res.errMsg)
                const encryptedData = res.encryptedData
                that.setData({ iv: res.iv })
                that.setData({ encryptedData: encryptedData })
                that.get3rdSession();
                that.drawBgCircle();
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


})
