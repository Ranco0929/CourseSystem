<template>
  <div class="dashboard-teacher-container">
    <el-row>
      <el-col v-for="(item, index) in courses" :key="item.courseId" :span="4">
        <el-card>
          <img :src="item.avatar" class="image" @click="gotoCourseDetail(item)">
          <div style="font-weight: bold;margin: 10px">{{ item.name }}</div>
          <div class="courseInfo">
            <span v-for="teacher in item.teachers" :key="teacher.userId">{{ teacher.name + ' ' }}</span>
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
    <el-dialog title="课程选择" :visible.sync="dialogFormVisible">
      <el-table :data="allCourses">
        <el-table-column prop="name" label="课程名称" width="150" />
        <el-table-column prop="teachers" label="授课教师" width="200" />
        <el-table-column prop="info" label="简介" />
        <el-table-column
          fixed="right"
          label="操作"
          width="100"
        >
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleClick(scope.row)">选课</el-button>
          </template>
        </el-table-column>
      </el-table>
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
      allCourses: [],

      // 添加课程
      dialogFormVisible: false,
      form: Object.assign({}, createCourseTemplate),
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
    this.getMyCourses()
    this.getAllCourses()
  },
  methods: {
    getMyCourses() {
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
    deleteCourse(course, index) {
      post('course/delete_course', {
        courseId: course.courseId,
        userId: this.$store.getters.userId
      }).then(() => {
        this.getMyCourses()
        this.getAllCourses()
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
    getAllCourses() {
      get('course/get_all_courses', {
        userId: this.$store.getters.userId
      }).then(res => {
        for (const rec of res.data) {
          let teachers = ''
          for (const t of rec.teachers) {
            teachers += t.name + ' '
          }
          rec.teachers = teachers
        }
        this.allCourses = res.data
      }).catch(err => {
        this.$message.error(err)
      })
    },
    handleClick(row) {
      post('course/add_course', {
        courseId: row.courseId,
        userId: this.$store.getters.userId
      }).then(_ => {
        this.getMyCourses()
      }).catch(err => {
        this.$message.error(err)
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
