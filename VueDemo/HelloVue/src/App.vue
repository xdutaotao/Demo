<template>
  <div id="app">
    <h1>{{title}}</h1>
    <input v-model="newData" @keyup.enter="addNew">
    <ul>
      <li v-for="todo in todos"
          :class="{finished: todo.finished}"
          @click="toggleFinished(todo)">
        {{todo.label}}
      </li>
    </ul>
    <component-a dataFromFather="ijjjjjjj"></component-a>
  </div>
</template>

<script>
import Store from './store'
import ComponentA from './components/componentA'

export default {
  data() {
    return {
      title: 'this is a todo list',
      todos: Store.getStore(),
      newData: ''
    }
  },
  methods: {
    toggleFinished: function (todo) {
      todo.finished = !todo.finished
    },
    addNew: function () {
      this.todos.splice(this.todos.length, 0, {
        label: this.newData,
        finished: false
      })
      this.newData = ''
    }
  },
  components: {
    ComponentA
  },
  watch: {
    todos: {
      handler: function (todos) {
        Store.setStore(todos)
      },
      deep: true
    }
  }
}
</script>

<style>

  .finished{
    text-decoration: underline;
  }

#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
