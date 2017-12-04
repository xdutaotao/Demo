

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
    ],

    isHideLoadMore: true,
    pageIndex: 0,

    myRank: '',
    myName: '',
    myScore: '',
    myAvatar: ''
  },

  getScore: function(){
    var that = this;
    wx.request({
      url: app.globalData.url + '/index.php?g=Api&m=CommonApi&a=getScore',
      data: {
        page: that.data.pageIndex
      },
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      method: 'GET',
      success: function (res) {
        console.log(res.data)
        if (that.data.pageIndex == 0) {
          that.setData({
            list: res.data,
            isHideLoadMore: true
          })

          res.data.forEach(function(item){
            if (item.openid == wx.getStorageSync('openid')){
              console.log(item.m_id);
              that.setData({
                myRank : item.m_id,
                myName : item.nickname,
                myScore : item.all_score,
                myAvatar : item.avatar
              })

              
            }
          })

        } else {
          that.setData({
            list: that.data.list.concat(res.data),
            isHideLoadMore: true
          })
        }

        if (res.data && res.data.length > 0)
          that.data.pageIndex++;
      }
    });

  },

  onLoad: function () {
    var that = this;


    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo
      })
    }

    that.getScore();

  },
  

  onReachBottom: function(){
    var that = this;
    console.log("333333")

    this.setData({
      isHideLoadMore: false
    })

    setTimeout(() => {
      that.setData({
        list: that.data.list.concat(that.data.list),
        isHideLoadMore: true
      })

    }, 2000);
      console.log(this.data.list)
  },


})