<template>
  <el-form>
    <el-form-item label="身份">
      <el-input v-model="user.role" />
    </el-form-item>
    <el-form-item label="简介">
      <el-input v-model.trim="user.info" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">Update</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          role: '',
          info: ''
        }
      }
    }
  },
  methods: {
    submit() {
      this.$message({
        message: 'User information has been updated successfully',
        type: 'success',
        duration: 5 * 1000
      })
    }
  }
}
</script>
