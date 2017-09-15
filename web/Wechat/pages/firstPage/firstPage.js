// firstPage.js
//index.js
//获取应用实例
var app = getApp()

// var appid = 'wx29602978ed48fcf4'
// var secret = '0bd7e3aa68811109cf25275bf6d6df6f'

var appid = 'wx993b21125135952b'
var secret = 'e15980b0bf3aa38985bee7c386337c67'

var ctx = wx.createCanvasContext('canvasCircle');
var interval;
var svarName;

var radius = 80;
var lineWidth = 12;

Page({
  data: {
    motto: 'Hello World',
    userInfo: {},
    code: "",
    encryptedData: "",
    iv: "",
    session_key: "",
    openid: "",
    todayStep: "",
    distance: "",
    time: "",
    kaka: "",
    allStep: 0
  },

  drawBgCircle: function () {
    //创建并返回绘图上下文context对象。
    var cxt_arc = wx.createCanvasContext('canvasBgCircle');
    cxt_arc.setLineWidth(lineWidth);
    cxt_arc.setStrokeStyle('#2c4c60');
    cxt_arc.setLineCap('round');
    cxt_arc.beginPath();
    cxt_arc.arc(120, 130, radius, 0, 2 * Math.PI, false);
    cxt_arc.stroke();
    cxt_arc.draw();
  },

  drawCircle: function () {
    var that = this;
    clearInterval(svarName);
    //ctx.createLinearGradient(100, 100, )
    function drawArc(s, e) {
      ctx.draw();
      var x = 120, y = 130;
      ctx.setLineWidth(lineWidth);
      ctx.setStrokeStyle('#e9890B');
      ctx.setLineCap('round');
      ctx.beginPath();
      ctx.arc(x, y, radius, s, e, false);
      ctx.stroke()
      ctx.draw()
    }
    var step = 1, startAngle = Math.PI, endAngle = 0;
    var animation_interval = 100, n = 100;

    console.log(that.data.allStep+"2222")

    var animation = function () {
      if (step <= that.data.allStep) {
        endAngle = step * 2 * Math.PI / n + startAngle;
        //console.log(endAngle)
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
      url: app.globalData.url +'/index.php?g=Api&m=CommonApi&a=convertStep',
      data: {
        code: that.data.code,
        encryptedData: that.data.encryptedData,
        iv: that.data.iv,
        appid: appid,
        secret: secret,
        session_key: wx.getStorageSync('session_key'),
        openid: wx.getStorageSync('openid')
      },
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      method: 'POST',
      success: function (res) {
        let response = res.data.substring(3)
        that.stepInfoList = JSON.parse(response).stepInfoList;
        console.log(res)

        if (that.stepInfoList != null)
          that.todayStep = that.stepInfoList[that.stepInfoList.length - 1].step + "";
        
        var all = that.todayStep/1000;
        if (all <= 1){
          all = 1;
        }else if (all >= 100){
          all = 100;
        }
        console.log("all" + all)

        that.setData({
          todayStep: that.todayStep,
          distance: (that.todayStep*5/8000).toFixed(2),
          time: (that.todayStep/120).toFixed(2),
          kaka: (that.todayStep * 655 / 720).toFixed(2),
          allStep: all,
        })

        that.drawBgCircle();
        that.drawCircle();

      }
    })
  },

  judgeBindInfo: function(){
    let that = this
      wx.request({
        url: app.globalData.url + '/index.php?g=Api&m=CommonApi&a=judgeBindInfo',
        data: {
          code: that.data.code,
          appid: appid,
          secret: secret
        },
        header: {
          "Content-Type": "application/x-www-form-urlencoded"
        },
        method: 'POST',
        success: function (res) {
          console.log(res.data.exist);

          wx.setStorageSync('openid', res.data.openid);
          wx.setStorageSync("session_key", res.data.session_key)

          if (res.data.exist == 0){
            //不存在
            // wx.redirectTo({
            //   url: '../login/login',
            // })
          }else{
            if (wx.getWeRunData) {

              wx.getWeRunData({
                success(res) {
                  console.log('获取计步接口成功！' + res.errMsg)
                  const encryptedData = res.encryptedData
                  that.setData({ iv: res.iv })
                  that.setData({ encryptedData: encryptedData })
                  that.get3rdSession();
                  

                },
                fail(error) {
                  console.log(error);
                }
              })

            } else {
              console.log("不支持计步");
            }

          }


        }
      })
  },

  postUserInfo: function(UserInfo){
    let that = this
    wx.request({
      url: app.globalData.url + '/index.php?g=Api&m=CommonApi&a=userInfoSave',
      data: {
        avatarUrl: UserInfo.avatarUrl,
        city: UserInfo.city,
        country: UserInfo.country,
        gender: UserInfo.gender,
        language: UserInfo.language,
        nickName: UserInfo.nickName,
        province: UserInfo.province,
        openid: wx.getStorageSync('openid')
      },
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      method: 'POST',
      success: function (res) {
        console.log(res.data);

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

      console.log(that.data.userInfo)
      that.postUserInfo(that.data.userInfo);

    })
    

    wx.login({
      success: function (res) {
        if (res.code) {
          //发起网络请求
          console.log('获取用户登录成功！' + res.code)
          that.setData({ code: res.code })

          that.judgeBindInfo();


        } else {
          console.log('获取用户登录态失败！' + res.errMsg)
        }
      }
    });

  },

  goSecondTab: function(){
    wx.switchTab({
      url: '../main/main'
    }); 

  },

  goThirdTab: function(){
    wx.switchTab({
      url: '../mall/mall'
    });
  },

  goFourthTab: function(){
    wx.switchTab({
      url: '../order/order'
    });
  }



})
