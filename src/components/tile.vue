<template>
</template>

<script>
export default {
  data: function () {
    return {
    }
  },
  props: {
    figure: "",
    color: "",
    highlighted: false,
    selected: false,

  },
  mounted() {
    this.figure = this.figure.toLowerCase();
  }
}
</script>

<style>

</style>
