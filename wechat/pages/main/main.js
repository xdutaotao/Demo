const app = getApp()

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

    myRank: "-",
    myName: "",
    myScore: "-",
    myAvatar: "",

    pageIndex: 0,
    isHideLoadMore: true,

  },

  getDayBillboard: function () {
    var that = this;
    wx.request({
      url: app.globalData.url + '/index.php?g=Api&m=CommonApi&a=getDayBillboard',
      data: {
        page: that.data.pageIndex
      },
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      method: 'GET',
      success: function (res) {
        console.log(res.data)
        console.log(that.data.pageIndex)
        if (that.data.pageIndex == 0) {
          that.setData({
            list: res.data,
            isHideLoadMore: true
          })

          res.data.forEach(function (item) {
            if (item.openid == wx.getStorageSync('openid')) {
              console.log(item);
              that.setData({
                myRank: item.m_id,
                myName: item.nickname,
                myScore: item.all_score,
                myAvatar: item.avatar
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

  onLoad:function(){
    var that = this;

    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo
      })

      that.getDayBillboard()
    }
    
    
  },

  // tab切换
  changeState:function(e){
    var that=this;
      if( this.data.currentTab === e.target.dataset.current) {  
      return false;  
    } else {  
        console.log(e.target.dataset.current)
       this.setData({
         currentTab: e.target.dataset.current
       })
    } 
  },
 

 
})