/**
 * Created by guzhenfu on 17/5/6.
 */

import React from 'react'
import {
    StyleSheet,
    View,
    Text,
    Image,
    FlatList,
    TouchableHighlight,
    ActivityIndicator,
    ScrollView} from 'react-native'

import Utils from './../common/theme'

const preData = [
    {name: '时时彩', time: '- 第2017055期',
        number: ['07', '12', '13', '20', '24', '31', '05'],
        date: '2017-06-12 21:15:00', money: '奖池:685895820元'},
    {name: '大乐透', time: '- 第2017055期',
        number: ['16', '29', '30', '32', '33', '04', '05'],
        date: '2017-06-11 21:15:00', money: '奖池:5895820元'},
    {name: '3D', time: '- 第2017055期',
        number: ['6', '2', '3', '20', '24'],
        date: '2017-06-10 21:15:00', money: '奖池:895820元'},
    {name: '七星彩', time: '- 第2017055期',
        number: ['06', '12', '13', '20', '24', '31', '05'],
        date: '2017-06-09 21:15:00', money: '奖池:685895820元'},
    {name: '七乐彩', time: '- 第2017055期',
        number: ['08', '4', '16', '20', '24', '31', '05'],
        date: '2017-06-08 21:15:00', money: '奖池:195820元'},
    {name: '3', time: '- 第2017055期',
        number: ['07', '15', '13', '20', '24', '31', '05'],
        date: '2017-06-07 21:15:00', money: '奖池:2895820元'},
    {name: '时时彩', time: '- 第2017055期',
        number: [ '20', '24', '31', '05'],
        date: '2017-06-06 21:15:00', money: '奖池:8858920元'},
    {name: '大乐透', time: '- 第2017055期',
        number: ['07', '12', '13', '83', '24', '31', '05'],
        date: '2017-06-05 21:15:00', money: '奖池:123895820元'},
    {name: '3D', time: '- 第2017055期',
        number: ['07', '44', '13', '20', '24', '31', '05'],
        date: '2017-06-04 21:15:00', money: '奖池:665895820元'},
    {name: '双色球', time: '- 第2017055期',
        number: ['07', '12', '13', '55', '05'],
        date: '2017-06-03 21:15:00', money: '奖池:26795820元'},
];



export default class OrderPage extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            data: preData,
        };
    }


    /**
     * 点击事件
     * @param item
     * @private
     */
    _onItemPress(item){
        //this.props.navigator.push({component: LoginPage})
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
                <Text style={styles.topText}>历史记录</Text>
            </View>
        )
    }


    _renderListHead(){
        return (
            <View style={styles.listHeader}>
                <Image
                    source={require('./../image/topBanner.png')}
                    resizeMode='stretch'
                    style={{height: 100, width: '100%'}}/>

                <View style={styles.lotteryStyle}>
                    <View style={styles.styleLine}>

                    </View>

                    <Text style={styles.styleText}>
                        历史开奖记录
                    </Text>

                    <View style={styles.styleLine}>

                    </View>
                </View>

            </View>
        )
    }

    _separator(){
        return <View style={{height:1,backgroundColor:Utils.dividerBgColor}}/>;
    }

    render(){
        return(
            <View style={styles.container}>
                {this._renderHead()}

                <FlatList
                    ref={(list)=> this._listRef = list}
                    renderItem={(item) => this._renderItem(item)}
                    ListHeaderComponent = {()=> this._renderListHead()}
                    ItemSeparatorComponent={()=> this._separator()}
                    data={preData}
                    keyExtractor={(item, index) => {return index}}/>
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
        height: 80,
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
        marginTop: 20,
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
    },
    listHeader:{
        flexDirection: 'column',
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
});