<template>
  <div class="main-area">
    <el-card>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="作业详情" name="first">
          <center>
            <h3>{{ task.title }}</h3>
            <h5>{{ '截止时间' + task.deadline }}</h5>
          </center>
          <el-form>
            <el-form-item v-for="(content, index) in task.content" :key="index" :label="index+1+''">
              <div v-html="markdown2HTML(content.question)" />
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="提交情况" name="second">
          <el-table
            :data="submissions"
            border
            style="width: 100%"
          >
            <el-table-column
              prop="name"
              label="姓名"
              align="center"
            />
            <el-table-column
              prop="createdAt"
              label="提交时间"
              align="center"
            />
            <el-table-column
              prop="updatedAt"
              label="更新时间"
              align="center"
            />
            <el-table-column
              prop="state"
              label="状态"
              align="center"
            />
            <el-table-column
              fixed="right"
              label="操作"
              align="center"
            >
              <template slot-scope="scope">
                <el-button :disabled="scope.row.state === '已批改'" type="text" size="small" @click="gotoCorrect(scope.row)">批改</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="作业分析" name="third">
          <div style="width: 100%;height: 400px;">
            <chart :option="accuracy" />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { get } from '@/api/client'
import marked from 'marked'
import chart from '../chart/chart'

export default {
  name: 'Index',
  components: {
    chart
  },
  data() {
    return {
      taskId: '',
      activeName: 'first',
      task: [],
      submissions: [],
      accuracy: {
        xAxis: {
          type: 'category',
          data: []
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: [],
          type: 'bar',
          showBackground: true,
          backgroundStyle: {
            color: 'rgba(220, 220, 220, 0.8)'
          }
        }]
      }
    }
  },
  created() {
    if (this.$router.currentRoute.query.taskId !== undefined) {
      this.taskId = this.$router.currentRoute.query.taskId
      this.init().then(_ => {
        this.initSubmissions().catch(err => {
          this.$message.error('加载失败：' + err)
        })
      }).catch(err => {
        this.$message.error('加载失败：' + err)
      })
      this.setTaskAnalysis()
    }
  },
  methods: {
    async init() {
      const task = await get('task/get_task', { taskId: this.taskId })
      // 获取作业内容
      const questionContent = []
      for (const id in task.data.content) {
        const question = this.json2Str((task.data.content)[id].question)
        questionContent.push({
          type: (task.data.content)[id].type,
          question: question,
          grade: (task.data.content)[id].grade
        })
      }
      // 获取答案内容
      const solutionContent = []
      for (const id in task.data.solution) {
        const solution = this.json2Str((task.data.solution)[id])
        solutionContent.push(solution)
      }
      this.task = {
        createAt: task.data.createdAt,
        deadline: task.data.deadline,
        solution: solutionContent,
        title: task.data.title,
        content: questionContent,
        updatedAt: task.data.updatedAt
      }
    },
    async initSubmissions() {
      const sbs = await get('task/get_submissions', { taskId: this.taskId })
      for (const sb of sbs.data) {
        sb.state = sb.state === '0' ? '未批改' : (sb.state === '1' ? '已批改' : '打回')
      }
      this.submissions = sbs.data
    },
    setTaskAnalysis() {
      get('task/get_task_analysis', {
        taskId: this.taskId
      }).then(res => {
        this.setTaskAccuracy(res.data)
        this.setTaskSimilarity(res.data)
      }).catch(err => {
        this.$message.error(err)
      })
    },
    setTaskAccuracy(data) {
      if (!data || !data.accuracy) return

      for (const key in data.accuracy) {
        this.accuracy.xAxis.data.push(key)
        this.accuracy.series[0].data.push(data.accuracy[key])
      }
    },
    setTaskSimilarity(data) {
      if (!data || !data.similarity) return
      // TODO
    },
    gotoCorrect(rowInfo) {
      this.$router.push({ path: 'taskCorrection', query: { taskId: this.taskId, userId: rowInfo.userId }})
    },
    json2Str(json) {
      let str = ''
      for (const el in json) {
        str += json[el].content
      }
      return str
    },
    markdown2HTML(md) {
      return marked(md)
    },
    handleClick(tab, event) {
      // if(tab.name === '作业分析') this.getTaskAnalysis()
    }
  }
}
</script>

<style scoped>
  .main-area{
    padding: 20px;
    height: 100%;
  }
</style>
