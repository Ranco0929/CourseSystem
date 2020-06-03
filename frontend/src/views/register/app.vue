<template>
  <el-row type="flex" justify="center">
    <el-form ref="formData" :model="formData" :rules="rules" label-width="80px" @keyup.enter.native="register()">
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="formData.email" placeholder="请输入邮箱" prefix-icon="icon-login_email" clearable />
      </el-form-item>
      <el-form-item prop="password" label="密码">
        <el-input v-model="formData.password" placeholder="请输入密码" type="password" prefix-icon="icon-login_pwd" clearable />
      </el-form-item>
      <el-form-item prop="cheackPassword" label="确认密码">
        <el-input v-model="formData.cheackPassword" placeholder="再次输入密码" type="password" prefix-icon="icon-login_pwd" clearable />
      </el-form-item>
      <el-form-item prop="role" label="身份">
        <el-select v-model="formData.role" placeholder="请选择" type="role" prefix-icon="icon-login_role" clearable>
          <el-option v-for="item in options" :key="item.role" :label="item.label" :value="item.role" />
        </el-select>
      </el-form-item>
      <el-form-item prop="name" label="姓名">
        <el-input v-model="formData.name" placeholder="请输入姓名" type="name" prefix-icon="icon-login_name" clearable />
      </el-form-item>
      <el-form-item prop="info" label="简介">
        <el-input v-model="formData.info" placeholder="请输入简介" type="info" prefix-icon="icon-login_info" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-upload" @click="register('formData', 'dialogVisible')">注册</el-button>
        <el-button @click="resetForm('formData')">重置</el-button>
      </el-form-item>
      <el-button type="primary" style="width:100%;margin-bottom:30px" @click="JumptoLogin">已有账号？点击登录</el-button>

      <el-dialog title="验证邮箱" :visible.sync="dialogVisible" :before-close="handleClose">
        <span>请输入发送到邮箱的校验码</span>
        <el-input v-model="verifiedCode" placeholder="请输入邮箱校验码" name="code" />
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="dialogVisible = false">确定</el-button>
        </span>
      </el-dialog>
    </el-form>
  </el-row>
</template>
<script>
import { post } from '@/api/client'
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
        email: '',
        password: '',
        name: '',
        info: '',
        role: ''
      },
      rules: {
        password: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
        cheackPassword: [{ required: true, validator: validatePass, trigger: 'blur' }],
        name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
        info: [{ required: true, message: '简介不能为空', trigger: 'blur' }],
        email: [{ required: true, message: '邮箱不能为空', trigger: 'blur' }]
      },
      options: [{
        role: 'teacher',
        label: '老师'
      }, {
        role: 'student',
        label: '学生'
      }],
      dialogVisible: false
    }
  },
  methods: {
    register(formName, dialogVisible) {
      dialogVisible = true
      console.log(dialogVisible)
      this.$refs[formName].validate(valid => {
        if (valid) {
          post('user/register', {
            name: formName.name,
            email: formName.email,
            info: formName.info,
            role: formName.role,
            password: formName.password
          })
          this.$message({
            type: 'success',
            message: '注册成功'
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done()
        })
        .catch(_ => {})
    },
    JumptoLogin() {
      this.$router.push({ path: 'login' })
    }
  }
}
</script>
