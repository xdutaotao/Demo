<template >
<div>
    <div v-if="loginType === 'username'" >
        <label>Username</label>
        <input placeholder="Enter your username">
    </div>

    <div v-else>
        <label>Email</label>
        <input placeholder="Enter your email address">
    </div>

    <button @click="toggle">login toggle</button>

</div>

  
</template>

<script>
export default {

  data: function(){
    return {
      loginType: ""
    }
  },

  methods: {
      toggle: function(){
          console.log(this.loginType);
          this.loginType = this.loginType == "" ? "username" : "" ;
      }
  }
}
</script>

<style scoped>
    
</style>