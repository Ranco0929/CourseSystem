<template>
  <div class="index">
    <div v-if="user">
      <el-row :gutter="50">
        <el-col :span="18" :xs="24">
          <el-card>
            <el-tabs v-model="activeTab">
              <el-tab-pane label="个人资料" name="Account">
                <Account />
              </el-tab-pane>
              <el-tab-pane label="编辑个人资料" name="EidtAccount">
                <EidtAccount />
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import Account from '@/views/profile/components/Account'
import EidtAccount from '@/views/profile/components/EidtAccount'

export default {
  name: 'Index',
  components: { EidtAccount, Account },
  data() {
    return {
      user: {},
      activeTab: 'Account'
    }
  }
}
</script>

<style>

</style>
