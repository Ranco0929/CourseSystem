<template>
  <div class="main-area">
    <el-card>
      <component :is="currentRole" />
    </el-card>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import teacherDashboard from './teacher'
import studentDashboard from './student'

export default {
  name: 'Dashboard',
  components: { teacherDashboard, studentDashboard },
  data() {
    return {
      currentRole: 'studentDashboard'
    }
  },
  computed: {
    ...mapGetters([
      'roles'
    ])
  },
  created() {
    if (this.roles.includes('teacher')) {
      this.currentRole = 'teacherDashboard'
    }
  }
}
</script>

<style scoped>
  .main-area{
    padding: 20px;
  }
</style>
