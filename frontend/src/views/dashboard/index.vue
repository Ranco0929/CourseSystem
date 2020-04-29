<template>
  <div class="dashboard-container">
    <component :is="currentRole" />
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
    console.log('dashboard created', this.roles)
    if (this.roles.includes('teacher')) {
      this.currentRole = 'teacherDashboard'
    }
  }
}
</script>
