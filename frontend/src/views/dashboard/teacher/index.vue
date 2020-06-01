<template>
  <div class="dashboard-teacher-container">
    <el-row>
      <el-col v-for="(item, index) in courses" :key="item.courseId" :span="4">
        <el-card>
          <img :src="item.avatar" class="image" @click="gotoCourseDetail(item)">
          <div style="font-weight: bold;margin: 10px">{{ item.name }}</div>
          <div class="courseInfo">
            <span v-for="t in item.teachers" :key="t.userId">{{ t.name + ' ' }}</span>
          </div>
          <div class="courseInfo">
            {{ item.info.slice(0, 20) + '...' }}
          </div>
          <div class="courseInfo">
            <span>创建时间：</span>
            <span>{{ new Date(item.createdAt).toLocaleDateString().substring(0, 10) }}</span>
            <el-button type="text" @click="deleteCourse(item, index)">删除</el-button>
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
    <!-- 创建课程的弹出框 -->
    <el-dialog title="创建课程" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules">
        <el-form-item
          prop="name"
          label="课程名称"
          :rules="[{ required: true, message: '请输入课程名称', trigger: 'blur' }]"
        >
          <el-input v-model="form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="课程简介">
          <el-input v-model="form.info" autocomplete="off" />
        </el-form-item>
        <el-form-item>
          <el-form v-model="form.teachers">
            <el-form-item v-for="(teacher, index) in form.teachers" :key="index">
              <span>{{ teacher.value }}</span>
              <el-button type="text" @click="deleteTeacher(index)">删除</el-button>
            </el-form-item>
          </el-form>
          <el-autocomplete
            v-if="showSelectTeacher"
            :fetch-suggestions="queryTeachers"
            placeholder="请输入老师姓名"
            @select="handleSelect"
          />
          <el-button type="primary" @click="showSelectTeacher = true">添加老师</el-button>
        </el-form-item>
        <el-form-item>
          <!-- TODO 这里的action需要改为项目的东西 -->
          <el-upload
            action="/dev-api/vue-element-admin/course/create"
            list-type="picture-card"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
          >
            <i class="el-icon-plus" />
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="createCourse">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { get, post } from '@/api/client'

const createCourseTemplate = {
  name: '',
  info: '',
  avatar: '',
  teachers: []
}

export default {
  name: 'DashboardTeacher',
  data() {
    return {
      info: '',
      courses: [],

      // 创建课程
      dialogFormVisible: false,
      form: createCourseTemplate,
      rules: {
        name: [
          { required: true, message: '请输入课程', trigger: 'blur' },
          { type: 'name', message: '课程名不能为空', trigger: ['blur', 'change'] }
        ]
      },
      showSelectTeacher: false,

      avatarVisible: false
    }
  },
  created() {
    // 初始化
    get('course/get_courses', { userId: this.$store.getters.userId })
      .then(res => {
        this.courses = res.data
      })
      .catch(err => {
        this.$message.error(err.msg)
        if (err.code === 40002 || err.code === 40003 || err.code === 40005) {
          this.$store.dispatch('user/resetToken')
            .catch(err => {
              this.$message.error(err.msg)
            })
            .finally(() => {
              this.$router.push({ path: 'login', query: {}})
            })
        }
      })
  },
  methods: {
    deleteCourse(course, index) {
      post('course/delete_course', {
        courseId: course.courseId,
        userId: this.$store.getters.userId
      }).then(_ => {
        get('course/get_courses', { userId: this.$store.getters.userId })
          .then(res => {
            this.courses = res.data
          })
        this.$message({
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
      }).catch(err => {
        this.$message({
          message: '删除失败:' + err,
          type: 'error',
          duration: 2000
        })
      })
    },
    gotoCourseDetail(course) {
      this.$router.push({ path: 'courseDetail', query: { courseId: course.courseId }})
    },
    queryTeachers(queryString, cb) {
      get('user/get_teachers', {
        data: {}
      }).then(teachers => {
        const result = queryString ? teachers.data.filter(this.createStateFilter(queryString)) : teachers.data
        const ret = []
        for (const rec of result) {
          ret.push({ value: rec.name, userId: rec.userId })
        }
        cb(ret)
      })
    },
    createStateFilter(queryString) {
      return (user) => {
        return (user.name.toLowerCase().indexOf(queryString.toLowerCase()) === 0)
      }
    },
    deleteTeacher(index) {
      this.form.teachers.splice(index, 1)
    },
    handleSelect(item) {
      this.form.teachers.push(item)
      this.showSelectTeacher = false
    },
    handleRemove(file, fileList) {
      console.log(file, fileList)
    },
    handlePictureCardPreview(file) {
      this.form.avatar = file.url
      this.avatarVisible = true
    },
    createCourse() {
      const userId = []
      for (const teacher of this.form.teachers) {
        userId.push(teacher.userId)
      }
      const req = Object.assign({}, this.form, { creator: this.$store.getters.userId, teachers: userId })
      post('course/create_course', req).then(res => {
        this.form = Object.assign({}, createCourseTemplate)
        this.$router.go(0)
      }).catch(err => {
        this.$message.error('创建课程错误：' + err)
      }).finally(() => {
        this.dialogFormVisible = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  .courseInfo {
    font-size: smaller;
    font-weight: lighter;
    margin: 10px;
  }
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
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
