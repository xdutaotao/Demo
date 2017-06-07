/**
 * Created by guzhenfu on 17/4/9.
 */

import React from 'react'
import {
    Navigator,
    StyleSheet,
    Image,
    View,
    ToastAndroid} from 'react-native'
import TabNavigator from 'react-native-tab-navigator';
import Utils from '../common/theme'
import HomePage from './HomePage'
import MallPage from './MallPage'

const tabNames = ['home', 'mall', 'order','info'];

export default class Navigation extends React.Component{
    static defaultProps = {
        selectedColor: 'red',
        normalColor: '#a9a9a9'
    };

    constructor(props){
        super(props);
        this.state = {
            selectedTab: tabNames[0],
            tabName: ['首页','开奖','历史','走势']
        }
    }

    componentWillMount() {
        const {selectedColor, normalColor} = this.props;
    }

    render(){
        const {selectedColor} = this.props;

        return(
            <TabNavigator
                hidesTabTouch={true}
                tabBarStyle={styles.tabBar}
                sceneStyle={{ paddingBottom: styles.tabBar.height }}>
                <TabNavigator.Item
                    tabStyle={styles.tabStyle}
                    title={this.state.tabName[0]}
                    selected={this.state.selectedTab === tabNames[0]}
                    selectedTitleStyle={{color: selectedColor}}
                    renderIcon={() => <Image style={styles.tab} source={require('./../image/Homea@2x.png')} />}
                    renderSelectedIcon={() => <Image style={styles.tab} source={require('./../image/Homeb@2x.png')} />}
                    onPress={() => this.setState({ selectedTab: tabNames[0] })}>
                    {<HomePage navigator={this.props.navigator}/>}
                </TabNavigator.Item>
                <TabNavigator.Item
                    tabStyle={styles.tabStyle}
                    title={this.state.tabName[1]}
                    selected={this.state.selectedTab === tabNames[1]}
                    selectedTitleStyle={{color: selectedColor}}
                    renderIcon={() => <Image style={styles.tab} source={require('./../image/Homea@2x.png')} />}
                    renderSelectedIcon={() => <Image style={styles.tab} source={require('./../image/Homeb@2x.png')} />}
                    onPress={() => this.setState({ selectedTab: tabNames[1] })}>
                    {<MallPage navigator={this.props.navigator}/>}
                </TabNavigator.Item>
                <TabNavigator.Item
                    tabStyle={styles.tabStyle}
                    title={this.state.tabName[2]}
                    selected={this.state.selectedTab === tabNames[2]}
                    selectedTitleStyle={{color: selectedColor}}
                    renderIcon={() => <Image style={styles.tab} source={require('./../image/Homea@2x.png')} />}
                    renderSelectedIcon={() => <Image style={styles.tab} source={require('./../image/Homeb@2x.png')} />}
                    onPress={() => this.setState({ selectedTab: tabNames[2] })}>
                    {<HomePage navigator={this.props.navigator}/>}
                </TabNavigator.Item>
                <TabNavigator.Item
                    tabStyle={styles.tabStyle}
                    title={this.state.tabName[3]}
                    selected={this.state.selectedTab === tabNames[3]}
                    selectedTitleStyle={{color: selectedColor}}
                    renderIcon={() => <Image style={styles.tab} source={require('./../image/Homea@2x.png')} />}
                    renderSelectedIcon={() => <Image style={styles.tab} source={require('./../image/Homeb@2x.png')} />}
                    onPress={() => this.setState({ selectedTab: tabNames[3] })}>
                    {<HomePage navigator={this.props.navigator}/>}
                </TabNavigator.Item>
            </TabNavigator>
        );
    }
}

const styles = StyleSheet.create({
    tabStyle:{
        padding: Utils.pixToDpSize(5)
    },
    tabBar:{
        height: Utils.pixToDpSize(150),
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: '#fff',
    },
    tab:{
        width: Utils.pixToDpSize(50),
        height: Utils.pixToDpSize(50)
    }
});