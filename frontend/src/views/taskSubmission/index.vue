<template>
  <div class="main-area">
    <el-card>
      <center>
        <h3>{{ task.title }}</h3>
      </center>
      <el-form>
        <el-form-item v-for="(content, index) in task.content" :key="index" :label="index+1+''">
          <div v-html="markdown2HTML(content.question)" />
          <div v-if="state === '1'">
            <el-input v-if="content.type === '0' || content.type === '1'" v-model="answer[index]" placeholder="请输入正确选项" />
            <el-input v-if="content.type === '2'" v-model="answer[index]" placeholder="请输入正确答案" />
            <el-input v-if="content.type === '3'" v-model="answer[index]" placeholder="请输入对或错" />
            <markdown-editor v-if="content.type === '4'" v-model="answer[index]" />
            <markdown-editor v-if="content.type === '5'" v-model="answer[index]" />
          </div>
          <div v-else v-html="markdown2HTML(task.solution[index])" />
        </el-form-item>
        <el-form-item>
          <el-button :disabled="state === '0'" type="primary" @click="submitForm">提交</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { get, post } from '@/api/client'
import marked from 'marked'

export default {
  name: 'Index',
  data() {
    return {
      taskId: '',
      userId: '',
      courseId: '',
      state: '',
      task: '',
      answer: []
    }
  },
  created() {
    if (this.$router.currentRoute.query.taskId !== undefined &&
      this.$router.currentRoute.query.userId !== undefined &&
      this.$router.currentRoute.query.courseId !== undefined &&
      this.$router.currentRoute.query.state !== undefined) {
      this.taskId = this.$router.currentRoute.query.taskId
      this.userId = this.$router.currentRoute.query.userId
      this.courseId = this.$router.currentRoute.query.courseId
      this.state = this.$router.currentRoute.query.state
      this.init()
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
        // 创建对应数目的答案模板
        this.answer.push('')
      }
      // 获取答案内容
      const solutionContent = []
      for (const id in task.data.solution) {
        const solution = this.json2Str((task.data.solution)[id])
        solutionContent.push(solution)
      }

      // 写完了，截至日期到了
      if (this.state === '0') {
        this.task = { title: task.data.title, content: questionContent }
      } else {
        this.task = { title: task.data.title, content: questionContent, solution: solutionContent }
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
      ans['state'] = '1'
      ans['answer'] = {}
      for (let i = 0; i < this.answer.length; ++i) {
        ans['answer']['' + i] = this.str2Json(this.answer[i])
      }
      post('task/submit_task', ans)
        .then(_ => {
          this.$message.success('作业提交成功')
          this.$router.go(0)
        })
        .catch(err => {
          this.$message.error(err)
        })
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
