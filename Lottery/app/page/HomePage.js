/**
 * Created by guzhenfu on 17/4/9.
 */

import React from 'react'
import {
    StyleSheet,
    View,
    Text,
    Image,
    TouchableHighlight,
    FlatList,
    RefreshControl,
    ScrollView} from 'react-native'

import Swiper from 'react-native-swiper'
import ApiContant from './../common/ApiContant'
import './../common/ToastLog'
import HomeModle from './../modle/HomeModle'
import Utils from '../common/theme'
import WebViewPage from './WebViewPage'
import Loading from './../component/Loading'

let projectText = ['三分时时彩', '重庆时时彩', '北京PK拾',
                    'PC蛋蛋', '三分PK拾', '香港六合彩', '广东11选5', '新疆时时彩',
                    '天津时时彩', '上海11选5', '安徽快3', '福彩3D' ];
let projectImage = [
    require('./../image/photo01.png'), require('./../image/photo02.png'),
    require('./../image/photo03.png'), require('./../image/photo04.png'),
    require('./../image/photo05.png'), require('./../image/photo06.png'),
    require('./../image/photo07.png'), require('./../image/photo08.png'),
    require('./../image/photo09.png'), require('./../image/photo10.png'),
    require('./../image/photo11.png'), require('./../image/photo12.png')
];

let cycleImage = [
    require('./../image/banner1.png') , require('./../image/banner2.png') ,
    require('./../image/banner3.png') ,
];


let nameLeft = ['472938*****', '972938*****', '672938*****',
    '372938*****', '8234938*****', '872938*****', '572938*****', '462938*****',
    '472938*****', '972938*****', '672938*****', '372938*****' ];


let nameCenter = ['喜中748元', '喜中48元', '喜中248元',
    '喜中458元', '喜中645元', '喜中748元', '喜中756元', '喜中548元',
    '喜中748元', '喜中48元', '喜中248元', '喜中458元' ];


let nameRight = ['购买幸运28', '购买幸运28', '购买幸运28',
    '购买幸运28', '购买幸运28', '购买幸运28', '购买幸运28', '购买幸运28',
    '购买幸运28', '购买幸运28', '购买幸运28', '购买幸运28' ];

export default class HomePage extends React.Component{
    // 构造
      constructor(props) {
        super(props);

        // 初始状态
        this.state = {
            loaded: false,
            imageViewsData: [],
            noticeData: [],
            dataSource: [],
            detailDataSource: [],
            refreshing: false,
            isPushOk: false,
        };
      }

    /**
     * 得到轮播图的数据
     * @private
     */
    componentWillMount(){
        if (!this.state.loaded) {
            // this._getNetWorkData();
            this.setState({loaded: true});
        }
    }

    /**
     * 请求网络数据
     */
    _getNetWorkData(resolve){

        /**
         * 公告
         */
        HomeModle.getInstance().getTopInformList(ApiContant.NOTICE_CATOGOTY_ID)
            .then(data => {
                this.setState({
                    noticeData: JSON.parse(data.DATA)
                });
            }, error => {

            })
            .finally(() => {
                if (!this.state.loaded){
                    this.setState({loaded: true});
                }
            });
    }



    /**
     * 渲染轮播图UI
     * @returns {Array}
     * @private
     */
    _renderSwipeImage(){
        let imageViews = [];
        if (this.state.loaded){
            for(let index=0; index<3; index++){
                imageViews.push(
                    <TouchableHighlight style={{flex: 1}} key={index}
                                        onPress = {() => {
                                            this.props.navigator.push({component: WebViewPage,
                                                args: {url: 'http://www.baidu.com'}})}}>
                        <Image style={{flex: 1}}
                               source={cycleImage[index]}
                               resizeMode='repeat'/>
                    </TouchableHighlight>
                )
            }
            return imageViews;
        }
    }

    /**
     * 渲染项目栏UI
     * @private
     */
    _renderProject(){
        let project = [];
        for(let i=0; i<12; i++){
            project.push(
                    <TouchableHighlight style={styles.projectClickStyle}
                                key={i}
                                underlayColor={Utils.underClickColor}
                                onPress={()=>{}}>
                        <View style={styles.imageStyle} >
                            <Image source={projectImage[i]}
                                   style={styles.imageStyleView}
                                    resizeMode='stretch'/>
                            <Text>{projectText[i]}</Text>
                        </View>
                    </TouchableHighlight>
            );
        }
        return project;
    }



    _renderName(){
        let imageViews = [];
        if (this.state.loaded){
            for(let index=0; index<3; index++){
                imageViews.push(
                    <TouchableHighlight style={{flex: 1}} key={index}
                                        onPress = {() => {
                                            this.props.navigator.push({component: WebViewPage,
                                                args: {url: 'http://www.baidu.com'}})}}>
                        <View style={styles.nameStyle} >
                            <Text>{nameLeft[index]}</Text>
                            <Text style={styles.nameCenter}>{nameCenter[index]}</Text>
                            <Text>{nameRight[index]}</Text>
                        </View>
                    </TouchableHighlight>
                )
            }
            return imageViews;
        }
    }


    render(){
        if (!this.state.loaded){
            return(
                <Loading />
                );
        }else{
            return(
                <View style={styles.container}>
                    <ScrollView
                        horizontal={false}>
                    <Swiper height={150}
                            paginationStyle={{bottom:10}}
                            autoplay={true}
                            loop={true}>
                        {this._renderSwipeImage()}
                    </Swiper>

                    <View style={styles.lotteryStyle}>
                        <View style={styles.styleLine}>

                        </View>

                        <Text style={styles.styleText}>
                            彩票种类
                        </Text>

                        <View style={styles.styleLine}>

                        </View>
                    </View>

                    <View style={styles.dividerLine}/>

                    <View style={styles.imageWrapperStyle}>
                        {this._renderProject()}

                    </View>

                     <View style={[styles.dividerLine, {marginTop: 20}]} />

                     <View style={styles.lotteryStyle}>
                        <View style={styles.styleLine}>

                        </View>

                        <Text style={styles.styleText}>
                            中奖名单
                        </Text>

                        <View style={styles.styleLine}>

                        </View>
                    </View>

                        <View style={styles.dividerLine}></View>

                    <Swiper height={50}
                            horizontal={false}
                            paginationStyle={{right:-20}}
                            autoplay={true}
                            loop={true}>
                        {this._renderName()}
                    </Swiper>

                    </ScrollView>

                </View>
            );
        }
    }
}




const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: 'white',
    },
    imageWrapperStyle:{
        height:250,
        width: '100%',
        flexDirection: 'row',
        flexWrap: 'wrap',
    },
    projectClickStyle:{
        height: '35%',
        width: '25%',
        justifyContent: 'center',
        alignItems: 'center',
    },
    imageStyle:{
        height: '100%',
        width: '100%',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
    },
    imageStyleView:{
        height: '60%',
        width: '55%',
        margin: 5,
    },
    announceWrapStyle:{
        width:'100%',
        height: 20,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        marginTop:20
    },

    lotteryStyle:{
        height: 60,
        alignItems: 'center',
        flexDirection: 'row',
        justifyContent: 'center'
    },

    styleLine:{
        backgroundColor: 'red',
        width: 20,
        height: 2,
    },

    styleText:{
        color: 'black',
        margin: 20,
    },

    dividerLine:{
        width: '100%',
        backgroundColor: Utils.dividerBgColor,
        height: 2,
    },
    nameStyle:{
        height: '100%',
        width: '100%',
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: 10,
    },

    nameCenter:{
        color: 'red',
    }

});