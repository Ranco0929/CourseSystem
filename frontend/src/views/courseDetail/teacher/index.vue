<template>
  <div>
    <el-tabs v-model="activeName">
      <el-tab-pane label="作业" name="first">
        <div style="height: 100%">
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
                  v-if="t.state === '1'"
                  style="float: right; padding: 3px 0"
                  type="danger"
                  @click="deleteTask(t)"
                >删除</el-button>
                <el-button
                  style="float: right; padding: 3px 0"
                  type="primary"
                  @click="gotoTaskDetail(t)"
                >{{ t.state === '0'? '未发布':'已发布' }}</el-button>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-image
                src="http://pic.51yuansu.com/pic3/cover/03/04/69/5af7f7bb54447_610.jpg"
                fit="cover"
                style="padding: 20px;height: 200px;width: 200px"
                @click="gotoTaskRelease"
              />
            </el-col>
          </el-row>
        </div>
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
        { taskId: 6, title: '第六次作业', deadline: '2019-03-08', state: '0' },
        { taskId: 7, title: '第六次作业', deadline: '2019-03-08', state: '0' },
        { taskId: 8, title: '第六次作业', deadline: '2019-03-08', state: '0' },
        { taskId: 9, title: '第六次作业', deadline: '2019-03-08', state: '0' }
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
    deleteTask(task) {
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
