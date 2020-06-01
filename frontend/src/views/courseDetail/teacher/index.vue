<template>
  <div>
    <el-tabs v-model="activeName">
      <el-tab-pane label="作业" name="first">
        <el-row>
          <el-col v-for="(t, index) in tasks" :key="t.taskId" :span="6">
            <el-card style="margin: 20px;height: 200px">
              <div slot="header" class="clearfix" style="font-weight: bold">{{ t.title }}</div>
              <div style="font-weight: lighter;font-size: small;margin: 10px">开始日期：{{ t.createdAt }}</div>
              <div style="font-weight: lighter;font-size: small;margin: 10px">截止日期：{{ t.deadline }}</div>
              <div style="font-weight: bold;font-size: small;margin: 10px">
                状态：{{ t.state === '0'? '未发布':'已发布' }}
              </div>
              <el-button
                v-if="t.state === '1'"
                style="float: right; padding: 3px 0"
                type="danger"
                @click="deleteTask(t, index)"
              >删除</el-button>
              <el-button
                style="float: right; padding: 3px 0"
                type="primary"
                @click="gotoTaskDetail(t)"
              >查看</el-button>
            </el-card>
          </el-col>
          <el-col :span="6">
            <img
              src="http://pic.51yuansu.com/pic3/cover/03/04/69/5af7f7bb54447_610.jpg"
              style="padding: 20px;height: 200px;width: 200px"
              @click="gotoTaskRelease"
            >
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="学生名单" name="second">
        <el-table
          :data="students"
          style="width: 100%"
        >
          <el-table-column
            prop="name"
            label="姓名"
            width="180"
          />
          <el-table-column
            prop="info"
            label="简介"
          />
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { get, post } from '@/api/client'

export default {
  name: 'Index',
  data() {
    return {
      courseId: '',
      info: '',
      teachers: [],
      activeName: 'first',
      tasks: [],
      students: []
    }
  },
  created() {
    if (this.$route.query.courseId !== undefined) {
      this.courseId = this.$route.query.courseId
      this.getTasks()
      this.getStudents()
    }
  },
  methods: {
    getTasks() {
      if (this.courseId !== undefined) {
        // 获取作业
        get('task/get_tasks', {
          courseId: this.courseId,
          userId: this.$store.getters.userId
        }).then(res => {
          this.tasks = res.data
        }).catch(err => {
          this.$message.error(err)
        })
      }
    },
    getStudents() {
      if (this.courseId !== undefined) {
        // 获取学生名单
        get('course/get_students', {
          courseId: this.courseId
        }).then(res => {
          this.students = res.data
        })
      }
    },
    deleteTask(task, index) {
      post('task/delete_task', {
        courseId: this.courseId,
        taskId: task.taskId
      }).then(_ => {
        this.getTasks()
      }).catch(err => {
        this.$message.error(err)
      })
    },
    gotoTaskDetail(task) {
      this.$router.push({ path: 'taskDetail', query: { taskId: task.taskId }})
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
