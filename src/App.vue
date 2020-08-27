<template>
  <div id="app">
    <chess-board></chess-board>
  </div>
</template>

<script>
import chessBoard from "@/components/chessBoard";

export default {
  components: {
    chessBoard
  }
}
</script>

<style>
body {
  margin: 0;
  background-color: #333;
}
#app {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
}
</style>
