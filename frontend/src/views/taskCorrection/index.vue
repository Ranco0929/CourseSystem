<template>
  <div class="main-area">
    <el-card>
      <center>
        <h3>{{ task.title }}</h3>
      </center>
      <el-form v-model="correction">
        <el-form-item v-for="(question, index) in task.content" :key="index">
          <div v-html="markdown2HTML(question.question)" />
          <div v-html="markdown2HTML(submission[index])" />
          <div v-html="markdown2HTML(question.solution)" />
          <el-form v-model="correction[index]">
            <el-form-item :label="'给分'" :rules="[{ required: true, message: '打分不能为空', trigger: 'blur' }]">
              <el-input v-model="correction.grade" placeholder="'请打分'" />
            </el-form-item>
            <el-form-item :label="'批语'">
              <el-input v-model="correction.extra" placeholder="'请写批语'" />
            </el-form-item>
          </el-form>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm">提交</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import marked from 'marked'
import { create } from '@/api/client'

export default {
  name: 'Index',
  data() {
    return {
      taskId: '',
      userId: '',
      task: [],
      submission: [],
      correction: []
    }
  },
  created() {
    if (this.$router.query.taskId !== undefined && this.$router.query.userId !== undefined) {
      this.taskId = this.$router.query.taskId
      this.userId = this.$router.query.userId
      this.initTask()
      this.initSubmission()
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
        this.correction.push({
          grade: '',
          extra: ''
        })
      }
      // 获取答案内容
      const solutionContent = []
      for (const id in task.data[0].solution) {
        const solution = this.json2Str((task.data[0].solution)[id])
        solutionContent.push(solution)
      }
      // TODO 作业的状态还没有完全规定好
      this.task = { title: task.data[0].title, content: questionContent, solution: solutionContent }
    },
    async initSubmission() {
      const submission = await find('task-submission', { data: { taskId: this.taskId, userId: this.userId }})
      const submissionContent = []
      for (const id of submission.answer) {
        const ans = this.json2Str(submission.answer[id])
        submissionContent.push(ans)
      }
      this.submission = submissionContent
    },
    submitForm() {
      const content = {}
      for (let i = 0; i < this.correction.length; ++i) {
        content['' + i] = this.correction[i]
      }
      const form = {
        taskId: this.taskId,
        userId: this.userId,
        content: content
      }
      create('task-correction', {
        data: form
      }).then(res => {
        // 成功
        this.$message({
          message: '提交成功',
          type: 'success',
          duration: 2000
        })
        this.$router.back()
      }).catch(err => {
        // 失败
        this.$message({
          message: '提交失败' + err,
          type: 'error',
          duration: 2000
        })
      })
    },
    json2Str(json) {
      let str = ''
      for (const el in json) {
        str += json[el].content
      }
      return str
    },
    str2Json(str) {
      if (str === undefined || str === null || str.length === 0) return {}

      let pre = 0
      let cur = 0
      let cnt = 0
      const json = {}
      while (cur < str.length) {
        if (str[cur] === '!') {
          // text型提取
          json['' + cnt] = {
            type: 'text',
            content: str.substring(pre, cur)
          }
          cnt++
          pre = cur
          // image型提取 TODO
        }
        cur++
      }
      return json
    },
    markdown2HTML(md) {
      return marked(md)
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
