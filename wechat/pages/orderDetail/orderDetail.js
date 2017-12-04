// orderDetail.js
const app = getApp()

var WxParse = require('../../wxParse/wxParse.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    id: 0,
    detail: ''
  },

  getNoticeDetail: function () {
    var that = this;
    wx.request({
      url: app.globalData.url + '/index.php?g=Portal&m=Index&a=noticeDetail',
      data: {
        id: that.data.id
      },
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      method: 'GET',
      success: function (res) {
        console.log(res.data.detail);

        var article = res.data.detail;

        WxParse.wxParse('article', 'html', article, that, 20);



      }
    });
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var objID = options.id;
    this.setData({
      id: objID
    })
    this.getNoticeDetail()
  },

  
})