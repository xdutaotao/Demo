/**
 * Created by guzhenfu on 2017/7/16.
 */

const STORE_KEY = 'vuejs-key'

export default{
  getStore() {
    return JSON.parse(window.localStorage.getItem(STORE_KEY) || '[]')
  },
  setStore(data) {
    window.localStorage.setItem(STORE_KEY, JSON.stringify(data))
  }
}


