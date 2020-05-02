<template>
  <div class="app-container">
    <div v-if="user">
      <el-row :gutter="50">
        <el-col :span="18" :xs="24">
          <el-card>
            <el-tabs v-model="activeTab">
              <el-tab-pane label="个人资料" name="Account">
                <activity />
              </el-tab-pane>
              <el-tab-pane label="编辑个人资料" name="EidtAccount">
                <account />
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import Activity from './components/Activity'
import Account from './components/Account'
import { mapGetters } from 'vuex'

export default {
  name: 'Profile',
  components: { Account, Activity },
  data() {
    return {
      user: {},
      activeTab: 'Account'
    }
  },
  computed: {
    ...mapGetters([
      'avatar',
      'userID',
      'name',
      'role',
      'info'
    ])
  },
  created() {
    this.getUser()
  },
  methods: {
    getUser() {
      this.user = {
        avatar: this.avatar,
        userId: this.userId,
        name: this.name,
        role: this.role,
        info: this.info
      }
    }
  }
}
</script>
