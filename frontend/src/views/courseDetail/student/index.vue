<template>
  <div>
    <el-row>
      <el-col v-for="t in tasks" :key="t.taskId" :span="6">
        <el-card style="margin: 20px;height: 200px">
          <div slot="header" class="clearfix">
            <span style="font-weight: bold">{{ t.title }}</span>
          </div>
          <div>
            <div style="font-weight: lighter;font-size: smaller;margin-bottom: 20px">简介：这次是某次作业简介</div>
            <div style="font-weight: lighter;margin-bottom: 20px">截止日期：{{ t.deadline }}</div>
          </div>
          <el-button
            style="float: right; padding: 3px 0"
            type="primary"
            :disabled="new Date(t.deadline) > new Date()"
            @click="gotoTaskSubmission(t)"
          >{{ t.submissionState === '0' ? '未完成' : (t.submissionState === '1' ? '已完成' : '打回重做') }}</el-button>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { get } from '@/api/client'

export default {
  name: 'Index',
  data() {
    return {
      courseId: '',
      tasks: []
    }
  },
  created() {
    if (this.$route.query.courseId !== undefined) {
      this.courseId = this.$route.query.courseId
      get('task/get_tasks', {
        courseId: this.courseId,
        userId: this.$store.getters.userId
      }).then(res => {
        console.log('res', res)
        this.tasks = res.data
      })
    }
  },
  methods: {
    gotoTaskSubmission(task) {
      this.$router.push({
        path: 'taskSubmission',
        query: {
          taskId: task.taskId,
          userId: this.$store.getters.userId,
          state: task.state,
          courseId: this.courseId
        }
      })
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
