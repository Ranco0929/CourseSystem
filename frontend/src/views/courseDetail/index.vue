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
            <el-image :src="course.avatar" />
            <div>
              <span>{{ course.name }}</span>
              <el-divider content-position="center">
                <span v-for="teacher in course.teachers" :key="teacher.userId">{{ teacher.name }}</span>
              </el-divider>
              <span>简介：</span>
              <span>{{ course.info.substring(0, 20) + '...' }}</span>
              <br>
              <br>
              <span>创建时间：</span>
              <span>{{ new Date(course.createdAt).toLocaleString().substring(0, 10) }}</span>
            </div>
          </center>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { get } from '@/api/client'
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
      get('course/get_course', {
        courseId: this.courseId,
        userId: this.$store.getters.userId
      }).then(res => {
        this.course = res.data
      })
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
