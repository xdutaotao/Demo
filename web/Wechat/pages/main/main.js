var mainService=require('mainService.js');

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
      {name: "1", sex:"2"},
      { name: "1", sex: "2" },
      { name: "1", sex: "2" },
      { name: "1", sex: "2" },
      { name: "1", sex: "2" },
      { name: "1", sex: "2" },
      { name: "1", sex: "2" },
      { name: "1", sex: "2" }
    ],
  },

  onLoad:function(){
    var that = this;
    mainService.getTopInforData(function(res){
      
        that.setData({
          imgUrls:res
        })
    }, function(error){
        console.log(error)
    });

    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo
      })
    }
    
    // list.push({name:"1",sex:"2"})
    // list.push({name:'3',sex:'4'})
    // that.setData({
    //   list: list
    // })

    // console.log(list)
    
  },

  // tab切换
  changeState:function(e){
    var that=this;
      if( this.data.currentTab === e.target.dataset.current) {  
      return false;  
    } else {  
      that.setData( {  
        currentTab: e.target.dataset.current  
      })  
    } 
  },
     //事件处理函数
  bindViewTap: function() {
    wx.navigateTo({
      url: '../mainDetail/maindetail'
    })
  }

 
})