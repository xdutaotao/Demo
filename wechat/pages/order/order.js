

const app = getApp()

var list = [];

Page({
  data: {
    imgUrls: [],
    indicatorDots: true,
    autoplay: true,
    interval: 3000,
    duration: 1000,
    currentTab: 0,
    // indicator-dots:true

    motto: 'Hello World',
    userInfo: {},
    list: [],
    pageIndex: 0,
    isHideLoadMore: true
  },

  getNotice: function () {
    var that = this;
    wx.request({
      url: app.globalData.url + '/index.php?g=Portal&m=Index&a=getNotice',
      data: {
        page: that.data.pageIndex
      },
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      method: 'GET',
      success: function (res) {
        console.log(res.data)
        if (that.data.pageIndex == 0){
          that.setData({
            list: res.data,
            isHideLoadMore: true
          })
        }else{
          that.setData({
            list: that.data.list.concat(res.data),
            isHideLoadMore: true
          })
        }

        if (res.data && res.data.length>0)
          that.data.pageIndex++;
      }
    });
  },

  onLoad: function () {
    var that = this;
    that.getNotice();
  },

  onReachBottom: function () {
    var that = this;
    console.log("333333")

    this.setData({
      isHideLoadMore: false,
    })

    that.getNotice();
  },

  goToDetail: function(e){
    let itemIndex = parseInt(e.currentTarget.dataset.index);
    let id = this.data.list[itemIndex].id;
    console.log(id);
    wx.navigateTo({
      url: '../orderDetail/orderDetail?id='+id
    })
  }


})