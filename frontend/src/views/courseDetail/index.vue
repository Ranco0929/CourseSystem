<template>
  <div class="main-area">
    <el-row>
      <el-col :span="18">
        <el-card>
          <component :is="currentRole" />
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="height: 100%;position: fixed;margin-right: 20px;margin-left: 20px;margin-bottom: 20px">
          <center>
            <h4>当前课程</h4>
            <el-image :src="course.avatar" class="image" />
            <div>
              <span>{{ course.name }}</span>
              <el-divider content-position="center">
                <span v-for="teacher in course.teachers" :key="teacher">{{ teacher }}</span>
              </el-divider>
              <span>简介：</span>
              <span>{{ course.info.substring(0, 20) + '...' }}</span>
              <br>
              <br>
              <span>{{ new Date(course.createdAt).toLocaleString() }}</span>
            </div>
          </center>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { find } from '@/api/client'
import teacherCourseDetail from './teacher'
import studentCourseDetail from './student'

export default {
  name: 'CourseDetail',
  components: { teacherCourseDetail, studentCourseDetail },
  data() {
    return {
      currentRole: 'studentCourseDetail',
      courseId: '',
      course: {
        courseId: '',
        name: '',
        info: '',
        avatar: '',
        teachers: [],
        createdAt: '',
        updatedAt: ''
      }
    }
  },
  computed: {
    ...mapGetters([
      'roles'
    ])
  },
  created() {
    if (this.roles.includes('teacher')) {
      this.currentRole = 'teacherCourseDetail'
    }
    if (this.$route.query.courseId !== undefined) {
      this.courseId = this.$route.query.courseId
      this.getCourseInfo()
    }
  },
  methods: {
    async getCourseInfo() {
      if (this.courseId !== undefined) {
        // 获取课程简介
        const course = await find('course', { data: { courseId: this.courseId }})
        // 获取授课老师
        const teachCourse = await find('teach-course', { data: { courseId: this.courseId }})
        const teachers = []
        for (const tc of teachCourse.data) {
          const user = await find('user', { data: { userId: tc.userId }})
          // console.log('user', user)
          teachers.push(user.data[0].name)
        }
        (course.data[0])['teachers'] = teachers
        // console.log('course', course)
        this.course = course.data[0]
      }
    }
  }
}
</script>

<style scoped>
  .main-area{
    padding: 20px;
  }
</style>
