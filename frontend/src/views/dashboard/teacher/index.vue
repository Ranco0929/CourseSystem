<template>
  <div class="dashboard-teacher-container">
    <el-row>
      <el-col v-for="item in courses" :key="item.courseId" :span="4">
        <el-card>
          <el-image :src="item.avatar" class="image" @click="gotoCourseDetail(item)" />
          <div>
            <span>{{ item.name }}</span>
            <br>
            <span>授课教师：</span>
            <span v-for="teacher in item.teachers" :key="teacher">{{ teacher }}</span>
            <br>
            <span>简介：</span>
            <span>{{ item.info.slice(0, 20) + '...' }}</span>
            <br>
            <span>{{ new Date(item.createdAt).toLocaleString() }}</span>
            <el-button type="text" class="button">编辑</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <img
          src="https://images.669pic.com/element_min_new_pic/28/4/43/25/25c8336849c4a8dfd0220697d266b4c3.png"
          class="image"
          @click="dialogFormVisible = true"
        >
      </el-col>
    </el-row>
    <markdown-editor v-model="content1" height="300px" />
    <!-- 创建课程的弹出框 -->
    <el-dialog title="创建课程" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules">
        <el-form-item
          prop="name"
          label="课程名称"
        >
          <el-input v-model="form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="课程简介">
          <el-input v-model="form.info" autocomplete="off" />
        </el-form-item>
        <el-form-item>
          <pan-thumb v-show="image !== null" :image="image" />
          <el-button
            type="primary"
            icon="el-icon-upload"
            style="position: absolute;bottom: 15px;margin-left: 40px;"
            @click="imagecropperShow=true"
          >
            上传封面
          </el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="createCourse">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 上传封面的上传框 -->
    <image-cropper
      v-show="imagecropperShow"
      :key="imagecropperKey"
      :width="300"
      :height="300"
      url="https://httpbin.org/post"
      lang-type="en"
      @close="close"
      @crop-upload-success="cropSuccess"
    />
  </div>
</template>

<script>
import { find } from '@/api/client'
import imageCropper from '@/components/ImageCropper'
import PanThumb from '@/components/PanThumb'
import markdownEditor from '@/components/MarkdownEditor'

export default {
  name: 'DashboardTeacher',
  components: {
    imageCropper,
    PanThumb,
    markdownEditor
  },
  data() {
    return {
      courses: [],

      // 创建课程
      dialogFormVisible: false,
      form: {
        name: '',
        info: '',
        avatar: '',
        teacher: []
      },
      rules: {
        name: [
          { required: true, message: '请输入课程', trigger: 'blur' },
          { type: 'name', message: '课程名不能为空', trigger: ['blur', 'change'] }
        ]
      },

      // 封面上传
      imagecropperShow: false,
      imagecropperKey: 0,
      image: 'https://wpimg.wallstcn.com/577965b9-bb9e-4e02-9f0c-095b41417191'
    }
  },
  created() {
    // 初始化
    this.getCourse()
  },
  methods: {
    async getCourse() {
      // 获取用户信息
      const info = await this.$store.dispatch('user/info')
      // 获取用户授课信息
      const teachCourse = await find('teach-course', { data: { userId: info.userId }})

      // 获取用户教授课程的信息
      const rec = []
      for (const tc of teachCourse.data) {
        const course = await find('course', { data: { courseId: tc.courseId }})
        const tc2 = await find('teach-course', { data: { courseId: tc.courseId }})

        const users = []
        for (const t of tc2.data) {
          const user = await find('user', { data: { userId: t.userId }})
          users.push(user.data[0].name)
        }
        (course.data[0])['teachers'] = users
        rec.push(course.data[0])
      }
      // 对data域中的数据从新进行赋值
      this.courses = rec
    },
    gotoCourseDetail(course) {
      this.$router.push({ path: 'courseDetail', query: { courseId: course.courseId }})
    },
    cropSuccess(resData) {
      this.imagecropperShow = false
      this.imagecropperKey = this.imagecropperKey + 1
      this.image = resData.files.avatar
    },
    close() {
      this.imagecropperShow = false
    },
    createCourse() {
      // TODO
      console.log('upload success')
    }
  }
}
</script>

<style lang="scss" scoped>
  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }
  .button {
    padding: 0;
    float: right;
  }
  .image {
    width: 100%;
    padding: 10px;
    display: block;
  }
</style>
