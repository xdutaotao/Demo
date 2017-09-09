

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
    list: [
      { name: "1", sex: "2" },
      { name: "1", sex: "2" },
      { name: "1", sex: "2" },
      { name: "1", sex: "2" },
      { name: "1", sex: "2" },
      { name: "1", sex: "2" },
      { name: "1", sex: "2" },
      { name: "1", sex: "2" }
    ],

    hasMore: true,
    page: 0,
  },

  onLoad: function () {
    var that = this;


    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo
      })
    }

  },

  // tab切换
  changeState: function (e) {
    var that = this;
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      that.setData({
        currentTab: e.target.dataset.current
      })
    }
  },
  //事件处理函数
  bindViewTap: function () {
    wx.navigateTo({
      url: '../mainDetail/maindetail'
    })
  },

  loadMore: function(e){
    var that = this;
    if (!this.data.hasMore) return

    console.log("1111111")
    this.setData({
      list: that.data.list.concat(this.data.list)
    })
    


    // var url = 'http://v.juhe.cn/weixin/query?key=f16af393a63364b729fd81ed9fdd4b7d&pno=' + (++that.data.page) + '&ps=10';
    // network_util._get(url,
    //   function (res) {
    //     that.setData({
    //       list: that.data.list.concat(res.data.result.list),
    //       hidden: true,
    //       hasRefesh: false,
    //     });
    //   }, function (res) {
    //     console.log(res);
    //   })

  },


})