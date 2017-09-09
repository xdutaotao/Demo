var app = getApp()

// pages/login/login.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    phone: '',
    code: '',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(app.globalData.userInfo)
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    
  },

  getCode: function(){
    if (this.data.phone == null || this.data.phone.length != 11){
      wx.showToast({
        title: '手机号输入错误',
        duration: 2000
      });
      return;
    }

    var that = this;
    wx.request({
      url: app.globalData.url + '/index.php?g=Api&m=CommonApi&a=sendCode',
      data: {
        phone: that.data.phone
      },
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      method: 'POST',
      success: function (res) {
        console.log(res)
      }
    })

  },

  goLogin: function(){


    var that = this;
    wx.request({
      url: app.globalData.url + '/index.php?g=Api&m=CommonApi&a=bindPost',
      data: {
        phone: that.data.phone,
        code: that.data.code,
        openid: wx.getStorageSync('openid')
      },
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      method: 'POST',
      success: function (res) {
        console.log(res)
        if (res.data.code == 1){
          wx.redirectTo({
            url: '../firstPage/firstPage',
          })
        }
        
      }
    })

    

  },

  listenerPhoneInput: function(e){
    this.data.phone = e.detail.value;
  },

  listenerCodeInput: function(e){
    this.data.code = e.detail.value;
  }


})