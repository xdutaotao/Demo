var app = getApp()

var interval ;

// pages/login/login.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    phone: '',
    code: '',
    codeNum: 60,
    codeText: '获取验证码',
    isShowToast: false  
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

    if (that.data.codeText == '获取验证码' && that.data.codeNum == 60){
        interval = setInterval(function () {
        console.log(that.data.codeNum)
        that.data.codeNum--;
        that.setData({
          codeText: that.data.codeNum + 's'
        })

        if (that.data.codeNum == 0) {
          that.setData({
            codeNum: 60,
            codeText: '获取验证码'
          })
          clearInterval(interval);
        }
      }, 1000);


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
          console.log(res.data)
        }

      })

    }
    

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
          clearInterval(interval)
          wx.redirectTo({
            url: '../firstPage/firstPage',
          })
        }else{
          that.setData({
            count: 1500,
            toastText: res.data.msg
          });
          that.showToast(); 
        }
        
      }
    })

    

  },

  listenerPhoneInput: function(e){
    this.data.phone = e.detail.value;
  },

  listenerCodeInput: function(e){
    this.data.code = e.detail.value;
  },

  showToast: function () {
    var _this = this;
    // toast时间  
    _this.data.count = parseInt(_this.data.count) ? parseInt(_this.data.count) : 3000;
    // 显示toast  
    _this.setData({
      isShowToast: true,
    });
    // 定时器关闭  
    setTimeout(function () {
      _this.setData({
        isShowToast: false
      });
    }, _this.data.count);
  },


})