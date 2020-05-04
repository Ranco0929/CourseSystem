<template>
  <div>
    <el-row>
      <el-col v-for="t in tasks" :key="t.taskId" :span="6">
        <el-card style="margin: 20px;height: 200px">
          <div slot="header" class="clearfix">
            <span>{{ t.title }}</span>
          </div>
          <div>
            <span style="font-size: smaller">简介：这次是某次作业简介</span>
            <br>
            <br>
            <span style="font-weight: lighter">截止日期：{{ t.deadline }}</span>
          </div>
          <el-button
            style="float: right; padding: 3px 0"
            type="primary"
            @click="gotoTaskDetail(t)"
          >{{ t.state === '0'? '未发布':'已发布' }}</el-button>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { find } from '@/api/client'

export default {
  name: 'Index',
  data() {
    return {
      courseId: '',
      info: '',
      teachers: [],
      activeName: 'first',
      tasks: [
        { taskId: 1, title: '第一次作业', deadline: '2019-03-08', state: '1' },
        { taskId: 2, title: '第二次作业', deadline: '2019-03-08', state: '0' },
        { taskId: 3, title: '第三次作业', deadline: '2019-03-08', state: '0' },
        { taskId: 4, title: '第四次作业', deadline: '2019-03-08', state: '0' },
        { taskId: 5, title: '第五次作业', deadline: '2019-03-08', state: '0' },
        { taskId: 6, title: '第六次作业', deadline: '2019-03-08', state: '0' }
      ],
      tableData: [{
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-04',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1517 弄'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1519 弄'
      }, {
        date: '2016-05-03',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1516 弄'
      }]
    }
  },
  created() {
    if (this.$route.query.courseId !== undefined) {
      this.courseId = this.$route.query.courseId
    }
  },
  methods: {
    async getTasks() {
      if (this.courseId !== undefined) {
        // 获取作业
        const res = await find('task', { data: { courseId: this.courseId, state: 1 }})
        this.tasks = res.data
      }
    },
    gotoTaskDetail(task) {
    },
    gotoTaskRelease() {
      this.$router.push({ path: 'taskRelease', query: { courseId: this.courseId }})
    }
  }
}
</script>
<style scoped>
  .text {
    font-size: 14px;
  }
  .item {
    margin-bottom: 18px;
  }
  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }
  .box-card {
    width: 480px;
  }
</style>
