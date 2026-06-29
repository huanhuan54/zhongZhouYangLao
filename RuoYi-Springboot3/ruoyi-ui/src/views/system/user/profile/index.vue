<template>
  <div class="profile-page">
    <section class="identity-panel">
      <h2>个人信息</h2>
      <div class="avatar-wrap">
        <user-avatar />
      </div>
      <strong class="login-name">{{ user.userName || '-' }}</strong>
      <div class="identity-list">
        <div><i class="el-icon-mobile-phone" /><span>{{ user.phonenumber || '未设置手机号' }}</span></div>
        <div><i class="el-icon-message" /><span>{{ user.email || '未设置邮箱' }}</span></div>
        <div><i class="el-icon-office-building" /><span>{{ departmentText }}</span></div>
        <div><i class="el-icon-medal" /><span>{{ roleGroup || '普通用户' }}</span></div>
        <div><i class="el-icon-time" /><span>{{ user.createTime || '-' }}</span></div>
      </div>
    </section>

    <main class="profile-content">
      <h2>基本资料</h2>
      <el-tabs v-model="selectedTab">
        <el-tab-pane label="基本资料" name="userinfo">
          <user-info :user="user" @updated="getUser" />
        </el-tab-pane>
        <el-tab-pane label="修改密码" name="resetPwd">
          <reset-pwd />
        </el-tab-pane>
      </el-tabs>
    </main>
  </div>
</template>

<script>
import userAvatar from './userAvatar'
import userInfo from './userInfo'
import resetPwd from './resetPwd'
import { getUserProfile } from '@/api/system/user'

export default {
  name: 'Profile',
  components: { userAvatar, userInfo, resetPwd },
  data() {
    return {
      user: {},
      roleGroup: '',
      postGroup: '',
      selectedTab: 'userinfo'
    }
  },
  computed: {
    departmentText() {
      const dept = this.user.dept && this.user.dept.deptName
      const values = [dept, this.postGroup].filter(Boolean)
      return values.length ? values.join(' / ') : '未分配部门'
    }
  },
  created() {
    const activeTab = this.$route.params && this.$route.params.activeTab
    if (activeTab) this.selectedTab = activeTab
    this.getUser()
  },
  methods: {
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data || {}
        this.roleGroup = response.roleGroup || ''
        this.postGroup = response.postGroup || ''
      })
    }
  }
}
</script>

<style scoped lang="scss">
.profile-page {
  display: grid;
  grid-template-columns: 270px minmax(0, 1fr);
  gap: 18px;
  min-height: calc(100vh - 50px);
  padding: 18px;
  background: #f2f4f7;
}
.identity-panel,
.profile-content {
  background: #fff;
  border: 1px solid #e5e8ed;
}
.identity-panel {
  padding: 22px 24px;
  h2 {
    margin: 0 0 32px;
    font-size: 16px;
  }
}
.avatar-wrap {
  text-align: center;
}
.login-name {
  display: block;
  margin: 10px 0 24px;
  text-align: center;
  font-size: 17px;
}
.identity-list {
  display: grid;
  gap: 16px;
  color: #56606f;
  font-size: 13px;
  div {
    display: grid;
    grid-template-columns: 22px minmax(0, 1fr);
    align-items: center;
  }
  i {
    color: #6b7280;
    font-size: 15px;
  }
  span {
    overflow-wrap: anywhere;
  }
}
.profile-content {
  padding: 22px 28px;
  h2 {
    margin: 0 0 16px;
    font-size: 16px;
  }
  ::v-deep .el-tabs__content {
    max-width: 650px;
    padding-top: 18px;
  }
}
@media (max-width: 900px) {
  .profile-page {
    grid-template-columns: 1fr;
  }
}
</style>
