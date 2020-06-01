<template>
  <el-form>
    <el-form-item label="邮箱">
      <el-input ref="email" v-model="user.email" />
    </el-form-item>
    <el-form-item label="身份">
      <el-input ref="role" v-model="user.role" />
    </el-form-item>
    <el-form-item label="简介">
      <el-input ref="info" v-model="user.info" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">Update</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  data() {
    return {
      user: {
        email: '',
        role: '',
        info: ''
      }
    }
  },
  methods: {
    submit() {
      this.$store.commit('SET_INFO', this.$refs.info.value)
      this.$store.commit('SET_EMAIL', this.$refs.email.value)
      this.$store.commit('SET_ROLE', this.$refs.role.value)
      this.$message({
        message: 'User information has been updated successfully',
        type: 'success',
        duration: 5 * 1000
      })
    }
  }
}
</script>
