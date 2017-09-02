//index.js
//获取应用实例
var app = getApp()

var appid = 'wx29602978ed48fcf4'
var sessionKey = 'e5fedcd8bb5be396f93ff7b247eecfa0=='

var WXBizDataCrypt = require('../../utils/RdWXBizDataCrypt.js');


Page({
  data: {
    motto: 'Hello World',
    userInfo: {}
  },
  //事件处理函数
  bindViewTap: function() {
    wx.navigateTo({
      url: '../logs/logs'
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




    wx.login({
      success: function (res) {
        if (res.code) {
          //发起网络请求
          console.log('获取用户登录成功！' + res.code)

          wx.request({
            url: 'https://api.weixin.qq.com/sns/jscode2session',
            data: {
              appid: appid,
              secret: sessionKey,
              cojs_codede: res.code,
              grant_type: "authorization_code"
            },
            header: {
              "Content-Type": "application/x-www-form-urlencoded"
            },
            method: 'GET', 
            success: function(response){
              console.log(response);
            }
          })


          if (wx.getWeRunData) {

            wx.getWeRunData({
              success(res) {
                console.log('获取计步接口成功！' + res.errMsg)
                const encryptedData = res.encryptedData
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






  }
})
