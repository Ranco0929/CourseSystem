<template>
  <div :id="uuid" style="width: 100%;height: 100%" />
</template>

<script>
import echarts from 'echarts'
import uuidv1 from 'uuid/v4'
export default {
  name: 'Chart',
  props: {
    // 父组件需要传递的参数：option
    option: {
      type: Object,
      // Object类型的prop值一定要用函数return出来，不然会报错。原理和data是一样的，
      // 使用闭包保证一个vue实例拥有自己的一份option
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      uuid: '',
      chart: '',
      // 表格文档索引
      option_reference: 'https://www.echartsjs.com/option.html'
    }
  },
  computed: {
    style() {
      return {
        height: this.height,
        width: this.width
      }
    }
  },
  // 观察option的变化
  watch: {
    option: {
      handler(newOption, oldOption) {
        if (this.chart) {
          if (newOption !== oldOption) {
            this.chart.setOption(newOption)
          } else {
            this.chart.setOption(oldOption)
          }
        } else {
          this.init()
        }
      },
      // 对象内部属性的监听
      deep: true
    }
  },
  created() {
    this.uuid = uuidv1()
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      try {
        this.chart = echarts.init(document.getElementById(this.uuid))
        this.chart.setOption(this.option)
        window.addEventListener('resize', this.chart.resize)
      } catch (e) {
        console.log(e)
      }
    }
  }
}
</script>
