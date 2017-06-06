
import React from 'react'
import {View} from 'react-native'

import Navigation from './page/Navigation'
import SplashScreen from './page/SplashScreen'
import './common/Storage'
import './common/ToastLog'


export default class MainPage extends React.Component{
    // 构造
      constructor(props) {
        super(props);
        // 初始状态
        this.state = {
            splashed: false,
        };
      }

    /**
     * 设置定时器加载SplashScreen页面
     */
    componentWillMount(){
        this.timer = setTimeout(()=>{
            this.setState({
                splashed: true
            })
        }, 2000);
    }


    /**
     * 离开的时候，清除定时器
     */
    componentWillUnMount(){
        this.timer && clearTimeout(this.timer);
    }


    
    render(){
        if (this.state.splashed){
            return(
                <View style={{flex: 1, justifyContent: 'flex-end'}}>
                    <Navigation navigator={this.props.navigator}/>
                </View>
            );

        }else{
            //启动页
            return(
                <SplashScreen/>
            );
        }
        
    }
}