<template>
  <el-row type="flex" justify="center">
    <el-form ref="formData" :model="formData" :rules="rules" label-width="80px" @keyup.enter.native="register()">
      <el-form-item prop="userName" label="用户名">
        <el-input v-model="formData.userName" placeholder="请输入用户名" prefix-icon="icon-login_user" clearable />
      </el-form-item>
      <el-form-item prop="password" label="密码">
        <el-input v-model="formData.password" placeholder="请输入密码" type="password" prefix-icon="icon-login_pwd" clearable />
      </el-form-item>
      <el-form-item prop="cheackPassword" label="确认密码">
        <el-input v-model="formData.cheackPassword" placeholder="再次输入密码" type="password" prefix-icon="icon-login_pwd" clearable />
      </el-form-item>
      <el-form-item prop="role" label="身份">
        <el-select v-model="formData.value" placeholder="请选择" type="role" prefix-icon="icon-login_role" clearable>
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item prop="name" label="姓名">
        <el-input v-model="formData.name" placeholder="请输入姓名" type="name" prefix-icon="icon-login_name" clearable />
      </el-form-item>
      <el-form-item prop="info" label="简介">
        <el-input v-model="formData.info" placeholder="请输入简介" type="info" prefix-icon="icon-login_info" clearable />
      </el-form-item>
      <el-form-item prop="id" label="学工号">
        <el-input v-model="formData.id" placeholder="请输入学工号" type="id" prefix-icon="icon-login_id" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-upload" @click="register('formData')">注册</el-button>
        <el-button @click="resetForm('formData')">重置</el-button></el-form-item>
      <router-link to="login">已有密码？登录</router-link>

    </el-form>
  </el-row>
</template>
<script>
export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.formData.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }

    return {
      formData: {
        userName: '',
        password: '',
        cheackPassword: '',
        name: '',
        info: '',
        id: '',
        value: ''
      },
      rules: {
        userName: [{ required: true, message: '用户名不能为空', trigger: 'blur' }],
        password: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
        cheackPassword: [{ required: true, validator: validatePass, trigger: 'blur' }],
        name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
        info: [{ required: true, message: '简介不能为空', trigger: 'blur' }],
        id: [{ required: true, message: '学工号不能为空', trigger: 'blur' }],
        value: [{ required: true, message: '身份不能为空', trigger: 'blur' }]
      },
      options: [{
        value: '1',
        label: '老师'
      }, {
        value: '2',
        label: '学生'
      }]
    }
  },
  methods: {
    register(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$message({
            type: 'success',
            message: '注册成功'
          })
          this.$router.push({ name: 'login' })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    }

  }
}
</script>
