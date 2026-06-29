<template>
  <div v-loading="loading" class="task-detail-page">
    <section class="detail-section elder-section">
      <h2>基本信息</h2>
      <el-descriptions :column="3">
        <el-descriptions-item label="老人姓名">{{ task.elderName }}</el-descriptions-item>
        <el-descriptions-item label="护理等级">{{ task.nursingLevel }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ task.elderGender }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ elderAge }}岁</el-descriptions-item>
        <el-descriptions-item label="床位号">{{ task.bedNo }}</el-descriptions-item>
        <el-descriptions-item label="护理员姓名">{{ task.caregiverName }}</el-descriptions-item>
      </el-descriptions>
      <img src="/static/service/hy-009.png" class="elder-photo" alt="老人照片">
    </section>

    <section class="detail-section">
      <h2>护理项目</h2>
      <el-descriptions :column="2">
        <el-descriptions-item label="护理项目名称">{{ task.projectName }}</el-descriptions-item>
        <el-descriptions-item label="关联单据">{{ task.relatedOrderNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="执行状态">
          <el-tag size="mini" :type="statusMeta.type">{{ statusMeta.label }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="项目类型">{{ task.taskType === 0 ? '护理计划内' : '护理计划外' }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ task.createBy || 'admin' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ task.createTime }}</el-descriptions-item>
        <el-descriptions-item label="期望服务时间">{{ task.expectedServiceTime }}</el-descriptions-item>
        <el-descriptions-item label="备注信息">{{ task.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </section>

    <section v-if="task.status === 1" class="detail-section record-section">
      <h2>执行记录</h2>
      <el-descriptions :column="1">
        <el-descriptions-item label="执行人">{{ task.updateBy || task.caregiverName }}</el-descriptions-item>
        <el-descriptions-item label="执行时间">{{ task.executeTime }}</el-descriptions-item>
        <el-descriptions-item label="执行图片">
          <el-image
            v-if="task.executeImage"
            :src="imageUrl(task.executeImage)"
            :preview-src-list="[imageUrl(task.executeImage)]"
            fit="cover"
            class="record-image"
          />
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="执行记录">{{ task.executeRecord || '-' }}</el-descriptions-item>
      </el-descriptions>
    </section>

    <section v-if="task.status === 2" class="detail-section record-section">
      <h2>取消记录</h2>
      <el-descriptions :column="1">
        <el-descriptions-item label="取消人">{{ task.updateBy || '-' }}</el-descriptions-item>
        <el-descriptions-item label="取消时间">{{ task.cancelTime }}</el-descriptions-item>
        <el-descriptions-item label="取消原因">{{ task.cancelReason || '-' }}</el-descriptions-item>
      </el-descriptions>
    </section>

    <div class="back-row">
      <el-button type="primary" icon="el-icon-back" @click="$router.back()">返回</el-button>
    </div>
  </div>
</template>

<script>
import { getTask } from '@/api/nursing/task'
import { isExternal } from '@/utils/validate'

export default {
  name: 'NursingTaskDetail',
  data() {
    return {
      loading: false,
      task: {}
    }
  },
  computed: {
    elderAge() {
      return 68 + (Number(this.task.residentId || 1) % 18)
    },
    statusMeta() {
      return [
        { label: '待执行', type: 'warning' },
        { label: '已执行', type: 'success' },
        { label: '已取消', type: 'info' }
      ][this.task.status] || { label: '未知', type: 'info' }
    }
  },
  created() {
    this.loading = true
    getTask(this.$route.params.id).then(res => {
      this.task = res.data || {}
    }).finally(() => {
      this.loading = false
    })
  },
  methods: {
    imageUrl(value) {
      if (!value) return ''
      return isExternal(value) || value.indexOf('/static/') === 0 ? value : process.env.VUE_APP_BASE_API + value
    }
  }
}
</script>

<style scoped lang="scss">
.task-detail-page {
  min-height: calc(100vh - 50px);
  padding: 18px;
  background: #f2f4f7;
}
.detail-section {
  position: relative;
  padding: 20px 22px;
  margin-bottom: 14px;
  background: #fff;
  border: 1px solid #e5e8ed;
  h2 {
    margin: 0 0 20px;
    font-size: 16px;
  }
  ::v-deep .el-descriptions-item__label {
    width: 110px;
    color: #7b8491;
  }
}
.elder-section {
  padding-right: 180px;
  min-height: 180px;
}
.elder-photo {
  position: absolute;
  right: 28px;
  top: 42px;
  width: 120px;
  height: 120px;
  object-fit: cover;
}
.record-image {
  width: 120px;
  height: 100px;
}
.back-row {
  padding: 8px 0 18px;
  text-align: center;
}
@media (max-width: 800px) {
  .elder-section {
    padding-right: 22px;
    padding-top: 170px;
  }
  .elder-photo {
    top: 45px;
    left: 22px;
  }
}
</style>
