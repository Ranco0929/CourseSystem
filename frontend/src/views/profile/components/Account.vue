<template>
  <div class="user-information">
    <el-row>
      <el-col :span="40">
        <div @click="dialogVisible = true">
          <el-avatar :size="150" :src="avatar" />
        </div>
        <div>
          <el-card><span> 邮箱：{{ email }}</span></el-card>
          <el-card><span> 姓名：{{ name }}</span></el-card>
          <el-card><span> 身份：{{ role }}</span></el-card>
          <el-card><span> 简介：{{ info }}</span></el-card>
        </div>
      </el-col>
    </el-row>
    <!-- 更换头像 -->
    <el-dialog title="更换头像" :visible.sync="dialogVisible" :before-close="handleClose">
      <span>请上传图像</span>
      <el-upload
        class="avatar-uploader"
        action="https://jsonplaceholder.typicode.com/posts/"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload"
      >
        <img v-if="imageUrl" :src="imageUrl" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon" />
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {

  data() {
    return {
      dialogVisible: false,
      imageUrl: '',
      avatar: 'http://image.biaobaiju.com/uploads/20181025/19/1540467434-IhiJNbyXak.jpg'
    }
  },
  computed: {
    ...mapState(['role', 'email', 'name', 'avatar', 'info'])
  },
  methods: {
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done()
        })
        .catch(_ => {})
    },
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw)
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传头像只能是JPG格式！')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过2MB！')
      }
      return isJPG && isLt2M
    }
  }
}
</script>

<style>
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
