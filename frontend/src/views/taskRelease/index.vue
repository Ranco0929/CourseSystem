<template>
  <div class="main-area">
    <el-card v-if="courseId !== ''" style="height: 100%">
      <el-form :rules="rules">
        <el-form-item :label="'作业标题'" prop="title">
          <el-input v-model="task.title" placeholder="请输入作业标题" style="width: 220px" />
        </el-form-item>
        <el-form-item :label="'截止时间'" prop="deadline">
          <el-date-picker
            v-model="task.deadline"
            type="date"
            placeholder="选择日期"
          />
        </el-form-item>
        <el-form-item v-for="(question, index) in task.content" :key="index" :label="index + '、'">
          <div v-html="markdown2HTML(question.question)" />
          <el-button @click.prevent="editQuestion(index, question)">编辑</el-button>
          <el-button @click.prevent="removeQuestion(index)">删除</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm">提交</el-button>
          <el-button @click="popEditor = true">添加题目</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-dialog :visible.sync="popEditor" :before-close="handleClose">
      <el-form v-model="editingQuestion">
        <el-form-item :label="'题目类型'">
          <el-select v-model="editingQuestion.type" placeholder="请选择题目类型">
            <el-option
              v-for="item in questionType"
              :key="item.key"
              :label="item.label"
              :value="item.label"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="'题目内容'">
          <markdown-editor v-model="editingQuestion.question" />
        </el-form-item>
        <el-form-item :label="'题目答案'">
          <el-input v-if="editingQuestion.type === '单选题'" v-model="editingSolution" placeholder="请输入正确选项" />
          <el-input v-if="editingQuestion.type === '多选题'" v-model="editingSolution" placeholder="请输入正确选项" />
          <el-input v-if="editingQuestion.type === '填空题'" v-model="editingSolution" placeholder="请输入正确答案" />
          <el-input v-if="editingQuestion.type === '判断题'" v-model="editingSolution" placeholder="请输入对或错" />
          <markdown-editor v-if="editingQuestion.type === '简答题'" v-model="editingSolution" placeholder="请输入答案" />
          <markdown-editor v-if="editingQuestion.type === '应用题'" v-model="editingSolution" placeholder="请输入答案" />
        </el-form-item>
        <el-form-item :label="'题目分值'">
          <el-input v-model="editingQuestion.grade" placeholder="请输入分值" style="width: 100px" />
        </el-form-item>
      </el-form>
      <el-button type="primary" @click="confirmAddQuestion">确认</el-button>
      <el-button type="danger" @click="popEditor = false">取消</el-button>
    </el-dialog>
  </div>
</template>

<script>
import markdownEditor from '@/components/MarkdownEditor'
import marked from 'marked'
import { create } from '@/api/client'

const questionTemplate = {
  type: '',
  question: '',
  grade: ''
}

const solutionTemplate = ''

export default {
  name: 'Index',
  components: {
    markdownEditor
  },
  data() {
    return {
      courseId: '',
      task: {
        title: '',
        content: [],
        solution: [],
        state: '0'
      },
      questionType: [
        {
          key: '0',
          label: '单选题'
        },
        {
          key: '1',
          label: '多选题'
        },
        {
          key: '2',
          label: '填空题'
        },
        {
          key: '3',
          label: '判断题'
        },
        {
          key: '4',
          label: '简答题'
        },
        {
          key: '5',
          label: '应用题'
        }
      ],
      selectType: '',
      editingQuestion: questionTemplate,
      editingSolution: solutionTemplate,
      editingIndex: -1,
      popEditor: false,
      rules: {
        title: [{ required: true, message: '请输入作业标题', trigger: 'blur' }],
        deadline: [{ required: true, message: '请输入截止时间', trigger: 'blur' }]
      }
    }
  },
  created() {
    if (this.$router.currentRoute.query.courseId !== undefined) {
      this.courseId = this.$router.currentRoute.query.courseId
    }
  },
  methods: {
    submitForm() {
      const content = {}
      const solution = {}
      for (let i = 0; i < this.task.content; ++i) {
        content['' + i] = this.task.content[i]
        solution['' + i] = this.task.solution[i]
      }
      const form = {
        courseId: this.courseId,
        title: this.task.title,
        'content': content,
        'solution': solution,
        state: '1'
      }

      create('task', { data: form }).then(res => {
        // 成功
        this.$message({
          message: '提交成功',
          type: 'success',
          duration: 2000
        })
        this.$router.push({ path: 'courseDetail', query: { courseId: this.courseId }})
        // eslint-disable-next-line handle-callback-err
      }).catch(err => {
        // 失败
        this.$message({
          message: '提交失败',
          type: 'error',
          duration: 2000
        })
      })
    },
    confirmAddQuestion() {
      if (this.editingIndex < 0) this.task.content.push(this.editingQuestion)
      else this.task.content[this.editingIndex] = this.editingQuestion
      // 重置
      this.popEditor = false
      this.editingQuestion = questionTemplate
      this.editingIndex = -1
    },
    editQuestion(index, question) {
      this.editingIndex = index
      this.editingQuestion = question
      this.popEditor = true
    },
    removeQuestion(index) {
      if (index >= 0) {
        this.task.content.splice(index, 1)
      }
    },
    handleClose() {
      this.popEditor = false
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
