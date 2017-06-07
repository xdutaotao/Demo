/**
 * Created by guzhenfu on 17/4/9.
 */

import React from 'react'
import {
    StyleSheet,
    View,
    Text,
    FlatList,
    TouchableHighlight,
    ActivityIndicator,
    ScrollView} from 'react-native'

import LoginPage from './LoginPage'
import Utils from './../common/theme'
import RefreshList from 'react-native-refreshlist'

const preData = [
    {name: '双色球', time: '- 第2017055期',
        number: ['07', '12', '13', '20', '24', '31', '05'],
        date: '2017-05-14 21:15:00', money: '奖池:685895820元'},
    {name: '双色球', time: '- 第2017055期',
        number: ['16', '29', '30', '32', '33', '04', '05'],
        date: '2017-05-14 21:15:00', money: '奖池:5895820元'},
    {name: '双色球', time: '- 第2017055期',
        number: ['6', '2', '3', '20', '24'],
        date: '2017-05-14 21:15:00', money: '奖池:895820元'},
    {name: '双色球', time: '- 第2017055期',
        number: ['06', '12', '13', '20', '24', '31', '05'],
        date: '2017-05-14 21:15:00', money: '奖池:685895820元'},
    {name: '双色球', time: '- 第2017055期',
        number: ['08', '4', '16', '20', '24', '31', '05'],
        date: '2017-05-14 21:15:00', money: '奖池:195820元'},
    {name: '双色球', time: '- 第2017055期',
        number: ['07', '15', '13', '20', '24', '31', '05'],
        date: '2017-05-14 21:15:00', money: '奖池:2895820元'},
    {name: '双色球', time: '- 第2017055期',
        number: [ '20', '24', '31', '05'],
        date: '2017-05-14 21:15:00', money: '奖池:8858920元'},
    {name: '双色球', time: '- 第2017055期',
        number: ['07', '12', '13', '83', '24', '31', '05'],
        date: '2017-05-14 21:15:00', money: '奖池:123895820元'},
    {name: '双色球', time: '- 第2017055期',
        number: ['07', '44', '13', '20', '24', '31', '05'],
        date: '2017-05-14 21:15:00', money: '奖池:665895820元'},
    {name: '双色球', time: '- 第2017055期',
        number: ['07', '12', '13', '55', '05'],
        date: '2017-05-14 21:15:00', money: '奖池:26795820元'},
    ];

export default class MallPage extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            data: preData,
        };
        this.moreTime = 0;

        setTimeout(() => {
            this._listRef.setData(preData);

        }, 3000);
    }

    /**
     * 下拉刷新
     * @private
     */
    _onPullRelease(resolve){
        setTimeout(() => {
            resolve();
            this.moreTime = 0;
            this._listRef.setData(preData);
        }, 3000);
    }

    /**
     * 点击事件
     * @param item
     * @private
     */
    _onItemPress(item){
        //this.props.navigator.push({component: LoginPage})
    }

    /**
     * 加载更多  数据加载
     * @private
     */
    _loadMore(){
        setTimeout(() => {
            this._listRef.addData([]);
        }, 3000);
    }


    _renderItemCenter(numbers){
        let list = [];
        for (let i=0; i<numbers.length; i++){
            list.push(
                <View style={styles.listItemCenter} key={i}>
                    <Text style={styles.listItemCenterText}>{numbers[i]}</Text>
                </View>
            )
        }
        return list;
    }

    /**
     * 渲染item 布局
     * @param item
     * @returns {XML}
     * @private
     */
    _renderItem(item){
        return(
            <TouchableHighlight
                underlayColor={Utils.underClickColor}
                onPress={()=> {this._onItemPress(item)}}>
                <View style={styles.listWrapper}>

                    <View style={styles.listItemTop}>
                        <Text style={styles.listItemName}>{item.item.name}</Text>
                        <Text style={styles.listItemTime}>{item.item.time}</Text>
                    </View>
                    <View style={styles.listItemCenterWrap}>
                        {this._renderItemCenter(item.item.number)}
                    </View>
                    <View style={styles.listItemBottom}>
                        <Text style={styles.listItemBottomText}>{item.item.date}</Text>
                        <Text style={styles.listItemBottomTextRight}>{item.item.money}</Text>
                    </View>
                </View>
            </TouchableHighlight>
        )
    }

    _renderHead(){
        return (
            <View style={styles.top}>
                <Text style={styles.topText}>开奖记录</Text>
            </View>
        )
    }



    render(){
        return(
            <View style={styles.container}>
                <RefreshList
                    ref={(list)=> this._listRef = list}
                    onPullRelease={(resolve)=> this._onPullRelease(resolve)}
                    ItemHeight={200}
                    onEndReached={()=> this._loadMore()}
                    renderItem={(item)=> this._renderItem(item)}
                    renderMore={false}
                    ListHeaderComponent={()=> this._renderHead()}/>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: 'white',
        marginBottom: Utils.pixToDpSize(150),
        flexDirection: 'column',
    },
    listWrapper:{
        width: '100%',
        flexDirection: 'column',
        height: 200,
        borderTopWidth: 1,
        borderTopColor: Utils.dividerBgColor,
    },
    listItemWrapper:{
        width: '25%',
        justifyContent: 'center',
        alignItems: 'center',
        margin: 10
    },
    listItemCenterText:{
        color: 'red',
        fontSize: 20,
    },
    listItemBottomText:{
        color: Utils.navColor,
    },
    listItemBottomTextRight:{
        color: Utils.navColor,
        marginLeft: 20,
    },
    listItemTop:{
        marginTop: 10,
        marginLeft: 20,
        flexDirection: 'row',
    },
    listItemName:{
        color: 'black',
        fontSize: 20,
    },
    listItemTime:{
        color: Utils.navColor,
        fontSize: 18,
    },
    listItemCenter:{
        borderRadius: 90,
        borderWidth: 1,
        alignItems: 'center',
        justifyContent: 'center',
        borderColor: 'red',
        margin: 10,
        height: 40,
        width: 40,
    },
    listItemCenterWrap:{
        flexDirection: 'row',
        flexWrap: 'wrap',
        padding: 8,
    },
    listItemBottom:{
        flexDirection: 'row',
        paddingLeft: 20,
    },
    top:{
        width: '100%',
        justifyContent: 'center',
        alignItems: 'center',
        height: Utils.actionBar.height,
        backgroundColor: Utils.actionBar.backgroundColor,
    },
    topText:{
        color: 'white',
    }
});