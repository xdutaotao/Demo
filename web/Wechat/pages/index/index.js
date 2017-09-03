//index.js
//获取应用实例
var app = getApp()

var appid = 'wx29602978ed48fcf4'
var secret = '0bd7e3aa68811109cf25275bf6d6df6f'

var WXBizDataCrypt = require('../../utils/RdWXBizDataCrypt.js');


Page({
  data: {
    motto: 'Hello World',
    userInfo: {},
    code: "",
    encryptedData:"",
    iv: "",
    session_key: "",
    openid:""
  },
  //事件处理函数
  bindViewTap: function() {
    wx.navigateTo({
      url: '../logs/logs'
    })
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
        console.log(response)
        console.log(JSON.parse(response).stepInfoList)
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
