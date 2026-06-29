<template>
  <el-form ref="form" :model="form" :rules="rules" label-width="88px" class="profile-form">
    <el-form-item label="姓名" prop="nickName">
      <el-input v-model="form.nickName" maxlength="10" show-word-limit />
    </el-form-item>
    <el-form-item label="手机号" prop="phonenumber">
      <el-input v-model="form.phonenumber" maxlength="11" show-word-limit />
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="form.email" maxlength="30" show-word-limit />
    </el-form-item>
    <el-form-item label="性别">
      <el-radio-group v-model="form.sex">
        <el-radio label="0">男</el-radio>
        <el-radio label="1">女</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item class="form-actions">
      <el-button type="primary" @click="submit">保存</el-button>
      <el-button type="danger" plain @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserProfile } from '@/api/system/user'

export default {
  props: {
    user: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      form: {},
      rules: {
        nickName: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
        email: [
          { required: true, message: '邮箱地址不能为空', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
        phonenumber: [
          { required: true, message: '手机号码不能为空', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    user: {
      handler(user) {
        this.form = {
          nickName: user.nickName,
          phonenumber: user.phonenumber,
          email: user.email,
          sex: user.sex
        }
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    submit() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        updateUserProfile(this.form).then(() => {
          this.$modal.msgSuccess('个人资料保存成功')
          this.$emit('updated')
        })
      })
    },
    close() {
      this.$router.push('/index')
    }
  }
}
</script>

<style scoped lang="scss">
.profile-form {
  width: 520px;
  max-width: 100%;
  .el-input {
    width: 100%;
  }
}
.form-actions {
  margin-top: 28px;
}
</style>
