var mainService=require('mainService.js');

const app = getApp()

var myList = [
  { name: "1", sex: "2" },
  { name: "1", sex: "2" },
  { name: "1", sex: "2" },
  { name: "1", sex: "2" },
  { name: "4", sex: "20" },
  { name: "1", sex: "2" },
  { name: "1", sex: "2" },
  { name: "1", sex: "2" }
];


var rankList = [
  { name: "11", sex: "12" },
  { name: "12", sex: "22" },
  { name: "13", sex: "32" },
  { name: "14", sex: "42" },
  { name: "45", sex: "20" },
  { name: "16", sex: "52" },
  { name: "17", sex: "26" },
  { name: "18", sex: "27" }
]

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

    myRank: "4",
    myName: "20",
    myStep: "4732",
    myHead: "",

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
    
    
    that.setData({
      list: myList
    })

    // console.log(list)
    
  },

  // tab切换
  changeState:function(e){
    var that=this;
      if( this.data.currentTab === e.target.dataset.current) {  
      return false;  
    } else {  
      console.log(that.data.currentTab)
      if (that.data.currentTab == 0){
        that.setData({
          currentTab: e.target.dataset.current,
          list: myList
        }) 

      }else{
        that.setData({
          currentTab: e.target.dataset.current,
          list: rankList
        }) 
      }
       
    } 
  },
 

 
})