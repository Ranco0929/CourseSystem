<template>
  <div id="app">
    <router-view :key="new Date()" />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>
