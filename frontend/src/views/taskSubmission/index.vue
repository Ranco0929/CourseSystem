<template>
  <div class="main-area">
    <el-card>
      <center>
        <h3>{{ task.title }}</h3>
      </center>
      <el-form>
        <el-form-item v-for="(content, index) in task.content" :key="index" :label="index+1">
          <div v-html="markdown2HTML(content.question)" />
          <el-input v-if="content.type === '单选题'" v-model="answer[index]" placeholder="请输入正确选项" />
          <el-input v-if="content.type === '多选题'" v-model="answer[index]" placeholder="请输入正确选项" />
          <el-input v-if="content.type === '填空题'" v-model="answer[index]" placeholder="请输入正确答案" />
          <el-input v-if="content.type === '判断题'" v-model="answer[index]" placeholder="请输入对或错" />
          <markdown-editor v-if="content.type === '简答题'" v-model="answer[index]" />
          <markdown-editor v-if="content.type === '应用题'" v-model="answer[index]" />
          <div v-if="state === '1'" v-html="markdown2HTML(task.solution[index])" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm">提交</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { find } from '@/api/client'
import marked from 'marked'

export default {
  name: 'Index',
  data() {
    return {
      taskId: '',
      userId: '',
      state: '',
      task: '',
      answer: []
    }
  },
  created() {
    if (this.$router.query.taskId !== undefined &&
      this.$router.query.userId !== undefined &&
      this.$router.query.state !== undefined) {
      this.taskId = this.$router.query.taskId
      this.userId = this.$router.query.userId
      this.state = this.$router.query.state
      this.initTask()
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
      // TODO 作业的状态还没有完全规定好
      // 写完了，截至日期到了
      if (this.state === '1') {
        this.task = { title: task.data[0].title, content: questionContent, solution: solutionContent }
      } else {
        this.task = { title: task.data[0].title, content: questionContent }
      }
    },
    json2Str(json) {
      let str = ''
      for (const el in json) {
        str += json[el].content
      }
      return str
    },
    submitForm() {
      const ans = {}
      ans['taskId'] = this.taskId
      ans['userId'] = this.userId
      for (let i = 0; i < this.answer.length; ++i) {
        ans['answer']['' + i] = this.str2Json(this.answer[i])
      }
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
