/**
 * Created by guzhenfu on 17/4/9.
 */

import React from 'react'
import {
    StyleSheet,
    View,
    Image,
    Text,
    FlatList,
    TouchableHighlight,
    ActivityIndicator,
    ScrollView} from 'react-native'

import Utils from './../common/theme'
import WebViewPage from './WebViewPage'

let cycleImage = [
    require('./../image/trend1.png') , require('./../image/trend2.png') ,
    require('./../image/trend3.png') , require('./../image/trend4.png') ,
    require('./../image/trend5.png') ,
];

export default class TrendPage extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            index: 0,
        }
    }

    _renderHead(){
        return (
            <View style={styles.top}>
                <Text style={styles.topText}>走势</Text>
            </View>
        )
    }

    _renderWan(){
        this._rendTxtColor();
        this._wanTxt.setNativeProps({
            style:{
                color: 'red',
            }
        });

        this.setState({
            index: 0,
        })
    }

    _renderQian(){
        this._rendTxtColor();
        this._qianTxt.setNativeProps({
            style:{
                color: 'red',
            }
        });

        this.setState({
            index: 1,
        })


    }

    _renderBai(){
        this._rendTxtColor();
        this._baiTxt.setNativeProps({
            style:{
                color: 'red',
            }
        });

        this.setState({
            index: 2,
        })
    }

    _renderShi(){
        this._rendTxtColor();
        this._shiTxt.setNativeProps({
            style:{
                color: 'red',
            }
        });

        this.setState({
            index: 3,
        })
    }

    _renderGe(){
        this._rendTxtColor();
        this._geTxt.setNativeProps({
            style:{
                color: 'red',
            }
        });

        this.setState({
            index: 4,
        })
    }

    _rendTxtColor(){
        this._wanTxt.setNativeProps({
            style:{
                color: 'black',
            }
        });

        this._qianTxt.setNativeProps({
            style:{
                color: 'black',
            }
        });

        this._baiTxt.setNativeProps({
            style:{
                color: 'black',
            }
        });

        this._shiTxt.setNativeProps({
            style:{
                color: 'black',
            }
        });

        this._geTxt.setNativeProps({
            style:{
                color: 'black',
            }
        });
    }


    _renderType(){
        return (
            <View style={styles.typeWrap}>
                <TouchableHighlight
                    underlayColor={Utils.underClickColor}
                    onPress={()=> this._renderWan()}>
                    <View style={styles.typeItem}>
                        <Text style={[styles.listItemCenterText, {color: 'red'}]}
                              ref={(c) => this._wanTxt = c}>{'万'}</Text>
                    </View>
                </TouchableHighlight>

                <TouchableHighlight
                    underlayColor={Utils.underClickColor}
                    onPress={()=> {this._renderQian()}}>
                    <View style={styles.typeItem}>
                        <Text style={styles.listItemCenterText}
                              ref={(c) => this._qianTxt = c}>{'千'}</Text>
                    </View>
                </TouchableHighlight>

                <TouchableHighlight
                    underlayColor={Utils.underClickColor}
                    onPress={()=> {this._renderBai()}}>

                    <View style={styles.typeItem}>
                        <Text style={styles.listItemCenterText}
                              ref={(c) => this._baiTxt = c}>{'百'}</Text>
                    </View>
                 </TouchableHighlight>

                <TouchableHighlight
                    underlayColor={Utils.underClickColor}
                    onPress={()=> {this._renderShi()}}>
                    <View style={styles.typeItem}>
                        <Text style={styles.listItemCenterText}
                              ref={(c) => this._shiTxt = c}>{'十'}</Text>
                    </View>
                </TouchableHighlight>

                <TouchableHighlight
                    underlayColor={Utils.underClickColor}
                    onPress={()=> {this._renderGe()}}>
                    <View style={styles.typeItem}>
                        <Text style={styles.listItemCenterText}
                              ref={(c) => this._geTxt = c}>{'个'}</Text>
                    </View>
                </TouchableHighlight>

            </View>
        )
    }



    render(){
        return(
            <View style={styles.container}>
                {this._renderHead()}
                {this._renderType()}

                <Image source={cycleImage[this.state.index]}
                    resizeMode="repeat"/>
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
    typeWrap: {
        flexDirection: 'row',
        justifyContent: 'center'
    },
    typeItem:{
        borderRadius: 10,
        borderWidth: 1,
        alignItems: 'center',
        justifyContent: 'center',
        borderColor: 'white',
        margin: 10,
        paddingTop: 10,
        paddingBottom: 10,
        paddingLeft: 20,
        paddingRight: 20,
        backgroundColor: Utils.dividerBgColor,
    },
    listItemCenterText:{

    }
});