<template>
  <div class="user-information">
    <el-row>
      <el-col v-for="item in user" :key="item.name" :span="20">
        <el-avatar :size="50" :src="item.avatar" @click="dialogFormVisible = true" />
        <div>
          <span> 学号：{{ item.userId }}</span>
          <br>
          <span> 姓名：{{ item.name }}</span>
          <br>
          <span> 身份：{{ item.role }}</span>
          <br>
          <span> 简介：{{ item.info }}</span>
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

export default {
  data() {
    return {
      dialogVisible: false,
      imageUrl: ''
    }
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
