<template>
  <el-form ref="form" :model="user" :rules="formRules" label-width="90px" class="password-form">
    <el-form-item label="原密码" prop="oldPassword">
      <el-input v-model="user.oldPassword" placeholder="请输入原密码" type="password" show-password />
    </el-form-item>
    <el-form-item label="新密码" prop="newPassword" :rules="infoPwdValidator">
      <el-input v-model="user.newPassword" placeholder="8-20位，包含数字、字母和特殊符号" type="password" show-password />
    </el-form-item>
    <el-form-item label="确认密码" prop="confirmPassword">
      <el-input v-model="user.confirmPassword" placeholder="请再次输入新密码" type="password" show-password />
    </el-form-item>
    <el-form-item class="form-actions">
      <el-button type="primary" @click="submit">保存</el-button>
      <el-button type="danger" plain @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserPwd } from '@/api/system/user'
import passwordRule from '@/utils/passwordRule'

export default {
  mixins: [passwordRule],
  data() {
    return {
      user: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
    }
  },
  computed: {
    formRules() {
      return {
        oldPassword: [{ required: true, message: '原密码不能为空', trigger: 'blur' }],
        confirmPassword: [
          { required: true, message: '确认密码不能为空', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.user.newPassword !== value ? callback(new Error('两次输入的密码不一致')) : callback()
            },
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    submit() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        this.$modal.confirm('密码修改成功后需要重新登录，是否继续？').then(() => {
          return updateUserPwd(this.user.oldPassword, this.user.newPassword)
        }).then(() => {
          this.$modal.msgSuccess('密码修改成功，请重新登录')
          this.$store.dispatch('LogOut').then(() => {
            location.href = '/login'
          })
        }).catch(() => {})
      })
    },
    close() {
      this.$router.push('/index')
    }
  }
}
</script>

<style scoped lang="scss">
.password-form {
  width: 520px;
  max-width: 100%;
}
.form-actions {
  margin-top: 28px;
}
</style>
