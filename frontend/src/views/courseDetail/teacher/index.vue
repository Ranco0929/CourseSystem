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
          :data="tableData"
          style="width: 100%"
        >
          <el-table-column
            prop="date"
            label="日期"
            width="180"
          />
          <el-table-column
            prop="name"
            label="姓名"
            width="180"
          />
          <el-table-column
            prop="address"
            label="地址"
          />
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { find, remove } from '@/api/client'

export default {
  name: 'Index',
  data() {
    return {
      courseId: '',
      info: '',
      teachers: [],
      activeName: 'first',
      tasks: [
        { taskId: 1, title: '第一次作业', deadline: '2019-03-08', state: '1', createdAt: '2019-03-01' },
        { taskId: 2, title: '第二次作业', deadline: '2019-03-08', state: '0', createdAt: '2019-03-01' },
        { taskId: 3, title: '第三次作业', deadline: '2019-03-08', state: '0', createdAt: '2019-03-01' },
        { taskId: 4, title: '第四次作业', deadline: '2019-03-08', state: '0', createdAt: '2019-03-01' },
        { taskId: 5, title: '第五次作业', deadline: '2019-03-08', state: '0', createdAt: '2019-03-01' },
        { taskId: 6, title: '第六次作业', deadline: '2019-03-08', state: '0', createdAt: '2019-03-01' },
        { taskId: 7, title: '第六次作业', deadline: '2019-03-08', state: '0', createdAt: '2019-03-01' },
        { taskId: 8, title: '第六次作业', deadline: '2019-03-08', state: '0', createdAt: '2019-03-01' },
        { taskId: 9, title: '第六次作业', deadline: '2019-03-08', state: '0', createdAt: '2019-03-01' }
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
    deleteTask(task, index) {
      remove('task', {
        data: {
          taskId: task.taskId
        }
      }).then(res => {
        this.tasks.splice(index, 1)
        this.$message.success('删除作业成功')
      }).catch(err => {
        this.$message.error('删除作业失败' + err)
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
