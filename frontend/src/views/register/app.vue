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
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="formData.email" placeholder="请输入邮箱" type="email" prefix-icon="icon-login_email" clearable />
      </el-form-item>
      <div data-v-89436cbc class="form-group yzm yzm_buttom_a back-fff">
        <div data-v-89436 class="code-input el-input">
          <input type="text" autocomplete="off" placeholder="请输入邮箱校验码" name="code" class="el-input__inner">
        </div>
        <div id="captchCheck" data-v-89436cbc class="check" style="display:none;" />
        <button data-v-89435cbc type="button" class="el-button yzm-buttom el-button--primary">
          <span>点击获取</span>
        </button>
        <p data-v-89436cbc class="error-message" />
      </div>
      <!--      <button :style="config.cssSet?config.cssSet:''" class="common-getCodeBtn" @click="togetCode" :disabled="isGetting">-->
      <!--        {{ message?message:config.initText }}-->
      <!--      </button>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-upload" @click="register('formData')">注册</el-button>
        <el-button @click="resetForm('formData')">重置</el-button></el-form-item>
      <router-link to="login">已有密码？登录</router-link>

    </el-form>
  </el-row>
</template>
<script>
import { create } from '@/api/client'
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
        name: '',
        info: '',
        id: '',
        value: '',
        email: ''
      },
      rules: {
        userName: [{ required: true, message: '用户名不能为空', trigger: 'blur' }],
        password: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
        cheackPassword: [{ required: true, validator: validatePass, trigger: 'blur' }],
        name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
        info: [{ required: true, message: '简介不能为空', trigger: 'blur' }],
        id: [{ required: true, message: '学工号不能为空', trigger: 'blur' }],
        value: [{ required: true, message: '身份不能为空', trigger: 'blur' }],
        email: [{ required: true, message: '邮箱不能为空', trigger: 'blur' }]
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
          create('formData')
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
