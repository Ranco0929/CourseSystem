<template>
  <div class="main-area">
    <el-card>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="作业详情" name="first">
          <center>
            <h3>{{ task.title }}</h3>
          </center>
          <el-form>
            <el-form-item v-for="(content, index) in task.content" :key="index" :label="index+1">
              <div v-html="markdown2HTML(content.question)" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm">提交</el-button>
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
              width="150"
            />
            <el-table-column
              prop="createdAt"
              label="提交时间"
              width="120"
            />
            <el-table-column
              prop="updatedAt"
              label="更新时间"
              width="120"
            />
            <el-table-column
              prop="state"
              label="状态"
              width="120"
            />
            <el-table-column
              fixed="right"
              label="操作"
              width="100"
            >
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="gotoCorrect(scope.row)">批改</el-button>
                <el-button type="text" size="small" @click="gotoRedo(scope.row)">打回</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
      <el-tab-pane label="作业分析" name="third">
        <span>作业分析</span>
      </el-tab-pane>
    </el-card>
  </div>
</template>

<script>
import { find } from '@/api/client'

export default {
  name: 'Index',
  data() {
    return {
      taskId: '',
      activeName: 'first',
      task: [],
      submissions: []
    }
  },
  created() {
    if (this.$router.query.taskId !== undefined) {
      this.taskId = this.$router.query.taskId
      this.initTask()
      this.initSubmissions()
    }
  },
  methods: {
    async initTask() {
      const task = await find('task', { data: { taskId: this.taskId }})
      // 获取作业内容
      const questionContent = []
      for (const id in task.data[0].content) {
        const question = this.json2Str((task.data[0].content)[id].question)
        questionContent.push({
          type: (task.data[0].content)[id].type,
          question: question,
          grade: (task.data[0].content)[id].grade
        })
        // 创建对应数目的答案模板
        this.answer.push('')
      }
      // 获取答案内容
      const solutionContent = []
      for (const id in task.data[0].solution) {
        const solution = this.json2Str((task.data[0].solution)[id])
        solutionContent.push(solution)
      }
      this.task = { title: task.data[0].title, content: questionContent, solution: solutionContent }
    },
    async initSubmissions() {
      const submissions = await find('task-submission', { data: { taskId: this.taskId }})
      const submissionsContent = []
      for (const s in submissions.data) {
        const user = await find('user', { data: { userId: s.userId }})
        const userName = user.data[0].name
        const created = s.created
        const updated = s.updated
        let state = ''
        const hasCorrect = await find('task-correction', { data: { userId: s.userId, taskId: this.taskId }})
        if (hasCorrect.data.length === 0) {
          state = '未批改'
        } else {
          state = hasCorrect.data[0].state === '1' ? '已批改' : '已打回'
        }
        submissionsContent.push({
          name: userName,
          created: created,
          updated: updated,
          state: state
        })
      }
      this.submissions = submissionsContent
    },
    gotoCorrect(rowInfo) {
      // TODO 找到rowInfo的具体内容，然后最终赋值给userId
      const userId = rowInfo
      this.$router.push({ path: 'task-correction', query: { taskId: this.taskId, userId: userId }})
    },
    gotoRedo(rowInfo) {
      // TODO
    },
    json2Str(json) {
      let str = ''
      for (const el in json) {
        str += json[el].content
      }
      return str
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
